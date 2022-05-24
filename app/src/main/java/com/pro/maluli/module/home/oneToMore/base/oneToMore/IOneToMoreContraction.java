package com.pro.maluli.module.home.oneToMore.base.oneToMore;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IOneToMoreContraction extends BaseView {
    interface View extends BaseView {

        void setUpdateImgSuccess(UpdateImgEntity data);

        void setStartInfo(OneToOneEntity data);

        void setLastLive(LastTimeLiveEntity data);
    }

    interface Presenter extends IClear {
        void startLive(String type, String title, String imgUrl, String money);

        void subImg(List<File> files);

        void getLastTimeLive();
    }
}
