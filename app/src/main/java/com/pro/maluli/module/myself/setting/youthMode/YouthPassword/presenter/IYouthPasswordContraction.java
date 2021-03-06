package com.pro.maluli.module.myself.setting.youthMode.YouthPassword.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.YouthEntity;

public interface IYouthPasswordContraction extends BaseView {
    interface View extends BaseView {

        void setYouthSuccess(YouthEntity data);

        void startSuccess();

        void stopSuccess();
    }

    interface Presenter extends IClear {
        void getYouthModelInfo();

        void setYouthStatus(String input);

        void stopYouth(String input);
    }
}
