package com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.presenter;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;

public interface IStartOneToMoreLiveContraction extends BaseView {
    interface View extends BaseView {



        void setGiftInfo(GiftEntity data);

        void setJoinLiveSuccess(JoinLiveEntity data);

        void setAnchorInfo(AnchorInfoEntity data);

        void sendGiftSuccess(GiftEntity.ListBean giftId);

        void setfinish();

        void setLikeAnNoLike(String msg);

        void setNomomey();

        void setShareImgSuccess(String logo);

        void giftFormeSuccess(GiftForMeEntity giftForMeEntity);
    }
    interface Presenter extends IClear {
        void anchorSub();
        void closeLive();

        void getliveInfo();

        void getGiftInfo();

        void getAnchorInfo();
        void getShareImg();
        void sendGift(GiftEntity.ListBean giftId);

        void getGiftForMe();
    }
}
