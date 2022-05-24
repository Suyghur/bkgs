package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.CanTimeVideoEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IAddVideoContraction extends BaseView {
    interface View extends BaseView {

        void setUpdateImgSuccess(UpdateImgEntity data);

        void setVideoTime(CanTimeVideoEntity data);

        void addVideoSuccess(String msg);
    }
    interface Presenter extends IClear {

        void subImg(List<File> files);

        void submitVideo(String trim, List<File>  videoUrl);

        void getVideoCanTime();
    }
}
