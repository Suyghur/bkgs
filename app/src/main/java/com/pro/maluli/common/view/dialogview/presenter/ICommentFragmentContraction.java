package com.pro.maluli.common.view.dialogview.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.VideoEntity;

public interface ICommentFragmentContraction extends BaseView {
    interface View extends BaseView {
        void setVideoInfo(VideoEntity data);

        void commentSuccess();
    }

    interface Presenter extends IClear {


        void getCommentVideo(String videoId);

        void subComment(String videoId, String s);
    }
}
