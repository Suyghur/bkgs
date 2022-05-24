package com.pro.maluli.module.myself.myAccount.withdraw.base.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class WithdrawPresenter extends BasePresenter<IWithdrawContraction.View> implements IWithdrawContraction.Presenter {
    public String code;
    public String money;

    public WithdrawPresenter(Context context) {
        super(context);
    }

    @Override
    public void getMyAccount() {
        add(mService.myAccountInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MyAccountEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MyAccountEntity> response) {
                        mView.setAccountInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getVerifiCationCode(String mobile, String codetype) {
        showLoading(mContext);
        add(mService.getVerificationCode(mobile, codetype)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        String asda = response.toString();
                        ToastUtils.showShort(response.getMsg());
                        mView.getCodeSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void subWithdraw() {
        showLoading(mContext);
        add(mService.subCashOut(code, money)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mContext).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }
}
