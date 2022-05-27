package com.pro.maluli.module.other.welcome.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.WelcomInfoEntity;

public interface IWelcomContraction extends BaseView {
    interface View extends BaseView {
        void setWelcomInfo(WelcomInfoEntity welcomInfo);
    }

    interface Presenter extends IClear {
        void getWelcomInfo();

    }
}
