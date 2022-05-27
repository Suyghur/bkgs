package com.pro.maluli.module.home.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.LastTimeLiveEntity;

public interface IHomeContraction extends BaseView {
    interface View extends BaseView {

        void setHomeInfo(HomeInfoEntity data);

        void setLastLive(LastTimeLiveEntity data);
    }

    interface Presenter extends IClear {
        void getUserInfo();

        void getHomeInfo();

        void getLiveInfo();

    }
}
