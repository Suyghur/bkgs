package com.pro.maluli.module.myself.myAccount.appeal.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AppealPresenter extends BasePresenter<IAppealContraction.View> implements IAppealContraction.Presenter {
    public AppealPresenter(Context context) {
        super(context);
    }

    @Override
    public void subImg(List<File> files) {
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
                        mView.setUpdateImgSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
//        return Api.getDefault(ApiConstants.TYPE_HOST)
//                .uploadImage(map)
//                .compose(RxSchedulers.handleResult());
    }

    /**
     * 意见反馈
     */
    @Override
    public void subFeedBlack(String content, String images, String type, String rel) {
        add(mService.getsubReport(content, images, type, rel)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mView).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 直播申诉
     */
    @Override
    public void liveAppeal(String trim, String images, String type, String trim1) {
        add(mService.getLivesubReport(trim, images, type, trim1)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mView).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 直播举报
     */
    @Override
    public void reportLive(String content, String images, String msgType, String trim, String s, String userid, String liveId) {
        add(mService.getreport(content, images, msgType, trim, s, userid, liveId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mView).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 冻结申诉
     */
    @Override
    public void drawCashReport(String content, String images, String msgType, String trim) {
        add(mService.getCashReport(content, images, msgType, trim)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mView).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

}
