package com.pro.maluli.module.video.videoact;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.WelcomInfoEntity;

public interface IVideoActContraction extends BaseView {
    interface View extends BaseView {
        void setAnchorInfo(AnchorInfoEntity data);
    }
    interface Presenter extends IClear {
        void getAnchorInfo(String Anchor);

    }
}
