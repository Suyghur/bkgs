package com.pro.maluli.module.home.startLive.presenter;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.OneToOneLiveEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;

public interface IStartLiveContraction extends BaseView {
    interface View extends BaseView {


        void setSeeLiveInfo(SeeLiveUserEntity data);


        void closeLiveSuccess();

        void setGiftInfo(GiftEntity data);

        void sendGiftSuccess(GiftEntity.ListBean giftId);

        void setLikeAnNoLike(String msg);

        void setTimeSucess();

        void addTimeSucess();

        void endLiveSuccess();

        void jumpLiveSuccess();

        void setJoinLiveSuccess(JoinLiveEntity data);

        void setNomomey();

        void setGiftForMe(GiftForMeEntity giftForMeEntity);

        void finishAct();
    }
    interface Presenter extends IClear {

        void getGiftForMe();
        void anchorSub();

        void getSeeLiveUserInfo();


        void getGiftInfo();

        void closeLive();

        void sendGift(GiftEntity.ListBean id);

        void setLiveTime(String time);

        void addLiveTime(String time);

        void endLive();

        void jumpLive();

        void getGoingLive();

        void subScoreToLive(String valueOf, String valueOf1);

        void subGuest(String id);

        void getShareImg();

    }
}
