package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IAnchorIntroContraction extends BaseView {
    interface View extends BaseView {

    }

    interface Presenter extends IClear {
        void changeAnchorDesc(String desc);
    }
}
