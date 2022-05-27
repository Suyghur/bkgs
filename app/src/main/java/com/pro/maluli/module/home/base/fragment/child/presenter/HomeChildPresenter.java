package com.pro.maluli.module.home.base.fragment.child.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;

public class HomeChildPresenter extends BasePresenter<IHomeChildContraction.View> implements IHomeChildContraction.Presenter {
    public HomeChildPresenter(Context context) {
        super(context);
    }

    @Override
    public void getUserInfo() {
//        add(mService.getUserInfo()
//                .compose(getTransformer())
//                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
//                    @Override
//                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
//                        String sdad = response.toString();
////                        mView.userInfo();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//
//                    }
//                }));
    }
}
