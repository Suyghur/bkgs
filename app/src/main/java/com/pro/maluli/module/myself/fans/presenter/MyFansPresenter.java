package com.pro.maluli.module.myself.fans.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class MyFansPresenter extends BasePresenter<IMyFansContraction.View> implements IMyFansContraction.Presenter {
    public int page = 1;

    public MyFansPresenter(Context context) {
        super(context);
    }

    @Override
    public void getFansListInfo() {
        add(mService.getFansList(String.valueOf(page), "10")
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

}
