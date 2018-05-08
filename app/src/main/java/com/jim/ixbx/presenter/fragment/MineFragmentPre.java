package com.jim.ixbx.presenter.fragment;

import android.app.Activity;

import com.jim.ixbx.presenter.Contract.MineFragmentCon;

/**
 * Created by Jim.
 */

public class MineFragmentPre implements MineFragmentCon.Presenter {
    Activity mActivity;
    MineFragmentCon.View mView;

    public MineFragmentPre(Activity activity, MineFragmentCon.View view) {
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
