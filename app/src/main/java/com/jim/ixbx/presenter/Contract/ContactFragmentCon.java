package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

import java.util.List;

/**
 * Created by Jim.
 */

public interface ContactFragmentCon {
    interface View extends BaseView<ContactFragmentCon.Presenter> {

        void onInitContacts(List<String> list);

        void updateContacts(boolean b, String message);

        void onDelete(String contact, boolean success, String msg);
    }

    interface Presenter extends BasePre {

        void initContacts();

        void updateContacts();

        void deleteContact(String contact);
    }
}
