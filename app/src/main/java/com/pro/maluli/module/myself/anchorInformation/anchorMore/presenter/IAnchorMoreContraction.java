package com.pro.maluli.module.myself.anchorInformation.anchorMore.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IAnchorMoreContraction extends BaseView {
    interface View extends BaseView {
        void setAddBlackSuccess();

        void removeBlackSuccess();
    }

    interface Presenter extends IClear {


        void addBlack(String accid);

        void removeBlack(String accid);

    }
}
