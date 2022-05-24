package com.pro.maluli.module.home.announcement.detail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.NoticeEntity;

public interface IAnnouncementDetailContraction extends BaseView {
    interface View extends BaseView {


        void setDetailInfo(NoticeEntity.ListBean data);
    }
    interface Presenter extends IClear {


        void getNoticDetailInfo();
    }
}
