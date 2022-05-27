package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorInfoEntity;

public interface IAnchorVideoContraction extends BaseView {
    interface View extends BaseView {
//        void setVideoSuccess(AnchorVideoEntity data);

        void setAnchorInfo(AnchorInfoEntity data);
    }

    interface Presenter extends IClear {
        void getAnchorInfo(String anchorID);
//        void getAnchorVideo();

        void deleteVideo(String deleteIdString);
    }
}
