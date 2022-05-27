package com.pro.maluli.module.myself.myAccount.recharge.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.PayInfoEntity;
import com.pro.maluli.common.entity.RechargeEntity;
import com.pro.maluli.common.entity.WechatpayEntity;

public interface IRechargeContraction extends BaseView {
    interface View extends BaseView {

        void setPayInfo(RechargeEntity data);

        void setOrderInfo(PayInfoEntity data);

        void setWeChatOrderInfo(WechatpayEntity data);
    }

    interface Presenter extends IClear {
        void getUserInfo();

        void getPayInfo();


        void subAlipay(String type, String id);

        void subWechatPay(String s, String valueOf);
    }
}
