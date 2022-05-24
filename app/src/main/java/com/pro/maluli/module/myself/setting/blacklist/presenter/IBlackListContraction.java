package com.pro.maluli.module.myself.setting.blacklist.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.BlackListEntity;

public interface IBlackListContraction extends BaseView {
    interface View extends BaseView {

        void setDeleteSuccess();

        void setBlackLsit(BlackListEntity data);
    }

    interface Presenter extends IClear {

        void getBlackList();

        void deleteBlack(int id);

    }
}
