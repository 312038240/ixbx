package com.jim.ixbx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.ixbx.R;
import com.jim.ixbx.utils.StringUtils;

import java.util.List;

/**
 * Created by Jim.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements IContactAdapter{
    private static final String TAG = "Jim_ContactAdapter";
    Context mContext;
    List<String> mList;
    public ContactAdapter(Context context, List<String> list) {
        mContext=context;
        mList=list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String s = mList.get(position);
        holder.mTvUsername.setText(s);
        String initial = StringUtils.getInitial(s);
        holder.mTvSection.setText(initial);
        if (position==0){
            holder.mTvSection.setVisibility(View.VISIBLE);
        }else {
            //获取上一个首字母
            String preContact = mList.get(position - 1);
            String preInitial = StringUtils.getInitial(preContact);
            if (preInitial.equals(initial)){
                holder.mTvSection.setVisibility(View.GONE);
            }else {
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener !=null){
                    mOnItemClickListener.onItemLongClick(s,position);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(s,position);
                }
            }
        });

    }
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemLongClick(String contact,int position);
        void onItemClick(String contact,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    @Override
    public List<String> getData() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTvSection;
        TextView mTvUsername;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvSection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }
}
