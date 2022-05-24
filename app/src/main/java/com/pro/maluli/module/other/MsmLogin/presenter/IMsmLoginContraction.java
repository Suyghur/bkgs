package com.pro.maluli.module.other.MsmLogin.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IMsmLoginContraction extends BaseView {
    interface View extends BaseView {
        void getCodeSuccess();

        void gotoBindMobile(String openid, String type);

        void loginSuccess(String msg);
    }
    interface Presenter extends IClear {
        void getVerifiCationCode(String mobile);
        void login(String mobile, String type, String pwd,String openid);
    }
}
