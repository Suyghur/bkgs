package com.pro.maluli.module.myself.myAccount.withdraw.search.searchReward.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class SearchRewardPresenter extends BasePresenter<ISearchRewardContraction.View> implements ISearchRewardContraction.Presenter {
    public int page;
    public String end_time, start_time, date, flow_type, keyword;
    public SearchRewardPresenter(Context context) {
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
                        getSearchHistory();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getSearchHistory() {
        add(mService.getSearchRewardHistory()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SearchEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SearchEntity> response) {
                        mView.setHistorySearch(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));

    }

    @Override
    public void deleteHistory() {
        add(mService.deleteRewardSearchHistory()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.deleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
