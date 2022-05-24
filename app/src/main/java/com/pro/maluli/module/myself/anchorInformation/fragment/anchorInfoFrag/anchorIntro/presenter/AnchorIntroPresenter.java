package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnchorIntroPresenter extends BasePresenter<IAnchorIntroContraction.View> implements IAnchorIntroContraction.Presenter {
    public AnchorIntroPresenter(Context context) {
        super(context);
    }

    @Override
    public void changeAnchorDesc(String desc) {
        showLoading(mContext);
        add(mService.changeAnchorDesc(desc)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        dismissLoading(mContext);
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mContext).finish();
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
