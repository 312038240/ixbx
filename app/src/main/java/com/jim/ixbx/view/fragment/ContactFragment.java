package com.jim.ixbx.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jim.ixbx.R;
import com.jim.ixbx.adapter.ContactAdapter;
import com.jim.ixbx.event.OnContactUpdateEvent;
import com.jim.ixbx.presenter.Contract.ContactFragmentCon;
import com.jim.ixbx.presenter.fragment.ContactFragmentPre;
import com.jim.ixbx.view.activity.ChatActivity;
import com.jim.ixbx.view.activity.MainActivity;
import com.jim.ixbx.view.base.BaseFragment;
import com.jim.ixbx.view.views.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jim. 联系人界面
 */

public class ContactFragment extends BaseFragment implements ContactFragmentCon.View, ContactAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "Jim_ContactFragment";
    ContactFragmentCon.Presenter mPresenter;
    @InjectView(R.id.contactLayout)
    ContactLayout mContactLayout;
    ContactAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        new ContactFragmentPre(getActivity(), this);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化联系人
        mPresenter.initContacts();
        mContactLayout.setOnRefreshListener(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnContactUpdateEvent onContactUpdateEvent) {
        mPresenter.updateContacts();
    }

    @Override
    public void setPresenter(ContactFragmentCon.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 初始化联系人
     *
     * @param list
     */
    @Override
    public void onInitContacts(List<String> list) {
        mAdapter = new ContactAdapter(getActivity(), list);
        mContactLayout.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 更新联系人
     *
     * @param b
     * @param message
     */
    @Override
    public void updateContacts(boolean b, String message) {
        if (b) {
            mAdapter.notifyDataSetChanged();
            //隐藏下拉刷新
            if (mContactLayout!=null){
                mContactLayout.setRefreshing(false);
            }

        } else {
            Snackbar.make(mContactLayout, "更新好友列表失败" + message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelete(String contact, boolean success, String msg) {
        if (success) {
            Toast.makeText(getActivity(), "友尽", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "删除失败,还是在一起吧", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemLongClick(final String contact, int position) {
        Snackbar.make(mContactLayout, "您和" + contact + "确定友尽了吗？", Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.deleteContact(contact);
                    }
                }).show();
    }

    @Override
    public void onItemClick(String contact, int position) {
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(ChatActivity.class, false, contact);
    }

    @Override
    public void onRefresh() {
        /**
         * 1. 访问网络，获取联系人
         * 2. 如果拿到数据了，更新数据库
         * 3. 更新UI
         * 4. 隐藏下拉刷新
         */
        mPresenter.updateContacts();
    }
}
