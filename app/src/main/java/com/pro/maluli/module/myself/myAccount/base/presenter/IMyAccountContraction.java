package com.pro.maluli.module.myself.myAccount.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.entity.UserInfoEntity;

public interface IMyAccountContraction extends BaseView {
    interface View extends BaseView {
        void setUserInfoSuccess(UserInfoEntity data);

        void setAccountInfo(MyAccountEntity data);
    }
    interface Presenter extends IClear {
        void getUserInfo();

        void getMyAccount();

        void getCashOutInfo();
    }
}
