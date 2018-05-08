package com.jim.ixbx.presenter.Contract;

import com.hyphenate.chat.EMGroup;
import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

import java.util.List;

/**
 * Created by Jim.
 */

public interface GroupChatFragmentCon {
    interface View extends BaseView<GroupChatFragmentCon.Presenter> {

        void initGroupData(List<EMGroup> grouplist);
    }

    interface Presenter extends BasePre {

        void getAllGroup();
    }
}
