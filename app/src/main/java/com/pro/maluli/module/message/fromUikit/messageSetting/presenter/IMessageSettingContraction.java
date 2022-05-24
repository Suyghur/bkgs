package com.pro.maluli.module.message.fromUikit.messageSetting.presenter;

import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.WatchListEntity;

public interface IMessageSettingContraction extends BaseView {
    interface View extends BaseView {

        void setWatchInfo(WatchListEntity data);

        void setMessageCanScore(MessageCanScoreEntity data);

        void setAddBlackSuccess();

        void removeBlackSuccess();

    }
    interface Presenter extends IClear {

        void getMessageCanScore(String accid);

        void addBlack(String accid);

        void removeBlack(String accid);
    }
}
