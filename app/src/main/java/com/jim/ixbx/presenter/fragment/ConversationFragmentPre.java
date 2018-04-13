package com.jim.ixbx.presenter.fragment;

import android.app.Activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jim.ixbx.presenter.Contract.ConversationFragmentCon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jim.
 */

public class ConversationFragmentPre implements ConversationFragmentCon.Presenter {
    Activity mActivity;
    ConversationFragmentCon.View mView;
    private List<EMConversation> mEMConversationList = new ArrayList<>();
    public ConversationFragmentPre(Activity activity, ConversationFragmentCon.View view) {
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
    public void initConversation() {
        //获取所有会话
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        mEMConversationList.clear();
        mEMConversationList.addAll(allConversations.values());
        /**
         * 排序，最近的时间在最上面(时间的倒序)
         * 回传到View层
         */
        Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {

                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        mView.onInitConversation(mEMConversationList);
    }
}
