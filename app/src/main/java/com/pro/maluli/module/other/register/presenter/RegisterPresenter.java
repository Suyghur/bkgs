package com.pro.maluli.module.other.register.presenter;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class RegisterPresenter extends BasePresenter<IRegisterContraction.View> implements IRegisterContraction.Presenter {
    public RegisterPresenter(Context context) {
        super(context);
    }

    @Override
    public void getVerifiCationCode(String mobile) {
        showLoading(mContext);
        add(mService.getVerificationCode(mobile, "5")
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
}
