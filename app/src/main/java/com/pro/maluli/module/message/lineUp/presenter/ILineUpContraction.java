package com.pro.maluli.module.message.lineUp.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LineUpEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;

public interface ILineUpContraction extends BaseView {
    interface View extends BaseView {

        void setLineUplist(LineUpEntity data);

        void readSuccess();
    }
    interface Presenter extends IClear {


        void getReserveMsg();


        void readLineUpmsg(int id);
    }
}
