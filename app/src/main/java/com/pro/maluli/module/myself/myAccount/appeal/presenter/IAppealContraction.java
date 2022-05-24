package com.pro.maluli.module.myself.myAccount.appeal.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IAppealContraction extends BaseView {
    interface View extends BaseView {
        void setUpdateImgSuccess(UpdateImgEntity data);
    }
    interface Presenter extends IClear {
        void subImg(List<File> files);

        void subFeedBlack(String content, String images, String type, String rel);

        void liveAppeal(String trim, String images, String type, String trim1);

        void reportLive(String content, String images, String msgType, String trim, String s,String userid, String liveId);

        void drawCashReport(String content, String images, String msgType, String trim);
    }
}
