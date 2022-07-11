package com.pro.maluli.module.myself.myAccount.recharge.detail.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.BKRecordEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class BkDetailPresenter extends BasePresenter<IBkDetailContraction.View> implements IBkDetailContraction.Presenter {
    public int page;
    public String end_time, start_time, date, flow_type, keyword = "";

    public BkDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getBkDetail() {
        add(mService.bkDetailRecord(String.valueOf(page), end_time, start_time, date, flow_type, keyword)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<BKRecordEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<BKRecordEntity> response) {
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
