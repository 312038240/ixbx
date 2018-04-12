package com.jim.ixbx;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.jim.ixbx.adapter.IxbxMessageListener;
import com.jim.ixbx.utils.ThreadUtils;
import com.jim.ixbx.view.activity.ChatActivity;
import com.jim.ixbx.view.activity.LoginActivity;
import com.jim.ixbx.view.activity.MainActivity;
import com.jim.ixbx.view.base.BaseActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by Jim.
 */

public class IxbxApplication extends Application {
    private static final String TAG = "Jim_IxbxApplication";

    private SoundPool mSoundPool;
    private int mDuanSound;
    private int mYuluSound;
    private List<BaseActivity> mBaseActivityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initHuanxin();
        initBmob();
        initSoundPool();
    }

    private void initBmob() {
        LitePal.initialize(this);
        //第一：默认初始化
        Bmob.initialize(this, "d99c148cd4967723541cda1d6867ba25");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    public void addActivity(BaseActivity activity){
        if (!mBaseActivityList.contains(activity)){
            mBaseActivityList.add(activity);
        }
    }
    public void removeActivity(BaseActivity activity){
        mBaseActivityList.remove(activity);
    }
    /**
     * 初始化环信
     */
    private void initHuanxin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        /**
         * 下面的代码是为了避免环信被初始化2次
         */
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        initContactListener();
        initMessageListener();
        initConnectionListener();
    }

    /**
     * 监听好友状态事件
     */
    private void initContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //好友请求被同意
                //发出通知让ContactFragment更新UI
               // EventBus.getDefault().post(new OnContactUpdateEvent(s, true));
            }

            @Override
            public void onContactDeleted(String s) {
                //被删除时回调此方法
               // EventBus.getDefault().post(new OnContactUpdateEvent(s, false));
                Log.d(TAG, "onContactDeleted: " + s);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Log.d(TAG, "onContactInvited: " + username + "/" + reason);
                //同意或者拒绝
                try {
                    //默认好友请求是自动同意的，如果要手动同意需要在初始化SDK时调用
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //增加了联系人时回调此方法
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //好友请求被拒绝
            }

        });
    }

    /**
     * \
     * 注册消息监听来接收消息
     */
    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new IxbxMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                if (messages != null && messages.size() > 0) {
                    /**
                     * 1. 判断当前应用是否在后台运行
                     * 2. 如果是在后台运行，则发出通知栏
                     * 3. 如果是在后台发出长声音
                     * 4. 如果在前台发出短声音
                     */
                    if (isRuninBackground()) {
                        sendNotification(messages.get(0));
                        //发出长声音
                        //参数2/3：左右喇叭声音的大小
                        mSoundPool.play(mYuluSound, 1, 1, 0, 0, 1);
                    } else {
                        //发出短声音
                        mSoundPool.play(mDuanSound, 1, 1, 0, 0, 1);
                    }
                    //          EventBus.getDefault().post(messages.get(0));
                }
            }
        });

    }

    /**
     * 注册连接状态监听
     */
    private void initConnectionListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int i) {
                if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    /**EMError.USER_LOGIN_ANOTHER_DEVICE,同一个账号异地登录；
                     *  将当前任务栈中所有的Activity给清空掉
                     *  重新打开登录界面
                     */
                    for (BaseActivity baseActivity : mBaseActivityList) {
                        baseActivity.finish();
                    }

                    Intent intent = new Intent(IxbxApplication.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IxbxApplication.this, "您已在其他设备上登录了，请重新登录。", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (i == EMError.USER_REMOVED) {
                    // * EMError.USER_REMOVED，账号在后台被删除。
                } else {
                    if (NetUtils.hasNetwork(IxbxApplication.this)) {
                        //连接不到聊天服务器
                    } else {
                        //当前网络不可用，请检查网络设置
                    }
                }
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    private boolean isRuninBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    private void sendNotification(EMMessage message) {
        EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //延时意图
        /**
         * 参数2：请求码 大于1
         */
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("username", message.getFrom());

        Intent[] intents = {mainIntent, chatIntent};
        //获取一个用于启动 Activity  的 PendingIntent 对象
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true) //当点击后自动删除
                .setSmallIcon(R.mipmap.message) //设置小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.default_avatar))
                .setContentTitle("您有一条新消息")//设置通知标题
                .setContentText(messageBody.getMessage()) //设置通知内容
                .setContentInfo(message.getFrom())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notificationManager.notify(1, notification);
//        notification.set= R.mipmap.ic_launcher_round;
//        //图标
//        notification.icon=R.mipmap.message;
//        //添加声音提示
//        notification.defaults = Notification.DEFAULT_SOUND;
// /* 或者使用以下几种方式
//  * notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
//  * notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
//  * 如果想要让声音持续重复直到用户对通知做出反应，则可以在notification的flags字段增加"FLAG_INSISTENT"
//  * 如果notification的defaults字段包括了"DEFAULT_SOUND"属性，则这个属性将覆盖sound字段中定义的声音
//  */
//        //audioStreamType的值必须AudioManager中的值，代表响铃模式
//        notification.audioStreamType = AudioManager.ADJUST_LOWER;
//        //添加LED灯提醒
//        notification.defaults |= Notification.DEFAULT_LIGHTS;
//        //或者可以自己的LED提醒模式:
// /*notification.ledARGB = 0xff00ff00;
// notification.ledOnMS = 300; //亮的时间
// notification.ledOffMS = 1000; //灭的时间
// notification.flags |= Notification.FLAG_SHOW_LIGHTS;*/
//        //添加震动
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        //或者可以定义自己的振动模式：
// /*long[] vibrate = {0,100,200,300}; //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
// notification.vibrate = vibrate;*/
//        //状态栏提示信息
//        notification.tickerText = "您有一条新消息";
//        //获取当前时间
//        notification.when = System.currentTimeMillis();
//        //加载自定义布局
//        notification.contentView = getRemoteViews(context,"发现新版本，点击下载");
//        // 点击清除按钮或点击通知后会自动消失
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;

    }


    /**
     * 初始化音效池
     */
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mDuanSound = mSoundPool.load(this, R.raw.duan, 1);
        mYuluSound = mSoundPool.load(this, R.raw.yulu, 1);
    }
}
