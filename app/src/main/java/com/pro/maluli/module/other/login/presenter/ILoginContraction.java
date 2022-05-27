package com.pro.maluli.module.other.login.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface ILoginContraction extends BaseView {
    interface View extends BaseView {

        void loginSuccess(String msg);

        void gotoBindMobile(String s, String openid);
    }

    interface Presenter extends IClear {
        void getWelcomInfo();

        void login(String mobile, String type, String pwd, String openid);


    }
}
