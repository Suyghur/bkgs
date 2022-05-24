package com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.entity.WithDrawRecordEntity;

public interface IWithDrawRecordContraction extends BaseView {
    interface View extends BaseView {

        void setWithDrawRecordList(WithDrawRecordEntity data);
    }
    interface Presenter extends IClear {


        void getSearchHistory();

    }
}
