package com.pro.maluli.module.myself.myAccount.withdraw.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.MyAccountEntity;

public interface IWithdrawContraction extends BaseView {
    interface View extends BaseView {
        void setAccountInfo(MyAccountEntity data);

        void getCodeSuccess();
    }
    interface Presenter extends IClear {

        void getMyAccount();

        void getVerifiCationCode(String mobile, String codetype);

        void subWithdraw();

    }
}
