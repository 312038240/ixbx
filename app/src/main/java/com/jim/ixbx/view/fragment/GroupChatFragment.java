package com.jim.ixbx.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.jim.ixbx.R;
import com.jim.ixbx.adapter.GroupChatAdapter;
import com.jim.ixbx.presenter.Contract.GroupChatFragmentCon;
import com.jim.ixbx.presenter.fragment.GroupChatFragmentPre;
import com.jim.ixbx.utils.ThreadUtils;
import com.jim.ixbx.view.base.BaseFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jim.
 */

public class GroupChatFragment extends BaseFragment implements GroupChatFragmentCon.View {
    private static final String TAG = "GroupChatFragment";
    GroupChatFragmentCon.Presenter mPresenter;
    @InjectView(R.id.show_no_group)
    TextView mShowNoGroup;
    @InjectView(R.id.foot_group_size)
    TextView mFootGroupSize;
    @InjectView(R.id.contact_list)
    ListView mContactList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupchat, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GroupChatFragmentPre(getActivity(), this);
        initView();
    }

    private void initView() {
        mPresenter.getAllGroup();

    }

    @Override
    public void setPresenter(GroupChatFragmentCon.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initGroupData(final List<EMGroup> grouplist) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (grouplist==null&&grouplist.size()<=0){
                    mShowNoGroup.setVisibility(View.VISIBLE);
                    mFootGroupSize.setVisibility(View.GONE);
                    mContactList.setVisibility(View.GONE);
                }else {
                    mShowNoGroup.setVisibility(View.GONE);
                    mFootGroupSize.setVisibility(View.VISIBLE);
                    mContactList.setVisibility(View.VISIBLE);
                    GroupChatAdapter adapter=new GroupChatAdapter(getContext(),grouplist);
                    mContactList.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
