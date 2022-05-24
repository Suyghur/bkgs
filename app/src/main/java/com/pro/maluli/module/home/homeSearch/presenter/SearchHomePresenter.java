package com.pro.maluli.module.home.homeSearch.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class SearchHomePresenter extends BasePresenter<ISearchHomeContraction.View> implements ISearchHomeContraction.Presenter {
    public SearchHomePresenter(Context context) {
        super(context);
    }

    public int page;
    public String  keyword;


    @Override
    public void getBkDetail() {
        add(mService.homeSearchLive(keyword)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        LiveListEntity entity = JSONObject.parseObject(myJson, LiveListEntity.class);
//                        mView.setHomeLiveListData(entity);
                        mView.setBkDetailSuccess(entity);
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
        add(mService.getSearchHomeHistory()
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
        add(mService.deleteHomeSearchHistory()
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
