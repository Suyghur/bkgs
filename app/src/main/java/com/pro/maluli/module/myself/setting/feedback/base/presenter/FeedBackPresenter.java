package com.pro.maluli.module.myself.setting.feedback.base.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.FeedbackEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.module.myself.setting.blacklist.presenter.IBlackListContraction;

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
