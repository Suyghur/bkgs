package com.pro.maluli.module.myself.base.presenter;

import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.UserInfoEntity;

public interface IMyselfContraction extends BaseView {
    interface View extends BaseView {

        void setUserInfoSuccess(UserInfoEntity response);
    }
    interface Presenter extends IClear {
        void getUserInfo();

    }
}
