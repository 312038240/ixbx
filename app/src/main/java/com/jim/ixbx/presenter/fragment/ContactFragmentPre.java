package com.jim.ixbx.presenter.fragment;

import android.app.Activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jim.ixbx.model.UserContactInfo;
import com.jim.ixbx.presenter.Contract.ContactFragmentCon;
import com.jim.ixbx.utils.ThreadUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jim.
 */

public class ContactFragmentPre implements ContactFragmentCon.Presenter {
    private static final String TAG = "Jim_ContactFragmentPre";
    Activity mActivity;
    ContactFragmentCon.View mView;
    private List<String> contactList = new ArrayList<>();
    public ContactFragmentPre(Activity activity, ContactFragmentCon.View view) {
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
    public void initContacts() {
        /**
         * 1. 首先访问本地的缓存联系人
         * 2. 然后开辟子线程去环信后台获取当前用户的联系人
         * 3. 更新本地的缓存，刷新UI
         */
        final  String currentUser = EMClient.getInstance().getCurrentUser();
        List<UserContactInfo> userContactInfos = DataSupport.where("username=?", currentUser).find(UserContactInfo.class);
        contactList.clear();
        for (int i = 0; i < userContactInfos.size(); i++) {
            contactList.add(userContactInfos.get(i).getContact());
        }
        mView.onInitContacts(contactList);

        updateContactsFromServer(currentUser);
    }

    @Override
    public void updateContacts() {
        updateContactsFromServer(EMClient.getInstance().getCurrentUser());
    }

    @Override
    public void deleteContact(final String contact) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    afterDelete(contact, true,null);
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    afterDelete(contact,false,e.toString());
                }

            }
        });
    }
    private void afterDelete(final String contact, final boolean success, final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mView.onDelete(contact, success, msg);
            }
        });
    }
    private void updateContactsFromServer(final String currentUser) {
        //子线程去环信后台获取当前用户的联系人
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //排序
                    Collections.sort(contactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //更新本地的缓存，
                    DataSupport.deleteAll(UserContactInfo.class);
                    if (contactsFromServer.size()>0){
                        for (int i = 0; i < contactsFromServer.size(); i++) {
                            UserContactInfo info=new UserContactInfo();
                            info.setContact(contactsFromServer.get(i));
                            info.setUsername(currentUser);
                            info.save();
                        }
                    }
                    contactList.clear();
                    contactList.addAll(contactsFromServer);
                    //通知View刷新UI
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateContacts(true,null);
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateContacts(false,e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
