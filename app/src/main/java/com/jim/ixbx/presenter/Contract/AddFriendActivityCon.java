package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.model.UserBean;
import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

import java.util.List;

/**
 * Created by Jim.
 */

public interface AddFriendActivityCon {
    interface View extends BaseView<AddFriendActivityCon.Presenter> {

        void onSearchResult(List<UserBean> list, List<String> contacts, boolean b, Object o);

        void onAddResult(String username, boolean b, String msg);
    }

    interface Presenter extends BasePre {

        void searchFriend(String query);

        void addFriend(String username);
    }
}
