package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.jim.ixbx.presenter.Contract.RegistActivityCon;

/**
 * Created by Jim.
 */

public class RegistActivityPre implements RegistActivityCon.Presenter {
    Activity mActivity;
    RegistActivityCon.View mView;

    public RegistActivityPre(Activity activity, RegistActivityCon.View view) {
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
