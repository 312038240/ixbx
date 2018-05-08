package com.jim.ixbx.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jim.ixbx.R;
import com.jim.ixbx.adapter.ContactAdapter;
import com.jim.ixbx.presenter.Contract.CreateGroupActivityCon;
import com.jim.ixbx.presenter.activity.CreateGroupActivityPre;
import com.jim.ixbx.view.views.ArrowHeadView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupActivityCon.View {
    private static final String TAG = "CreateGroupActivity";
    @InjectView(R.id.head_view)
    ArrowHeadView mHeadView;
    CreateGroupActivityCon.Presenter mPresenter;
    ContactAdapter mAdapter;
    @InjectView(R.id.edittext)
    TextInputEditText mEdittext;
    @InjectView(R.id.image)
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.inject(this);
        new CreateGroupActivityPre(this, this);
        initView();
    }

    private void initView() {
        mHeadView.setTitle(R.string.title_activity_create_group);
        mHeadView.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHeadView.setRightButtonText(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = mEdittext.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(CreateGroupActivity.this, "群名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.createGroup(groupName);
            }
        });
    }

    @Override
    public void setPresenter(CreateGroupActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }

}
