package com.jim.ixbx.presenter.Contract;

import com.hyphenate.chat.EMConversation;
import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

import java.util.List;

/**
 * Created by Jim.
 */

public interface ConversationFragmentCon {
    interface View extends BaseView<ConversationFragmentCon.Presenter> {

        void onInitConversation(List<EMConversation> list);
    }

    interface Presenter extends BasePre {

        void initConversation();
    }
}
