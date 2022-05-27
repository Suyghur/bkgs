package com.pro.maluli.common.networkRequest;

import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.module.app.BKGSApplication;

import io.reactivex.functions.Consumer;

public abstract class SuccessConsumer<T> implements Consumer<T> {
    private BaseView mBaseView;

    public SuccessConsumer(BaseView baseView) {
        mBaseView = baseView;
    }

    @Override
    public void accept(T response) {

        BaseResponse baseResponse = (BaseResponse) response;
        int code = baseResponse.getCode();
        if (code == ResponseCode.SUCCESS
                || code == ResponseCode.NO_LOGIN_PWD
                || code == ResponseCode.BIND_MOBILE
                || code == ResponseCode.PAY_LIVE
                || code == ResponseCode.NO_MONEY) {
            onSuccess(response);
        } else if (code == ResponseCode.NO_LOGIN) {
            AcacheUtil.loginOut(BKGSApplication.getApp());
        } else {
            mBaseView.onError(code, baseResponse.getMsg());

        }


    }

    public abstract void onSuccess(T response);
}
