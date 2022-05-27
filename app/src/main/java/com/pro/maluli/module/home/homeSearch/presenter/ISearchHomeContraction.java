package com.pro.maluli.module.home.homeSearch.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.SearchEntity;

public interface ISearchHomeContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(LiveListEntity data);

        void setHistorySearch(SearchEntity data);

        void deleteSuccess();

    }

    interface Presenter extends IClear {

        void getBkDetail();

        void getSearchHistory();

        void deleteHistory();
    }
}
