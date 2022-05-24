package com.pro.maluli.module.message.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class MessagePresenter extends BasePresenter<IMessageContraction.View> implements IMessageContraction.Presenter {
    public MessagePresenter(Context context) {
        super(context);
    }

    @Override
    public void getUserInfo() {
        add(mService.getUserInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getMessageList() {
        add(mService.getMessageList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageListEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageListEntity> response) {
                        mView.setMessageSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
