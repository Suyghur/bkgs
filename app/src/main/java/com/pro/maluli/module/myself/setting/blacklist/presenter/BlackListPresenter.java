package com.pro.maluli.module.myself.setting.blacklist.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.BlackListEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class BlackListPresenter extends BasePresenter<IBlackListContraction.View> implements IBlackListContraction.Presenter {
    public BlackListPresenter(Context context) {
        super(context);
    }

    @Override
    public void getBlackList() {
        add(mService.getBlackList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<BlackListEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<BlackListEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                        mView.setBlackLsit(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void deleteBlack(int id) {
        add(mService.getDeleteBlack(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<BlackListEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<BlackListEntity> response) {
                        mView.setDeleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
