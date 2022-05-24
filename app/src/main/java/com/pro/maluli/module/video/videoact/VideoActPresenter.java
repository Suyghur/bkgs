package com.pro.maluli.module.video.videoact;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.WelcomInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class VideoActPresenter extends BasePresenter<IVideoActContraction.View> implements IVideoActContraction.Presenter {
    public VideoActPresenter(Context context) {
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
}
