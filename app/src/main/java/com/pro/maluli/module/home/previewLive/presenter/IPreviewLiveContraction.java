package com.pro.maluli.module.home.previewLive.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.LivePayInfoEntity;

public interface IPreviewLiveContraction extends BaseView {
    interface View extends BaseView {


        void setAnchorInfo(AnchorInfoEntity data);

        void setJoinLiveSuccess(JoinLiveEntity data);

        void setGotoPay(LivePayInfoEntity entity);

        void setNoMoney();

        void setLikeAnNoLike(String msg);
    }

    interface Presenter extends IClear {
        void getAnchorInfo(String anchorID);

        void joinLive(String roomId);

        void payInLive(int id);

        void anchorSub();
    }
}
