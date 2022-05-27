package com.pro.maluli.module.myself.setting.feedback.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.FeedbackEntity;

public interface IFeedBackContraction extends BaseView {
    interface View extends BaseView {
        void setFeedBackList(FeedbackEntity data);
    }

    interface Presenter extends IClear {

        void getFeedBackList();

    }
}
