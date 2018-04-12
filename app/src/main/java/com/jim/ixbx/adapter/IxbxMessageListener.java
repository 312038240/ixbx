package com.jim.ixbx.adapter;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Jim.
 */

public class IxbxMessageListener implements EMMessageListener {

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //收到透传消息
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        //收到已读回执
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        //收到已送达回执
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        //消息状态变动
    }

}
