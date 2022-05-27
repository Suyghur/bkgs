package com.pro.maluli.module.myself.userAgreement.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.ProtocolEntity;

public interface IUserAgreementContraction extends BaseView {
    interface View extends BaseView {
        void setProtocolSuccess(ProtocolEntity data);
    }

    interface Presenter extends IClear {

        void getProtocoList();
    }
}
