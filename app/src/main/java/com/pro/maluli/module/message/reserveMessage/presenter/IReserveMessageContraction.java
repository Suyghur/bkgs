package com.pro.maluli.module.message.reserveMessage.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.ReserveDetailEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.entity.SystemMsgEntity;

public interface IReserveMessageContraction extends BaseView {
    interface View extends BaseView {


        void setReserveNotic(ReserveMessageEntity data);

        void setReserveDetail(ReserveDetailEntity data);

        void deleteSuccess();
    }
    interface Presenter extends IClear {


        void getReserveMsg();

        void setReserveTop(int id);

        void setDeleteReserve(int id);

        void getReserveDetail(int id);
    }
}
