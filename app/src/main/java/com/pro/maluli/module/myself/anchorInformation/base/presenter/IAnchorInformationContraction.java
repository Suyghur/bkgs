package com.pro.maluli.module.myself.anchorInformation.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;

public interface IAnchorInformationContraction extends BaseView {
    interface View extends BaseView {

        void setLikeAnNoLike(String msg);

        void setAnchorInfo(AnchorInfoEntity data);

        void setAboutMeInfo(AboutMeEntity data);
    }
    interface Presenter extends IClear {

        void getAnchorInfo(String anchorID);

        void anchorSub();

        void getAboutInfo();
    }
}
