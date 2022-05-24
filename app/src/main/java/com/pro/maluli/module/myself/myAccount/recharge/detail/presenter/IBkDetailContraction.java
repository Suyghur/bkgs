package com.pro.maluli.module.myself.myAccount.recharge.detail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.BKRecordEntity;

public interface IBkDetailContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(BKRecordEntity data);
    }
    interface Presenter extends IClear {

        void getBkDetail();
    }
}
