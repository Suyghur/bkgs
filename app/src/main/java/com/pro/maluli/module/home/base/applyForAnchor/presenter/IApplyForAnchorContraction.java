package com.pro.maluli.module.home.base.applyForAnchor.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.ApplyLimitEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IApplyForAnchorContraction extends BaseView {
    interface View extends BaseView {


        void setUpdateImgSuccess(UpdateImgEntity data, int type);

        void setVideoTime(ApplyLimitEntity data);
    }

    interface Presenter extends IClear {


        void subImg(List<File> files, int type);

        void subApplyForAnchor(int classificationId, String realname, String phone, String card_id, String desc, String font_card_image, String back_card_image, String holding_card_image, String certificate_image);

        void getVideoCanTime();
    }
}
