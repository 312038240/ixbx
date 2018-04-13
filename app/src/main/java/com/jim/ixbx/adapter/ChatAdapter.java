package com.jim.ixbx.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.jim.ixbx.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Jim.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context mContext;
    List<EMMessage> mList;

    public ChatAdapter(Context context, List<EMMessage> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatactivity_item, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatactivity_item_send, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMMessage emMessage = mList.get(position);
        long msgTime = emMessage.getMsgTime();
        //需要将消息body转换为EMTextMessageBody
        EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        String message = body.getMessage();
        holder.mTvMsg.setText(message);

        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position == 0) {
            holder.mTvTime.setVisibility(View.VISIBLE);
        } else {
            EMMessage preMessage = mList.get(position - 1);
            long preMsgTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime, preMsgTime)) {
                holder.mTvTime.setVisibility(View.GONE);
            } else {
                holder.mTvTime.setVisibility(View.VISIBLE);
            }
        }
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            switch (emMessage.status()) {
                case INPROGRESS://发送中
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.drawable.msg_state_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.mIvState.getDrawable();
                    if (drawable.isRunning()) {
                        drawable.stop();
                    }
                    drawable.start();
                    break;
                case SUCCESS://发送完成
                    holder.mIvState.setVisibility(View.GONE);
                    break;
                case FAIL://发送失败
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.mipmap.msg_error);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mList.get(position);
        return emMessage.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;
        TextView mTvMsg;
        ImageView mIvState;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvState = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
