package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.hyphenate.chat.EMClient;
import com.jim.ixbx.presenter.Contract.SplashActivityCon;

/**
 * Created by Jim.
 */

public class SplashPresenter implements SplashActivityCon.Presenter {
    Activity activity;
    SplashActivityCon.View view;
    public SplashPresenter(Activity act, SplashActivityCon.View view){
        this.activity = act;
        this.view = view;
        this.view.setPresenter(this);
    }
    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void checkLogined() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            //已经登录过了
            view.onCheckedLogin(true);
        } else {
            //还未登录
            view.onCheckedLogin(false);
        }

    }
}
