package com.jim.ixbx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.jim.ixbx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim.
 */

public class GroupChatAdapter extends BaseAdapter {
    Context mContext;
    List<EMGroup> mList=new ArrayList<>();

    public GroupChatAdapter(Context context, List<EMGroup> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_group_chat,parent,false);
            holder=new ViewHolder();
            holder.mTextView=convertView.findViewById(R.id.textview);
            holder.mImageView=convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        EMGroup emGroup = mList.get(position);
        holder.mTextView.setText(emGroup.getGroupName());
        holder.mImageView.setImageResource(R.mipmap.avatar3);
        return convertView;
    }
    public static class ViewHolder{
        ImageView mImageView;
        TextView mTextView;

    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
