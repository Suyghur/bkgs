package com.pro.maluli.module.myself.watchlist.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class WatchListPresenter extends BasePresenter<IWatchListContraction.View> implements IWatchListContraction.Presenter {
    public int page = 1;

    public WatchListPresenter(Context context) {
        super(context);
    }

    @Override
    public void getWatchListInfo() {
        add(mService.getWacthList(String.valueOf(page), "10")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<WatchListEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WatchListEntity> response) {
                        mView.setWatchInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void setRemoveLike(int id) {
        add(mService.removeLike(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setRemoveLikeSuccess(id);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
