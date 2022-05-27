package com.pro.maluli.module.home.oneToOne.answerPhone.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;

public class AnswerPhonePresenter extends BasePresenter<IAnswerPhoneContraction.View> implements IAnswerPhoneContraction.Presenter {
    public AnswerPhonePresenter(Context context) {
        super(context);
    }

//    @Override
//    public void getVerifiCationCode(String mobile) {
//        showLoading(mContext);
//        add(mService.getVerificationCode(mobile)
//                .compose(getTransformer())
//                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
//                    @Override
//                    public void onSuccess(BaseResponse<Object> response) {
//                        dismissLoading(mContext);
//                        String asda = response.toString();
//                        ToastUtils.showShort(response.getMsg());
//                        mView.getCodeSuccess();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        dismissLoading(mContext);
//                        throwable.printStackTrace();
//                    }
//                }));
//    }
}
