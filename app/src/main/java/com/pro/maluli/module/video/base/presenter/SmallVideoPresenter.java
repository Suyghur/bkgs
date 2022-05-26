package com.pro.maluli.module.video.base.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class SmallVideoPresenter extends BasePresenter<ISmallVideoContraction.View> implements ISmallVideoContraction.Presenter {
    public SmallVideoPresenter(Context context) {
        super(context);
    }


    @Override
    public void getVideo() {
        add(mService.getVideo("")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                        if ("4002".equalsIgnoreCase(response.getData().getStatus_code())) {
                            mView.setNoSeeVideo(response.getMsg());
                            return;
                        }
                        mView.setVideoInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void videoTime() {
        add(mService.videoTime()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
                        try {
                            String sda = response.getData().getStatus_code();
                            if ("4002".equalsIgnoreCase(response.getData().getStatus_code())) {
                                mView.setNoSeeVideo(response.getMsg());
//                            return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mView.setVideoTime();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
