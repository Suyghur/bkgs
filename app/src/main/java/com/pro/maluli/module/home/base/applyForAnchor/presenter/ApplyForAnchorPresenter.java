package com.pro.maluli.module.home.base.applyForAnchor.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.ApplyLimitEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ApplyForAnchorPresenter extends BasePresenter<IApplyForAnchorContraction.View> implements IApplyForAnchorContraction.Presenter {
    public int page = 1;

    public ApplyForAnchorPresenter(Context context) {
        super(context);
    }


    @Override
    public void subImg(List<File> files, int type) {
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
                        mView.setUpdateImgSuccess(response.getData(), type);
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

    @Override
    public void subApplyForAnchor(int classificationId, String realname,
                                  String phone, String card_id, String desc,
                                  String font_card_image, String back_card_image,
                                  String holding_card_image, String certificate_image) {

        add(mService.submitApplyForAnchor(String.valueOf(classificationId), realname, phone, card_id, desc,
                        font_card_image, back_card_image, holding_card_image, certificate_image)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
                        ToastUtils.showShort(response.getMsg());
                        ((Activity) mContext).finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));

    }

    @Override
    public void getVideoCanTime() {
        add(mService.subApplyLimit()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ApplyLimitEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ApplyLimitEntity> response) {
                        mView.setVideoTime(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
