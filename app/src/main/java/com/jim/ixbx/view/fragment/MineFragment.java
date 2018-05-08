package com.jim.ixbx.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.MineFragmentCon;
import com.jim.ixbx.view.activity.AccountSettingActivity;
import com.jim.ixbx.presenter.fragment.MineFragmentPre;
import com.jim.ixbx.view.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Jim.
 */

public class MineFragment extends BaseFragment implements MineFragmentCon.View {
    MineFragmentCon.Presenter mPresenter;
    @InjectView(R.id.mine_name)
    TextView mMineName;
    @InjectView(R.id.start_user_profile)
    LinearLayout mStartUserProfile;
    @InjectView(R.id.mine_setting)
    LinearLayout mMineSetting;
    @InjectView(R.id.my_wallet)
    LinearLayout mMyWallet;
    @InjectView(R.id.mine_service)
    LinearLayout mMineService;
    @InjectView(R.id.new_version_icon)
    ImageView mNewVersionIcon;
    @InjectView(R.id.mine_about)
    LinearLayout mMineAbout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new MineFragmentPre(getActivity(), this);
    }

    @Override
    public void setPresenter(MineFragmentCon.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.start_user_profile, R.id.mine_setting, R.id.my_wallet, R.id.mine_service, R.id.mine_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_user_profile://头像
                break;
            case R.id.mine_setting://账号设置
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                break;
            case R.id.my_wallet://钱包
                break;
            case R.id.mine_service://意见反馈
                break;
            case R.id.mine_about://关于
                break;
        }
    }
}
