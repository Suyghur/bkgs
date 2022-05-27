package com.pro.maluli.module.myself.fans.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.WatchListEntity;

public interface IMyFansContraction extends BaseView {
    interface View extends BaseView {

        void setWatchInfo(WatchListEntity data);

    }

    interface Presenter extends IClear {

        void getFansListInfo();

    }
}
