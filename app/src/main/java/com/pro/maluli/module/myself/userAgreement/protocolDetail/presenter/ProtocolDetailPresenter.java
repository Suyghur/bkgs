package com.pro.maluli.module.myself.userAgreement.protocolDetail.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.ProtocolEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class ProtocolDetailPresenter extends BasePresenter<IProtocolDetailContraction.View> implements IProtocolDetailContraction.Presenter {
    public ProtocolDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getProtocoList(String id) {
        add(mService.getProtocoDetail(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ProtocolDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ProtocolDetailEntity> response) {
                        mView.setProtocolDetail(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
