package com.pro.maluli.module.myself.myAccount.withdraw.search.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.BKRecordEntity;
import com.pro.maluli.common.entity.SearchEntity;

public interface ISearchBkDetailContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(BKRecordEntity data);

        void setHistorySearch(SearchEntity data);

        void deleteSuccess();

    }
    interface Presenter extends IClear {

        void getBkDetail();

        void getSearchHistory();

        void deleteHistory();
    }
}
