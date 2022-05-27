package com.pro.maluli.module.myself.setting.aboutMe.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AboutMeEntity;

public interface IAboutMeContraction extends BaseView {
    interface View extends BaseView {

        void setAboutMeInfo(AboutMeEntity data);
    }

    interface Presenter extends IClear {

        void getAboutInfo();
    }
}
