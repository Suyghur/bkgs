package com.pro.maluli.module.message.fromUikit.messageSetting.presenter;

import android.content.Context;

import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class MessageSettingPresenter extends BasePresenter<IMessageSettingContraction.View> implements IMessageSettingContraction.Presenter {
    public int page = 1;

    public MessageSettingPresenter(Context context) {
        super(context);
    }

    @Override
    public void getMessageCanScore(String accid) {
        add(mService.getMessageScore(accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageCanScoreEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageCanScoreEntity> response) {
                        mView.setMessageCanScore(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void addBlack(String accid) {
        add(mService.addBlack(accid, "")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setAddBlackSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void removeBlack(String accid) {
        add(mService.removeBlack(accid, "", "")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.removeBlackSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
