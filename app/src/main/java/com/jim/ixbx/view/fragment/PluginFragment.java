package com.jim.ixbx.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.PluginFragmentCon;
import com.jim.ixbx.presenter.fragment.PluginFragmentPre;
import com.jim.ixbx.view.activity.LoginActivity;
import com.jim.ixbx.view.activity.MainActivity;
import com.jim.ixbx.view.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jim. 动态界面
 */

public class PluginFragment extends BaseFragment implements PluginFragmentCon.View, View.OnClickListener {
    @InjectView(R.id.btn_logout)
    Button mBtnLogout;
    private ProgressDialog mProgressDialog;
    PluginFragmentCon.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        new PluginFragmentPre(getActivity(), this);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String currentUser = EMClient.getInstance().getCurrentUser();
        mBtnLogout.setText(currentUser);
        mBtnLogout.setOnClickListener(this);
    }

    @Override
    public void setPresenter(PluginFragmentCon.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        mProgressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                //退出登录
                mProgressDialog.setMessage("正在退出...");
                mProgressDialog.show();
                mPresenter.logout();
            break;
            default:

                break;
        }
    }

    @Override
    public void onLogout(String currentUser, boolean b, String error) {
        mProgressDialog.hide();
        if (b){
            MainActivity activity = (MainActivity)getActivity();
            activity.startActivity(LoginActivity.class,true);
        }else{
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        }
    }
}
