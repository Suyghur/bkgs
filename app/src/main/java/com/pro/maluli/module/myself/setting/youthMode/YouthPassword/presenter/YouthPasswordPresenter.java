package com.pro.maluli.module.myself.setting.youthMode.YouthPassword.presenter;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.toolkit.Logger;

import io.reactivex.functions.Consumer;

public class YouthPasswordPresenter extends BasePresenter<IYouthPasswordContraction.View> implements IYouthPasswordContraction.Presenter {
    public YouthPasswordPresenter(Context context) {
        super(context);
    }

    @Override
    public void getYouthModelInfo() {
        add(mService.getYouthModelInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        mView.setYouthSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void setYouthStatus(String input) {
        showLoading(mContext);
        add(mService.startYouth(input)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        dismissLoading(mContext);
                        ToastUtils.showShort(response.getMsg());
                        mView.startSuccess();
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
    public void stopYouth(String input) {
        showLoading(mContext);
        add(mService.stopYouth(input)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        dismissLoading(mContext);
                        ToastUtils.showShort(response.getMsg());
                        mView.stopSuccess();
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
