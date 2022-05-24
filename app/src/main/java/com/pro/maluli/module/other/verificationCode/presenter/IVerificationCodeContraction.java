package com.pro.maluli.module.other.verificationCode.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IVerificationCodeContraction extends BaseView {
    interface View extends BaseView {

        void loginSuccess();

        void setGoSetPassWord(String mobile, String code);

        void getCodeSuccess();

    }
    interface Presenter extends IClear {
        void checkCode(String mobile, String codeType, String input);
        void getVerifiCationCode(String mobile);
        void codeLogin(String mobile, String s, String input);

        void bindMobile(String mobile, String openid, String type, String input);

        void register(String mobile, String inputCode);
    }
}
