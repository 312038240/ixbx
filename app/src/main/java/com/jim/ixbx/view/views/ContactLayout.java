package com.jim.ixbx.view.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.ixbx.R;

/**
 * Created by Jim.联系人界面
 */

public class ContactLayout extends RelativeLayout {
    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private SlideBar mSlideBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.contact_layout, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTextView = (TextView) findViewById(R.id.tv_float);
        mSlideBar = (SlideBar) findViewById(R.id.slideBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setRefreshing(boolean refreshing){
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

}
