package com.pro.maluli.module.myself.setting.feedback.feedBackDetail.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.FeedBackDetailEntity;

public interface IFeedBackDetailContraction extends BaseView {
    interface View extends BaseView {
        void setDetail(FeedBackDetailEntity data);
    }
    interface Presenter extends IClear {

        void getFeedbackDetail(String id);

        void deleteFeedBack(String id);
    }
}
