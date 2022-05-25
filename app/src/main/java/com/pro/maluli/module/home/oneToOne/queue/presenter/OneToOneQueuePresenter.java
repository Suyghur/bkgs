package com.pro.maluli.module.home.oneToOne.queue.presenter;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.OneToOneLiveEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OneToOneQueuePresenter extends BasePresenter<IOneToOneQueueContraction.View> implements IOneToOneQueueContraction.Presenter {
    public int page = 1;
    public String anchor_id = "";

    public OneToOneQueuePresenter(Context context) {
        super(context);
    }

    @Override
    public void startLive(String type, String title, String imgUrl) {
        add(mService.startLive(type, title, imgUrl, "", "", "0")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<OneToOneEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<OneToOneEntity> response) {
                        dismissLoading(mContext);
                        mView.setStartInfo(response.getData());
//                        mView.setStartInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getReserveInfo() {
        add(mService.getQueueInfo(anchor_id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ReserveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ReserveEntity> response) {
                        mView.setQueueSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subImg(List<File> files, String content) {
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            //必须加filename字段
            map.put("images[]\"; filename=\"" + file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));

        }
        add(mService.uploadImage(map)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UpdateImgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UpdateImgEntity> response) {
                        dismissLoading(mContext);
                        UpdateImgEntity entity = response.getData();
                        String asda = StringUtils.sortImg(entity.getUrl());
                        subReserve(content, asda);
//                        mView.setUpdateImgSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subReserve(String content, String images) {
        add(mService.subReserve(anchor_id, content, images)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
//                        mView.setLikeAnNoLike(response.getMsg());
//                        getReserveInfo();
                        mView.setReserveSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void anchorSub() {
        add(mService.removeLike(anchor_id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setLikeAnNoLike(response.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void changeLiveInfo(String number, String desc) {
        add(mService.changeLiveInfo(number, desc)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ReserveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ReserveEntity> response) {
//                        mView.setLikeAnNoLike(response.getMsg());
                        ToastUtils.showShort(response.getMsg());
                        getReserveInfo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

//    @Override
//    public void startLive(String type, String title, String imgUrl, String model, String money, String people) {
////        add(mService.startLive(type, title, imgUrl, "", "")
////                .compose(getTransformer())
////                .subscribe(new SuccessConsumer<BaseResponse<OneToOneEntity>>(mView) {
////                    @Override
////                    public void onSuccess(BaseResponse<OneToOneEntity> response) {
////                        dismissLoading(mContext);
////                        mView.setStartInfo(response.getData());
////
////                    }
////                }, new Consumer<Throwable>() {
////                    @Override
////                    public void accept(Throwable throwable) throws Exception {
////                        dismissLoading(mContext);
////                        throwable.printStackTrace();
////                    }
////                }));
//    }

    @Override
    public void getLiveInfo() {
        add(mService.getOneLiveInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<OneToOneLiveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<OneToOneLiveEntity> response) {
                        mView.setDqLiveInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void canReserve() {
        showLoading(mContext);
        add(mService.canReserve()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<OneToOneEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<OneToOneEntity> response) {
                        dismissLoading(mContext);
                        ToastUtils.showShort(response.getMsg());
                        getReserveInfo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 用户取消预约
     */
    @Override
    public void cancelReserve() {
        add(mService.cancelReserve(anchor_id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        ToastUtils.showShort(response.getMsg());
                        getReserveInfo();
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
