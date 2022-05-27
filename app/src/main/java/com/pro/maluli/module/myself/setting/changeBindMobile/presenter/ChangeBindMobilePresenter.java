package com.pro.maluli.module.myself.setting.changeBindMobile.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class ChangeBindMobilePresenter extends BasePresenter<IChangeBindMobileContraction.View> implements IChangeBindMobileContraction.Presenter {
    public ChangeBindMobilePresenter(Context context) {
        super(context);
    }

    @Override
    public void getVerifiCationCode(String mobile, String codeType) {
        showLoading(mContext);
        add(mService.getVerificationCode(mobile, codeType)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
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

    /**
     * @param mobile
     * @param code
     * @param type   类型 1-验证手机号 2-更改手机号 (第一步先验证手机号) -> (第二步更改手机)
     */
    @Override
    public void changeBindmobile(String mobile, String code, String type) {
        showLoading(mContext);
        add(mService.changeBingdMobile(mobile, code, type)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        if (type.equalsIgnoreCase("2")) {
                            ToastUtils.showShort(response.getMsg());
                            ((Activity) mContext).finish();
                            return;
                        }
                        mView.setPwdSuccess(response.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
    }

}
