package com.pro.maluli.module.myself.myAccount.withdraw.detail.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class RewardDetailPresenter extends BasePresenter<IRewardDetailContraction.View> implements IRewardDetailContraction.Presenter {
    public int page;
    public String end_time, start_time, date, flow_type, keyword = "";
    public RewardDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getBkDetail() {
        add(mService.rewardDetailRecord(String.valueOf(page), end_time, start_time, date, flow_type, keyword)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<RewardDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<RewardDetailEntity> response) {
                        mView.setBkDetailSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
