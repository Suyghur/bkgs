package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;

public interface IAnchorImageContraction extends BaseView {
    interface View extends BaseView {

        void setAnchorInfo(AnchorInfoEntity data);
    }
    interface Presenter extends IClear {
        void getAnchorInfo();

    }
}
