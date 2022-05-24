package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.entity.CanTimeVideoEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddVideoPresenter extends BasePresenter<IAddVideoContraction.View> implements IAddVideoContraction.Presenter {
    public int page = 1;

    public AddVideoPresenter(Context context) {
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
        add(mService.uploadVideo(map)
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

    @Override
    public void submitVideo(String trim, List<File> videoUrl) {
        showLoading(mContext);
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        for (int i = 0; i < videoUrl.size(); i++) {
            File file = videoUrl.get(i);
            //必须加filename字段
            map.put("video\"; filename=\"" + file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));

        }
        MultipartBody.Part desc =
                MultipartBody.Part.createFormData("desc", trim);
        add(mService.subVideo(map, desc)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
                        mView.addVideoSuccess(response.getMsg());
                        dismissLoading(mContext);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
    }

    @Override
    public void getVideoCanTime() {
        add(mService.getVideoCanTime()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<CanTimeVideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<CanTimeVideoEntity> response) {
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
