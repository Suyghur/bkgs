package com.pro.maluli.module.myself.watchlist.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.WatchListEntity;

public interface IWatchListContraction extends BaseView {
    interface View extends BaseView {

        void setWatchInfo(WatchListEntity data);

        void setRemoveLikeSuccess(int id);
    }

    interface Presenter extends IClear {

        void getWatchListInfo();

        void setRemoveLike(int id);
    }
}
