package com.pro.maluli.module.myself.setting.base.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.UserInfoEntity;

public interface ISettingContraction extends BaseView {
    interface View extends BaseView {
        void setUserInfoSuccess(UserInfoEntity response);

        void setSuccessBind(String msg);
    }
    interface Presenter extends IClear {
        void getUserInfo();

        void unbindWechat(String s);

        void bindWechatAndQQ(String type, String weChatData);

        void bindAlipay(String auth_code, String alipay_open_id, String user_id);

        void unbindAlipay();

    }
}
