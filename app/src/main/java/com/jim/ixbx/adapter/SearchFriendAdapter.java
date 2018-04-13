package com.jim.ixbx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jim.ixbx.R;
import com.jim.ixbx.model.UserBean;

import java.util.List;

/**
 * Created by Jim.
 */

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.ViewHolder> {
    Context mContext;
    List<UserBean> mList;
    List<String> mContactInfos;
    public SearchFriendAdapter(Context context, List<UserBean> list, List<String> contacts) {
        mContext=context;
        mList=list;
        mContactInfos=contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.searchfriend_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserBean user = mList.get(position);
        final String username = user.getUsername();
        holder.mTvUsername.setText(username);
        holder.mTvTime.setText(user.getCreatedAt());
        //判断当前username是不是已经在好友列表中了
        if (mContactInfos!=null&&mContactInfos.contains(username)){
            holder.mBtnAdd.setText("已经是好友");
            holder.mBtnAdd.setEnabled(false);
        }else{
            holder.mBtnAdd.setText("添加");
            holder.mBtnAdd.setEnabled(true);
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddFriendClickListener!=null){
                    mOnAddFriendClickListener.onAddClick(username);
                }
            }
        });

    }
    public interface OnAddFriendClickListener{
        void onAddClick(String username);
    }
    private OnAddFriendClickListener mOnAddFriendClickListener;
    public void setOnAddFriendClickListener(OnAddFriendClickListener addFriendClickListener){
        this.mOnAddFriendClickListener = addFriendClickListener;
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTvUsername;
        TextView mTvTime;
        Button mBtnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
