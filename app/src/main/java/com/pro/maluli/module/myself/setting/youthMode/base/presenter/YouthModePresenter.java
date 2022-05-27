package com.pro.maluli.module.myself.setting.youthMode.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class YouthModePresenter extends BasePresenter<IYouthModeContraction.View> implements IYouthModeContraction.Presenter {
    public YouthModePresenter(Context context) {
        super(context);
    }

    @Override
    public void getYouthModelInfo() {
        showLoading(mContext);
        add(mService.getYouthModelInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        dismissLoading(mContext);
                        mView.setYouthSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }
}
