package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jim.ixbx.presenter.Contract.AccountSettingActivityCon;
import com.jim.ixbx.utils.ThreadUtils;

/**
 * Created by Jim.
 */

public class AccountSettingActivityPre implements AccountSettingActivityCon.Presenter {
    Activity mActivity;
    AccountSettingActivityCon.View mView;

    public AccountSettingActivityPre(Activity activity, AccountSettingActivityCon.View view) {
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
    public void logout() {
        /**
         * 参数1：true代表解除绑定，不再推送消息
         */
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLogout(EMClient.getInstance().getCurrentUser(),true,null);
                    }
                });
            }

            @Override
            public void onError(int code, final String error) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLogout(EMClient.getInstance().getCurrentUser(),false,error);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
