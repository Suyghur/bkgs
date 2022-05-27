package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.AnchorVideoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnchorVideoPresenter extends BasePresenter<IAnchorVideoContraction.View> implements IAnchorVideoContraction.Presenter {
    public String anchorId;

    public AnchorVideoPresenter(Context context) {
        super(context);
    }

    //
//    @Override
//    public void getAnchorVideo() {
//        add(mService.getAnchorVideo()
//                .compose(getTransformer())
//                .subscribe(new SuccessConsumer<BaseResponse<AnchorVideoEntity>>(mView) {
//                    @Override
//                    public void onSuccess(BaseResponse<AnchorVideoEntity> response) {
////                        String sdad = response.toString();
//                        mView.setVideoSuccess(response.getData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//
//                    }
//                }));
//    }
    @Override
    public void getAnchorInfo(String anchorID) {
        add(mService.getAnchorInfomation(anchorID)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorInfoEntity> response) {
                        mView.setAnchorInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void deleteVideo(String deleteIdString) {
        add(mService.deleteAnchorVideo(deleteIdString)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorVideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorVideoEntity> response) {
//                        String sdad = response.toString();
                        ToastUtils.showShort(response.getMsg());
                        getAnchorInfo(String.valueOf(anchorId));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
