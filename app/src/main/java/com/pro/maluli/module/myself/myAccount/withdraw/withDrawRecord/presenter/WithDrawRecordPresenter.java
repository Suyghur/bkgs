package com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.WithDrawRecordEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class WithDrawRecordPresenter extends BasePresenter<IWithDrawRecordContraction.View> implements IWithDrawRecordContraction.Presenter {
    public int page;

    public WithDrawRecordPresenter(Context context) {
        super(context);
    }

    @Override
    public void getSearchHistory() {
        add(mService.getWithdrawRecord(String.valueOf(page))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<WithDrawRecordEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WithDrawRecordEntity> response) {
                        mView.setWithDrawRecordList(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));

    }

}
