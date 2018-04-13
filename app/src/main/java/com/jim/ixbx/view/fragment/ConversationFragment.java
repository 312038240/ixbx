package com.jim.ixbx.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.jim.ixbx.R;
import com.jim.ixbx.adapter.ConversationAdapter;
import com.jim.ixbx.presenter.Contract.ConversationFragmentCon;
import com.jim.ixbx.presenter.fragment.ConversationFragmentPre;
import com.jim.ixbx.view.activity.ChatActivity;
import com.jim.ixbx.view.activity.MainActivity;
import com.jim.ixbx.view.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Jim. 消息界面
 */

public class ConversationFragment extends BaseFragment implements ConversationFragmentCon.View, ConversationAdapter.onItemClickListener {
    private static final String TAG = "Jim_Conver";
    ConversationFragmentCon.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    ConversationAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        new ConversationFragmentPre(this.getActivity(),this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //初始化会话列表
        mPresenter.initConversation();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mAdapter = null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message){
        mPresenter.initConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void setPresenter(ConversationFragmentCon.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void onInitConversation(List<EMConversation> list) {
        if (mAdapter==null){
            mAdapter = new ConversationAdapter(getContext(),list);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(EMConversation conversation) {
        String userName = conversation.getLastMessage().getUserName();
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(ChatActivity.class,false,userName);
    }
}
