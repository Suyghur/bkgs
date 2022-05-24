package com.pro.maluli.module.message.reserveMessage.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.ReserveDetailEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class ReserveMessagePresenter extends BasePresenter<IReserveMessageContraction.View> implements IReserveMessageContraction.Presenter {
    public int page = 1;

    public ReserveMessagePresenter(Context context) {
        super(context);
    }


    @Override
    public void getReserveMsg() {
        add(mService.getReserveList(String.valueOf(page))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ReserveMessageEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ReserveMessageEntity> response) {
                        mView.setReserveNotic(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void setReserveTop(int id) {
        add(mService.getReserveTop(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemMsgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemMsgEntity> response) {
                        page = 1;
                        getReserveMsg();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void setDeleteReserve(int id) {
        add(mService.getDeleteReverseTop(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SystemMsgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SystemMsgEntity> response) {
//                        page = 1;
//                        getReserveMsg();
                        mView.deleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getReserveDetail(int id) {
        add(mService.getReserveDetail(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ReserveDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ReserveDetailEntity> response) {
                        mView.setReserveDetail(response.getData());
                        page = 1;
                        getReserveMsg();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
