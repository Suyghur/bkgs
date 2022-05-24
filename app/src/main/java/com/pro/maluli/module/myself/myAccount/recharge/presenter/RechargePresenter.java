package com.pro.maluli.module.myself.myAccount.recharge.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.WechatpayEntity;
import com.pro.maluli.common.entity.PayInfoEntity;
import com.pro.maluli.common.entity.RechargeEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class RechargePresenter extends BasePresenter<IRechargeContraction.View> implements IRechargeContraction.Presenter {
    public RechargePresenter(Context context) {
        super(context);
    }

    @Override
    public void getUserInfo() {

    }

    @Override
    public void getPayInfo() {
        add(mService.getPayInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<RechargeEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<RechargeEntity> response) {
                        mView.setPayInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void subAlipay(String type, String id) {
        add(mService.subAliPay(type, id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<PayInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<PayInfoEntity> response) {
//                        mView.setPayInfo(response.getData());
                        mView.setOrderInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void subWechatPay(String type, String id) {
        add(mService.subWechatPay(type, id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<WechatpayEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WechatpayEntity> response) {
//                        mView.setPayInfo(response.getData());
                        WechatpayEntity ada =response.getData();
                        mView.setWeChatOrderInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
