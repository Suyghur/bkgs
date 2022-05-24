package com.pro.maluli.module.myself.myAccount.withdraw.search.searchReward.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.BKRecordEntity;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.entity.SearchEntity;

public interface ISearchRewardContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(RewardDetailEntity data);

        void setHistorySearch(SearchEntity data);

        void deleteSuccess();

    }
    interface Presenter extends IClear {

        void getBkDetail();

        void getSearchHistory();

        void deleteHistory();
    }
}
