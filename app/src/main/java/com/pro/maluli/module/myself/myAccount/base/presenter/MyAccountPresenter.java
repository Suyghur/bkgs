package com.pro.maluli.module.myself.myAccount.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class MyAccountPresenter extends BasePresenter<IMyAccountContraction.View> implements IMyAccountContraction.Presenter {
    public MyAccountPresenter(Context context) {
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
                        mView.setUserInfoSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getMyAccount() {
        add(mService.myAccountInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MyAccountEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MyAccountEntity> response) {
                        mView.setAccountInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getCashOutInfo() {
        add(mService.bkWithdrawalDetail()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                        mView.setUserInfoSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
