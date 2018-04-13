package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.jim.ixbx.presenter.Contract.ChatActivityCon;
import com.jim.ixbx.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jim.
 */

public class ChatActivityPre implements ChatActivityCon.Presenter {
    Activity mActivity;
    ChatActivityCon.View mView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    public ChatActivityPre(Activity activity, ChatActivityCon.View view) {
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
    public void initChat(String username) {
        /**
         * 1. 如果曾经跟username有聊天过，那么获取最多最近的20条聊天记录，然后展示到View层
         * 2. 如果没有聊天过，返回一个空的List
         */
        updateChatData(username);
        mView.onInit(mEMMessageList);
    }

    @Override
    public void updateData(String username) {
        updateChatData(username);
        mView.onUpdate(mEMMessageList.size());
    }

    @Override
    public void sendMessage(String username, String msg) {
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg,username);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);
        mView.onUpdate(mEMMessageList.size());
        emMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onUpdate(mEMMessageList.size());
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onUpdate(mEMMessageList.size());
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });

        EMClient.getInstance().chatManager().sendMessage(emMessage);

    }

    private void updateChatData(String contact) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contact);
        if (conversation != null) {
            //需要将所有的未读消息标记为已读
            conversation.markAllMessagesAsRead();

            //获取最多最近的20条聊天记录，然后展示到View层
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最后一条消息之前的19条（最多）
            int count = 19;
            if (mEMMessageList.size()>=19){
                count = mEMMessageList.size();
            }
            List<EMMessage> messageList = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            Collections.reverse(messageList);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(messageList);
            Collections.reverse(mEMMessageList);
        } else {
            mEMMessageList.clear();
        }
    }
}
