package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnchorInfoPresenter extends BasePresenter<IAnchorInfoContraction.View> implements IAnchorInfoContraction.Presenter {
    public String anchorID;

    public AnchorInfoPresenter(Context context) {
        super(context);
    }

    @Override
    public void getAnchorInfo() {

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
}
