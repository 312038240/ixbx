package com.jim.ixbx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.jim.ixbx.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Jim.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    Context mContext;
    List<EMConversation> mList;
    onItemClickListener mListener;

    public ConversationAdapter(Context context, List<EMConversation> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.conversation_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EMConversation conversation = mList.get(position);
        //聊天的对方的名称
        List<EMMessage> allMessages = conversation.getAllMessages();
        //最后消息
        EMMessage lastMessage = conversation.getLastMessage();
        //聊天的对方的名称
        String userName = lastMessage.getUserName();
        //未读消息条数
        int unreadMsgCount = conversation.getUnreadMsgCount();
        //最后消息时间
        long msgTime = lastMessage.getMsgTime();
        EMTextMessageBody lastBody= (EMTextMessageBody) lastMessage.getBody();
        //最后消息内容
        String message = lastBody.getMessage();
        holder.mTvUsername.setText(userName);
        holder.mTvMsg.setText(message);
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (unreadMsgCount>99){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText("99+");
        }else if (unreadMsgCount>0){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText(unreadMsgCount+"");
        }else{
            holder.mTvUnread.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onItemClick(conversation);
                }
            }
        });
    }
    public interface onItemClickListener{
        void onItemClick(EMConversation conversation);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        mListener=listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTvUsername;
        TextView mTvTime;
        TextView mTvMsg;
        TextView mTvUnread;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvUsername = itemView.findViewById(R.id.tv_username);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvMsg =  itemView.findViewById(R.id.tv_msg);
            mTvUnread = itemView.findViewById(R.id.tv_unread);
        }
    }
    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
}
