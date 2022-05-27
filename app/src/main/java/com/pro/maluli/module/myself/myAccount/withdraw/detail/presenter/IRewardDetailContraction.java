package com.pro.maluli.module.myself.myAccount.withdraw.detail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.RewardDetailEntity;

public interface IRewardDetailContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(RewardDetailEntity data);
    }

    interface Presenter extends IClear {

        void getBkDetail();
    }
}
