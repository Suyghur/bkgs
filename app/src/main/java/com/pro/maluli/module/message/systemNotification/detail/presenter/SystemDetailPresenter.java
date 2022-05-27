package com.pro.maluli.module.message.systemNotification.detail.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.SystemDetailEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class SystemDetailPresenter extends BasePresenter<ISystemDetailContraction.View> implements ISystemDetailContraction.Presenter {
    public int page = 1;
    public String id;

    public SystemDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getSystemDetail() {
        add(mService.getSystemDetail(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemDetailEntity> response) {
                        mView.setDetailSystem(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
