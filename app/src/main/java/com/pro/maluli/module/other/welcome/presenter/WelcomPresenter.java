package com.pro.maluli.module.other.welcome.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.WelcomInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class WelcomPresenter extends BasePresenter<IWelcomContraction.View> implements IWelcomContraction.Presenter {
    public WelcomPresenter(Context context) {
        super(context);
    }

    @Override
    public void getWelcomInfo() {
        add(mService.GetWelcomeInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<WelcomInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WelcomInfoEntity> response) {
                        mView.setWelcomInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
