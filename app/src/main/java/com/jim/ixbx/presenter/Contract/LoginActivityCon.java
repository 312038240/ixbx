package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface LoginActivityCon {
    interface View extends BaseView<LoginActivityCon.Presenter> {

        void onLogin(String username, String pwd, boolean b, String msg);
    }

    interface Presenter extends BasePre {

        void login(String username, String pwd);
    }
}
