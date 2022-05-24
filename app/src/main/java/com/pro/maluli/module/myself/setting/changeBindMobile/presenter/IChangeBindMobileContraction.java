package com.pro.maluli.module.myself.setting.changeBindMobile.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IChangeBindMobileContraction extends BaseView {
    interface View extends BaseView {
        void getCodeSuccess();
        void setPwdSuccess(String msg);
    }
    interface Presenter extends IClear {
        void getVerifiCationCode(String mobile,String codeType);

        void changeBindmobile(String mobile, String code, String s);
    }
}
