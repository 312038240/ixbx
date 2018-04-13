package com.jim.ixbx.presenter.activity;

import android.app.Activity;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jim.ixbx.model.UserBean;
import com.jim.ixbx.presenter.Contract.LoginActivityCon;
import com.jim.ixbx.utils.ThreadUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jim.
 */

public class LoginActivityPre implements LoginActivityCon.Presenter {
    private static final String TAG = "Jim_LoginActivityPre";
    Activity mActivity;
    LoginActivityCon.View mView;

    public LoginActivityPre(Activity activity, LoginActivityCon.View view) {
        mActivity = activity;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void login(final String username, final String pwd) {
        //环信目前（3.5.x）的所有回调方法都是在子线程中回调的
        EMClient.getInstance().login(username,pwd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
//                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
//                mActivity.finish();
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLogin(username,pwd,true,null);
                    }
                });

                Log.e("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                Log.e("main", "登录聊天服务器失败！   "+message+"   "+code);
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (code) {
                            case 200:
                                mView.onLogin(username,pwd,false,message);
                                break;
                            case 204:
                                registerUser(username,pwd);
                                break;
                            default:
                                mView.onLogin(username,pwd,false,message);
                                break;
                        }
                    }
                });

            }
        });
    }

    private void registerUser(final String username, final String pwd) {
        //注册bmob
        UserBean userBean=new UserBean();
        userBean.setUsername(username);
        userBean.setPassword(pwd);
        userBean.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(final UserBean userBean, final BmobException e) {
                if (e==null){
                    //成功了再去注册环信平台
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);
                                //环信注册成功
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onLogin(username,pwd,true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //将Bmob上注册的user给删除掉
                                userBean.delete();
                                //环信注册失败了
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onLogin(username,pwd,false,e1.toString());
                                    }
                                });
                            }
                        }
                    });
                }else {
                    //失败了，将结果告诉Activity
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onLogin(username,pwd,false,e.getMessage());
                        }
                    });

                }
            }
        });
    }
}
