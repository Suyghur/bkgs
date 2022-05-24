package com.pro.maluli.module.myself.userAgreement.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.ProtocolEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class UserAgreementPresenter extends BasePresenter<IUserAgreementContraction.View> implements IUserAgreementContraction.Presenter {
    public UserAgreementPresenter(Context context) {
        super(context);
    }

    @Override
    public void getProtocoList() {
        add(mService.getProtocolList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ProtocolEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ProtocolEntity> response) {
                        mView.setProtocolSuccess(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
