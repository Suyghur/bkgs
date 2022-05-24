package com.pro.maluli.module.myself.anchorInformation.anchorMore.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnchorMorePresenter extends BasePresenter<IAnchorMoreContraction.View> implements IAnchorMoreContraction.Presenter {
    public int page = 1;

    public AnchorMorePresenter(Context context) {
        super(context);
    }

    @Override
    public void addBlack(String accid) {
        add(mService.addBlack("",accid)
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
        add(mService.removeBlack("",accid,"")
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
