package com.pro.maluli.module.home.base.fragment.child.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IHomeChildContraction extends BaseView {
    interface View extends BaseView {
        void setCommission(String commission);
    }

    interface Presenter extends IClear {
        void getUserInfo();

    }
}
