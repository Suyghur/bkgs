package com.pro.maluli.module.myself.userAgreement.protocolDetail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.ProtocolEntity;

public interface IProtocolDetailContraction extends BaseView {
    interface View extends BaseView {

        void setProtocolDetail(ProtocolDetailEntity data);
    }
    interface Presenter extends IClear {

        void getProtocoList(String id);
    }
}
