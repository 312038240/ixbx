package com.jim.ixbx.presenter.Contract;

import com.hyphenate.chat.EMMessage;
import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

import java.util.List;

/**
 * Created by Jim.
 */

public interface ChatActivityCon {
    interface View extends BaseView<ChatActivityCon.Presenter> {

        void onInit(List<EMMessage> emMessageList);

        void onUpdate(int size);
    }

    interface Presenter extends BasePre {

        void initChat(String username);

        void updateData(String username);

        void sendMessage(String username, String msg);
    }
}
