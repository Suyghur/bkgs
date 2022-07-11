package com.pro.maluli.module.home.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class HomePresenter extends BasePresenter<IHomeContraction.View> implements IHomeContraction.Presenter {
    public HomePresenter(Context context) {
        super(context);
    }

    @Override
    public void getUserInfo() {
    }

    @Override
    public void getHomeInfo() {
        add(mService.getHomeData()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<HomeInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<HomeInfoEntity> response) {
                        response.getData();
                        mView.setHomeInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getLiveInfo() {
        add(mService.getLastTimeLive("1")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<LastTimeLiveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<LastTimeLiveEntity> response) {
                        response.getData();
//                        mView.userInfo();
//                        mView.setLastTimeLiveInfo(response.getData());
                        mView.setLastLive(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
