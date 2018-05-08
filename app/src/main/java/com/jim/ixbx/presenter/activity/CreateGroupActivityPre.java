package com.jim.ixbx.presenter.activity;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.jim.ixbx.presenter.Contract.CreateGroupActivityCon;
import com.jim.ixbx.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim.
 */

public class CreateGroupActivityPre implements CreateGroupActivityCon.Presenter {
    private static final String TAG = "CreateGroupActivityPre";
    Activity mActivity;
    CreateGroupActivityCon.View mView;
    private List<String> contactList = new ArrayList<>();

    public CreateGroupActivityPre(Activity activity, CreateGroupActivityCon.View view) {
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
    public void createGroup(final String groupName) {
        /**
         * 创建群组
         * @param groupName 群组名称
         * @param desc 群组简介
         * @param allMembers 群组初始成员，如果只有自己传空数组即可
         * @param reason 邀请成员加入的reason
         * @param option 群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupStyle}
         *               option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
         *               option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
         * @return 创建好的group
         * @throws HyphenateException
         */


        //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                    option.maxUsers = 200;
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, "", new String[]{}, "", option);
                    if (group!=null){
                        Log.e(TAG, "run: "+group.getGroupName()+"   "+group.getGroupId() );
                        Toast.makeText(mActivity, "创建群组成功", Toast.LENGTH_SHORT).show();
                        mActivity.finish();
                    }
                } catch (HyphenateException e) {
                    Log.e(TAG, "HyphenateException: " + e.getErrorCode() + "   " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });

    }
}
