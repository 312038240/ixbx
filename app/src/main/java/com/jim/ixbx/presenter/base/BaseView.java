package com.jim.ixbx.presenter.base;

/** MVP中的基本V层
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
