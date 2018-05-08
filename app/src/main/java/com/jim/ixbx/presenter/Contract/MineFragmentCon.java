package com.jim.ixbx.presenter.Contract;

import com.jim.ixbx.presenter.base.BasePre;
import com.jim.ixbx.presenter.base.BaseView;

/**
 * Created by Jim.
 */

public interface MineFragmentCon {
    interface View extends BaseView<MineFragmentCon.Presenter> {
    }

    interface Presenter extends BasePre {
    }
}
