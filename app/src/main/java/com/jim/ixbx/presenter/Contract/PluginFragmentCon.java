package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface PluginFragmentCon {
    interface View extends BaseView<PluginFragmentCon.Presenter> {

        void onLogout(String currentUser, boolean b, String error);
    }

    interface Presenter extends BasePre {

        void logout();

    }
}
