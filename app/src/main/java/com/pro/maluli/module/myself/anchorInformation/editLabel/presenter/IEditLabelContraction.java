package com.pro.maluli.module.myself.anchorInformation.editLabel.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.entity.WatchListEntity;

public interface IEditLabelContraction extends BaseView {
    interface View extends BaseView {


        void setLabelSuccess(AnchorLabelEntity data);

        void setDeleteSuccess();
    }
    interface Presenter extends IClear {


        void getLabel();

        void deleteLabel(int id);
    }
}
