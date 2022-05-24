package com.pro.maluli.module.message.systemNotification.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.entity.WatchListEntity;

public interface ISystemNotificationContraction extends BaseView {
    interface View extends BaseView {


        void setSystemNotic(SystemMsgEntity data);

        void deleteSystemSuccess();
    }
    interface Presenter extends IClear {


        void getSystemMsg();

        void setSystemTop(int id);

        void setDeleteSystem(int id);
    }
}
