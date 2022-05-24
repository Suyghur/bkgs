package com.pro.maluli.module.myself.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AcacheUtil;

import io.reactivex.functions.Consumer;

public class MyselfPresenter extends BasePresenter<IMyselfContraction.View> implements IMyselfContraction.Presenter {
    public MyselfPresenter(Context context) {
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
}
