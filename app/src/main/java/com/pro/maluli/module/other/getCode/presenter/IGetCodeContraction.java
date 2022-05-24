package com.pro.maluli.module.other.getCode.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IGetCodeContraction extends BaseView {
    interface View extends BaseView {
        void getCodeSuccess();
    }
    interface Presenter extends IClear {
        void getVerifiCationCode(String mobile,String codetype);

    }
}
