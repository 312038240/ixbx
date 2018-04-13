package com.jim.ixbx.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.jim.ixbx.R;
import com.jim.ixbx.adapter.ChatAdapter;
import com.jim.ixbx.presenter.Contract.ChatActivityCon;
import com.jim.ixbx.presenter.activity.ChatActivityPre;
import com.jim.ixbx.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 聊天页面
 */
public class ChatActivity extends BaseActivity implements ChatActivityCon.View, TextWatcher, View.OnClickListener {
    ChatActivityCon.Presenter mPresenter;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.et_msg)
    EditText mEtMsg;
    @InjectView(R.id.btn_send)
    Button mBtnSend;
    private String mUsername;
    private ChatAdapter mChatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        new ChatActivityPre(this, this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        //聊天对象
        mUsername = intent.getStringExtra("username");
        if (TextUtils.isEmpty(mUsername)){
            Toast.makeText(this, "跟鬼聊啊，请携带username参数！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mTvTitle.setText("与"+ mUsername +"聊天中");

        mEtMsg.addTextChangedListener(this);
        String msg = mEtMsg.getText().toString();
        if (TextUtils.isEmpty(msg)){
            mBtnSend.setEnabled(false);
        }else {
            mBtnSend.setEnabled(true);
        }
        /**
         * 显示最多最近的20条聊天记录，然后定位RecyclerView到最后一行
         */
        mPresenter.initChat(mUsername);
        EventBus.getDefault().register(this);
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void setPresenter(ChatActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message){
        //当收到信消息的时候
        /*
         *  判断当前这个消息是不是正在聊天的用户给我发的
         *  如果是，让ChatPresenter 更新数据
         *
         */
        String from = message.getFrom();
        if (from.equals(mUsername)){
            mPresenter.updateData(mUsername);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length()==0){
            mBtnSend.setEnabled(false);
        }else{
            mBtnSend.setEnabled(true);
        }
    }

    @Override
    public void onInit(List<EMMessage> emMessageList) {
        /**
         * 初始化RecyclerView
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(this,emMessageList);
        mRecyclerView.setAdapter(mChatAdapter);
        if (emMessageList.size()!=0){
            mRecyclerView.scrollToPosition(emMessageList.size()-1);
        }
    }

    @Override
    public void onUpdate(int size) {
        mChatAdapter.notifyDataSetChanged();
        if (size!=0){
            mRecyclerView.smoothScrollToPosition(size-1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String msg = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(msg)){
                    Toast.makeText(this, "不能发空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.sendMessage(mUsername, msg);
                mEtMsg.getText().clear();
                break;
            default:
                break;
        }
    }
}
