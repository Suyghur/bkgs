package com.pro.maluli.module.myself.anchorInformation.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnchorInformationPresenter extends BasePresenter<IAnchorInformationContraction.View> implements IAnchorInformationContraction.Presenter {
    public String anchorID;

    public AnchorInformationPresenter(Context context) {
        super(context);
    }


    @Override
    public void getAnchorInfo(String anchorID) {
        add(mService.getAnchorInfomation(anchorID)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorInfoEntity> response) {
                        mView.setAnchorInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void anchorSub() {
        add(mService.removeLike(anchorID)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setLikeAnNoLike(response.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
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
