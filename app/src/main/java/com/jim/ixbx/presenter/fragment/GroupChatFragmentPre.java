package com.jim.ixbx.presenter.fragment;

import android.app.Activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.jim.ixbx.presenter.Contract.GroupChatFragmentCon;
import com.jim.ixbx.utils.ThreadUtils;

import java.util.List;

/**
 * Created by Jim.
 */

public class GroupChatFragmentPre implements GroupChatFragmentCon.Presenter {
    private static final String TAG = "GroupChatFragmentPre";
    Activity mActivity;
    GroupChatFragmentCon.View mView;

    public GroupChatFragmentPre(Activity activity, GroupChatFragmentCon.View view) {
        mActivity = activity;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getAllGroup() {
        //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    mView.initGroupData(grouplist);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
