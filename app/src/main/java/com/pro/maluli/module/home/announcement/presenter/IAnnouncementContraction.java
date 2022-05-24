package com.pro.maluli.module.home.announcement.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.entity.WatchListEntity;

public interface IAnnouncementContraction extends BaseView {
    interface View extends BaseView {


        void setNoticeInfo(NoticeEntity data);
    }
    interface Presenter extends IClear {

        void getFansListInfo();

    }
}
