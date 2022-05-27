package com.pro.maluli.module.myself.anchorInformation.addLabel.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;

public interface IAddLabelContraction extends BaseView {
    interface View extends BaseView {
        void addLabelSuccess();
    }

    interface Presenter extends IClear {


        void addLabel(String label);

    }
}
