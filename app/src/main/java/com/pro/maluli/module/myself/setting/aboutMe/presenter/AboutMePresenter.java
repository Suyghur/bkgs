package com.pro.maluli.module.myself.setting.aboutMe.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AboutMePresenter extends BasePresenter<IAboutMeContraction.View> implements IAboutMeContraction.Presenter {
    public AboutMePresenter(Context context) {
        super(context);
    }


    @Override
    public void getAboutInfo() {
        add(mService.getAboutMeInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AboutMeEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AboutMeEntity> response) {
                        mView.setAboutMeInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
