package com.jim.ixbx.presenter.activity;

import android.app.Activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jim.ixbx.model.UserBean;
import com.jim.ixbx.model.UserContactInfo;
import com.jim.ixbx.presenter.Contract.AddFriendActivityCon;
import com.jim.ixbx.utils.ThreadUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jim.
 */

public class AddFriendActivityPre implements AddFriendActivityCon.Presenter {
    private static final String TAG = "jim_AddFriend";
    Activity mActivity;
    AddFriendActivityCon.View mView;

    public AddFriendActivityPre(Activity activity, AddFriendActivityCon.View view) {
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
    public void searchFriend(final String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        BmobQuery<UserBean> query = new BmobQuery<UserBean>();
        //查询username字段的值是以keyword字开头的数据
        query.addWhereStartsWith("username", keyword)
                //名字不等于currentuser
                .addWhereNotEqualTo("username", currentUser)
                .findObjects(new FindListener<UserBean>() {
                    @Override
                    public void done(List<UserBean> list, BmobException e) {
                        //bmob联系人区分大小写,环信没有区分
                        if (list.size()>0){
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getUsername().equalsIgnoreCase(currentUser)){
                                    list.remove(i);
                                }
                            }
                        }

                        if (e == null && list != null && list.size() > 0) {
                            List<String> infos = new ArrayList<>();

                            List<UserContactInfo> all = DataSupport.findAll(UserContactInfo.class);
                            if (all.size()>0){
                                for (int i = 0; i < all.size(); i++) {
                                    infos.add(all.get(i).getContact());
                                }
                            }
                            //获取到数据
                            mView.onSearchResult(list, infos, true, null);
                        } else {
                            //没有找到数据
                            if (e == null) {
                                mView.onSearchResult(null, null, false, "没有找到对应的用户。");
                            } else {
                                mView.onSearchResult(null, null, false, e.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void addFriend(String username) {
        try {
            EMClient.getInstance().contactManager().addContact(username, "约么?");
            //添加成功（仅仅代表这个请求发送成功了，对方有没有同意是另外一会儿事儿）
            onAddResult(username,true,null);
        } catch (HyphenateException e) {
            //添加失败
            onAddResult(username, false,null);
            e.printStackTrace();
        }
    }

    private void onAddResult(final String username, final boolean b, final String s) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mView.onAddResult(username, b, s);
            }
        });
    }
}
