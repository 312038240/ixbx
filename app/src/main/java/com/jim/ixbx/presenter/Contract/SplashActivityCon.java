package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface SplashActivityCon {
    interface View extends BaseView<SplashActivityCon.Presenter> {

        void onCheckedLogin(boolean b);
    }

    interface Presenter extends BasePre {

        void checkLogined();
    }
}
