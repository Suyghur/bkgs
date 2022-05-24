package com.pro.maluli.module.myself.anchorInformation.addLabel.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AddLabelPresenter extends BasePresenter<IAddLabelContraction.View> implements IAddLabelContraction.Presenter {
    public int page = 1;

    public AddLabelPresenter(Context context) {
        super(context);
    }

    @Override
    public void addLabel(String label) {
        add(mService.addAnchorLabel(label)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        mView.addLabelSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }


}
