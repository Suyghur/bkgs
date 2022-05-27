package com.pro.maluli.module.myself.setting.feedback.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.FeedbackEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class FeedBackPresenter extends BasePresenter<IFeedBackContraction.View> implements IFeedBackContraction.Presenter {
    public int page;

    public FeedBackPresenter(Context context) {
        super(context);
    }

    @Override
    public void getFeedBackList() {
        add(mService.getFeedBackList(String.valueOf(page), "10")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<FeedbackEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<FeedbackEntity> response) {
                        mView.setFeedBackList(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
