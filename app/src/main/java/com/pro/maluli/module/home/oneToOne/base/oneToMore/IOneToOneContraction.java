package com.pro.maluli.module.home.oneToOne.base.oneToMore;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IOneToOneContraction extends BaseView {
    interface View extends BaseView {

        void setUpdateImgSuccess(UpdateImgEntity data);

//        void setStartInfo(OneToOneEntity data);

        void setLastLive(LastTimeLiveEntity data);
    }

    interface Presenter extends IClear {

        void startLive(String s, String trim, String imgUrl);

        void subImg(List<File> files);

        void getFansListInfo();
    }
}
