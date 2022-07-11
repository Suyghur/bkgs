package com.pro.maluli.module.home.oneToOne.queue.presenter;

import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.OneToOneLiveEntity;
import com.pro.maluli.common.entity.ReserveEntity;

import java.io.File;
import java.util.List;

public interface IOneToOneQueueContraction extends BaseView {
    interface View extends BaseView {

        void setQueueSuccess(ReserveEntity data);

        void setLikeAnNoLike(String msg);

        void setStartInfo(OneToOneEntity data);

        void setDqLiveInfo(OneToOneLiveEntity data);

        void setReserveSuccess();

        void setReserveFailed(BaseResponse<Object> response);

    }

    interface Presenter extends IClear {

        void startLive(String type, String title, String imgUrl);

//        void startLive(String type, String title, String imgUrl, String model, String money, String people);

        void getReserveInfo();

        void subImg(List<File> files, String content);

        void subReserve(String content, String images);

        void anchorSub();

        void changeLiveInfo(String number, String desc);


        void getLiveInfo();

        void canReserve();

        void cancelReserve();
    }
}
