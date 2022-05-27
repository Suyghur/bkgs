package com.pro.maluli.module.message.systemNotification.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class SystemNotificationPresenter extends BasePresenter<ISystemNotificationContraction.View> implements ISystemNotificationContraction.Presenter {
    public int page = 1;

    public SystemNotificationPresenter(Context context) {
        super(context);
    }


    @Override
    public void getSystemMsg() {
        add(mService.getSystemList(String.valueOf(page))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemMsgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemMsgEntity> response) {
                        mView.setSystemNotic(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void setSystemTop(int id) {
        add(mService.getSystemTop(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemMsgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemMsgEntity> response) {
                        page = 1;
                        getSystemMsg();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void setDeleteSystem(int id) {
        add(mService.getDeleteSystemTop(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemMsgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemMsgEntity> response) {
                        mView.deleteSystemSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
