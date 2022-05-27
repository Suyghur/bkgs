package com.pro.maluli.module.message.lineUp.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.LineUpEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class LineUpPresenter extends BasePresenter<ILineUpContraction.View> implements ILineUpContraction.Presenter {
    public int page = 1;

    public LineUpPresenter(Context context) {
        super(context);
    }


    @Override
    public void getReserveMsg() {
        add(mService.getlineUpList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<LineUpEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<LineUpEntity> response) {
                        mView.setLineUplist(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void readLineUpmsg(int id) {
        add(mService.getReadlineUpMsg(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.readSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));

    }

}
