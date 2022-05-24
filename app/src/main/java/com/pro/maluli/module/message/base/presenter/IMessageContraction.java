package com.pro.maluli.module.message.base.presenter;

import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.MessageListEntity;

public interface IMessageContraction extends BaseView {
    interface View extends BaseView {

        void setMessageSuccess(MessageListEntity data);
    }
    interface Presenter extends IClear {
        void getUserInfo();

        void getMessageList();
    }
}
