package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditImgPresenter extends BasePresenter<IEditImgContraction.View> implements IEditImgContraction.Presenter {
    public int page = 1;

    public EditImgPresenter(Context context) {
        super(context);
    }

    @Override
    public void getImg() {
        add(mService.getAnchorImg()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorImgEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorImgEntity> response) {
                        mView.setImageSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void deleteImage(int id) {
        add(mService.getDeleteImg(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        mView.setDeleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subMitImg(String url,String mark) {
        add(mService.addAnchorImg(url,mark)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        getImg();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subSortImg(String sortImg) {
        add(mService.sortAnchorImg(sortImg)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        getImg();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
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
}
