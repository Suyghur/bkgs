package com.pro.maluli.module.message.systemNotification.detail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.SystemDetailEntity;

public interface ISystemDetailContraction extends BaseView {
    interface View extends BaseView {


        void setDetailSystem(SystemDetailEntity data);
    }

    interface Presenter extends IClear {


        void getSystemDetail();
    }
}
