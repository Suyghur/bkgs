package com.pro.maluli.module.myself.personalCenter.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.entity.UserInfoEntity;

import java.io.File;
import java.util.List;

public interface IPersonalCenterContraction extends BaseView {
    interface View extends BaseView {
        void setUserInfo(UserInfoEntity entity);

        void setUpdateImgSuccess(UpdateImgEntity data);

        void setUpdateSuccess(String msg);
    }
    interface Presenter extends IClear {
        void getUserinfo();
        void subImg(List<File> files);

        void changerUserInfo(String imgUrl, int genderType, String trim);
    }
}
