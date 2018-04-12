package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.jim.ixbx.presenter.Contract.MainActivityCon;

/**
 * Created by Jim.
 */

public class MainActivityPre implements MainActivityCon.Presenter {
    Activity mActivity;
    MainActivityCon.View mView;

    public MainActivityPre(Activity activity, MainActivityCon.View view) {
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
}
