package com.pro.maluli.module.home.base.fragment.rechild.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LiveListEntity;

public interface IChildLiveListContraction extends BaseView {
    interface View extends BaseView {

        void setHomeLiveListData(LiveListEntity entity);
    }
    interface Presenter extends IClear {
        void getHomeLiveList(String id);

    }
}
