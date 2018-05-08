package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface CreateGroupActivityCon {
    interface View extends BaseView<CreateGroupActivityCon.Presenter> {

    }

    interface Presenter extends BasePre {

        void createGroup(String groupName);
    }
}
