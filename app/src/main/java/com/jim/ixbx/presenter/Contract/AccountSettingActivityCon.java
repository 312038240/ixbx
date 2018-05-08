package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface AccountSettingActivityCon {
    interface View extends BaseView<AccountSettingActivityCon.Presenter> {

          void onLogout(String currentUser, boolean b, String error);
    }

    interface Presenter extends BasePre {

        void logout();
    }
}
