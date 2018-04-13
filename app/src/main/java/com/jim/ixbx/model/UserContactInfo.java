package com.jim.ixbx.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Jim.
 */

public class UserContactInfo extends DataSupport {
    String username;
    String contact;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
