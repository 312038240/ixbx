package com.jim.ixbx.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.LoginActivityCon;
import com.jim.ixbx.presenter.activity.LoginActivityPre;
import com.jim.ixbx.utils.StringUtils;
import com.jim.ixbx.view.base.BaseActivity;
import com.jim.ixbx.view.views.CustomVideoView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity implements LoginActivityCon.View,  View.OnClickListener, TextView.OnEditorActionListener {
    private static final String TAG = "Jim_LoginActivity";
    LoginActivityCon.Presenter mPresenter;
    @InjectView(R.id.video_view)
    CustomVideoView mVideoView;
    @InjectView(R.id.et_username)
    EditText mEtUsername;
    @InjectView(R.id.til_username)
    TextInputLayout mTilUsername;
    @InjectView(R.id.et_pwd)
    EditText mEtPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    private String mUri;
    private int mPositionWhenPaused = -1;

    private Rationale mRationale = new Rationale() {
        @Override
        public void showRationale(Context context, List<String> permissions,
                                  RequestExecutor executor) {
            // 这里使用一个Dialog询问用户是否继续授权。
            executor.execute();
//            // 如果用户中断：
            //     executor.cancel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //沉浸式状态栏,大于等于4.4版本支持状态栏的效果
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        new LoginActivityPre(this, this);
        initView();

    }

    /**
     * 当再次startActivity的时候，接收新的Intent对象
     * 调用的前提是该启动模式是singleTask，或者singleTop但是他得在最上面才有效
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mEtUsername.setText(getUserName());
        mEtPwd.setText(getPwd());
    }

    private void initView() {
        mUri = "android.resource://" + getPackageName() + "/" + R.raw.video;
        mVideoView.requestLayout();
        //开始播放
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        //设置为循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setVideoPath(mUri);
                mVideoView.start();
            }
        });
        mEtUsername.setText(getUserName());
        mEtPwd.setText(getPwd());
        mEtPwd.setOnEditorActionListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        mVideoView.setVideoPath(mUri);
        mVideoView.start();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    @Override
    public void setPresenter(LoginActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 回车点击监听
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
        }
        return false;
    }

    private void login() {
        final String username = mEtUsername.getText().toString().trim();
        final String pwd = mEtPwd.getText().toString().trim();
        if (!StringUtils.checkUsername(username)) {
            mTilUsername.setErrorEnabled(false);
            mTilUsername.setError("用户名不合法");
            mEtUsername.requestFocus(View.FOCUS_RIGHT);
            return;
        } else {
            mTilUsername.setErrorEnabled(true);
        }
        if (!StringUtils.checkPwd(pwd)) {
            mTilPwd.setErrorEnabled(true);
            mTilPwd.setError("密码不合法");
            mEtPwd.requestFocus(View.FOCUS_RIGHT);
            return;
        } else {
            mTilPwd.setErrorEnabled(false);
        }
        AndPermission.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //同意
                        showDialog("正在玩命登录中...");
                        mPresenter.login(username,pwd);
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                //退出应用
                System.exit(0);
//                        //拒绝
//                        if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, permissions)) {
//                            // 这些权限被用户总是拒绝。
//                            SettingService settingService = AndPermission.permissionSetting(LoginActivity.this);
//
//                            // 如果用户同意去设置：
//                            settingService.execute();
//
//                            // 如果用户不同意去设置：
//                            settingService.cancel();
//                        }
            }
        }).start();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            default:

                break;
        }
    }

    @Override
    public void onLogin(String username, String pwd, boolean b, String msg) {
        hideDialog();
        if (b){
            saveUser(username,pwd);
            startActivity(MainActivity.class,true);
        }else {
            Toast.makeText(this, "登录失败"+msg, Toast.LENGTH_SHORT).show();
        }
    }
}
