package com.jim.ixbx.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jim.ixbx.R;
import com.jim.ixbx.view.activity.CreateGroupActivity;
import com.jim.ixbx.view.activity.NewFriendActivity;
import com.jim.ixbx.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Jim.
 */

public class ConFragment extends BaseFragment {
    private static final String TAG = "ConFragment";
    @InjectView(R.id.con_new_friend)
    RelativeLayout mConNewFriend;
    @InjectView(R.id.con_create_group)
    RelativeLayout mConCreateGroup;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    List<BaseFragment> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_con, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.add(new ContactFragment());
        mList.add(new GroupChatFragment());
        mList.add(new ChatRoomFragment());
        mViewPager.setAdapter(new ConAdapter(getChildFragmentManager(), mList));
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.con_new_friend, R.id.con_create_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // TODO: 2018/5/7
            case R.id.con_new_friend:
                startActivity(new Intent(getActivity(),NewFriendActivity.class));
                break;
            case R.id.con_create_group:
                startActivity(new Intent(getActivity(),CreateGroupActivity.class));
                break;
            default:
                break;
        }
    }


    class ConAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> mFragments;
        private final String[] titles = {"联系人", "群聊", "聊天室"};

        public ConAdapter(FragmentManager fm, List<BaseFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
