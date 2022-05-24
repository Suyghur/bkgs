package com.pro.maluli.module.myself.setting.youthMode.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.YouthEntity;

public interface IYouthModeContraction extends BaseView {
    interface View extends BaseView {

        void setYouthSuccess(YouthEntity data);
    }

    interface Presenter extends IClear {
        void getYouthModelInfo();
    }
}
