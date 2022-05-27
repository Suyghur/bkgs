package com.pro.maluli.module.main.base.presenter;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.entity.YouthEntity;

public interface IMainContraction extends BaseView {
    interface View extends BaseView {

        void setUserInfoSuccess(UserInfoEntity data);

        void setProtocolDetail(ProtocolDetailEntity data);

        void setSubRealConceal();

        void setNoticeSuccess();

        void setGiftInfo(GiftEntity data);

        void sendGiftSuccess();

        void getForMeGiftListSuccess(GiftForMeEntity data);

        void reOrderSuccess(String data);

        void setMessageCanScore(MessageCanScoreEntity data);

        void setGotoRechargeAct();

        void setYouthSuccess(YouthEntity data);

        void setMessageSuccess(MessageListEntity data);

        void setGoAnchorInfo(String anchorId);
    }

    interface Presenter extends IClear {
        void getUserInfo();

        void getGiftInfo();

        void getProtocoList(String id);

        void RealConceal();

        void agreeNotice(int id);

        void sendGift(int giftId, String accid);

        void getGiftgiveList(String accid);

        void subMitReCallLive(String link);

        void getMessageCanScore(String accid);

        void sendMessageForGift(String accid);

        void sendScoreForAnchor(String accid, int ablitityNumber, int serviceNumber);

        void getTeenager();

        void getMessageList();

        void checkISAnchor(String accid);
    }
}
