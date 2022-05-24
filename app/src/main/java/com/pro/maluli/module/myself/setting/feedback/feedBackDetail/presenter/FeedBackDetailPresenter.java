package com.pro.maluli.module.myself.setting.feedback.feedBackDetail.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.FeedBackDetailEntity;
import com.pro.maluli.common.entity.FeedbackEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.module.myself.setting.blacklist.presenter.IBlackListContraction;

import io.reactivex.functions.Consumer;

public class FeedBackDetailPresenter extends BasePresenter<IFeedBackDetailContraction.View> implements IFeedBackDetailContraction.Presenter {
    public FeedBackDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getFeedbackDetail(String id) {
        add(mService.getFeedBackDetail(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<FeedBackDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<FeedBackDetailEntity> response) {
                        mView.setDetail(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void deleteFeedBack(String id) {
        add(mService.getDeleteFeedBack(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<FeedbackEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<FeedbackEntity> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mView).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
