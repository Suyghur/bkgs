package com.pro.maluli.module.message.searchMessasge.presenter;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.SearchEntity;

import java.util.List;

public interface ISearchMsgContraction extends BaseView {
    interface View extends BaseView {

        void setBkDetailSuccess(List<IMMessage> data);

        void setHistorySearch(SearchEntity data);

        void deleteSuccess();

        void setUserNameInfo(List<NimUserInfo> param);
    }
    interface Presenter extends IClear {

        void getBkDetail();

        void searchUser();

        void getSearchHistory();

        void deleteHistory();
    }
}
