package com.jim.ixbx.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.SplashActivityCon;
import com.jim.ixbx.presenter.activity.SplashPresenter;
import com.jim.ixbx.view.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends BaseActivity implements SplashActivityCon.View {
    private static final long DURATION = 2000;
    SplashActivityCon.Presenter mPresenter;
    @InjectView(R.id.iv_splash)
    ImageView mIvSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        new SplashPresenter(this, this);
        /**
         * 1. 判断是否已经登录了
         *
         * 2. 如果登录了，直接进入MainActivity
         *
         * 3. 否则闪屏2秒后（渐变动画），进入LoginActivity
         */
        mPresenter.checkLogined();
    }

    @Override
    public void setPresenter(SplashActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCheckedLogin(boolean b) {
        if (b) {
            startActivity(MainActivity.class, true);
        } else {
//            闪屏2秒后（渐变动画），进入LoginActivity
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mIvSplash, "alpha", 0, 1).setDuration(DURATION);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class, true);
                }
            });

        }
    }
}
