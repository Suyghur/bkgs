package com.pro.maluli.module.other.findPassword.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IFindPasswordContraction extends BaseView {
    interface View extends BaseView {
        void setCommission(String commission);

        void setPwdSuccess(String msg, int is_first);
    }

    interface Presenter extends IClear {

        void subMitPwd(String mobile, String code, String pwd);
    }
}
