package com.pro.maluli.module.video.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.VideoEntity;

public interface ISmallVideoContraction extends BaseView {
    interface View extends BaseView {
        void setVideoInfo(VideoEntity data);

        void setvideoTime();


        void setNoSeeVideo(String msg);
    }
    interface Presenter extends IClear {

        void getVideo();

        void videoTime();

    }
}
