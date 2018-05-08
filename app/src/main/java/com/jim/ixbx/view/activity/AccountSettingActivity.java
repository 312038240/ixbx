package com.jim.ixbx.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.AccountSettingActivityCon;
import com.jim.ixbx.presenter.activity.AccountSettingActivityPre;
import com.jim.ixbx.view.base.BaseActivity;
import com.jim.ixbx.view.views.ArrowHeadView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountSettingActivity extends BaseActivity implements AccountSettingActivityCon.View {

    @InjectView(R.id.head_view)
    ArrowHeadView mHeadView;
    @InjectView(R.id.ac_set_change_pswd)
    RelativeLayout mAcSetChangePswd;
    @InjectView(R.id.ac_set_privacy)
    RelativeLayout mAcSetPrivacy;
    @InjectView(R.id.ac_set_new_message)
    RelativeLayout mAcSetNewMessage;
    @InjectView(R.id.ac_set_clean)
    RelativeLayout mAcSetClean;
    @InjectView(R.id.ac_set_exit)
    RelativeLayout mAcSetExit;
    AccountSettingActivityCon.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.inject(this);
        new AccountSettingActivityPre(this, this);
        initView();
    }

    private void initView() {
        mHeadView.setTitle(R.string.title_activity_account_setting);
        mHeadView.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.ac_set_change_pswd, R.id.ac_set_privacy, R.id.ac_set_new_message, R.id.ac_set_clean, R.id.ac_set_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_set_change_pswd:
                break;
            case R.id.ac_set_privacy:

                break;
            case R.id.ac_set_new_message:
                break;
            case R.id.ac_set_clean:
                break;
            case R.id.ac_set_exit:
                //退出登录
                showDialog("正在退出登录...");
                mPresenter.logout();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(AccountSettingActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onLogout(String currentUser, boolean b, String error) {
        hideDialog();
        if (b) {
            startActivity(LoginActivity.class, true);
        } else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
