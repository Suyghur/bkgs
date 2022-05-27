package com.pro.maluli.module.home.oneToMore.base.oneToMore;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OneToMorePresenter extends BasePresenter<IOneToMoreContraction.View> implements IOneToMoreContraction.Presenter {
    public int model = 0;

    public OneToMorePresenter(Context context) {
        super(context);
    }


    @Override
    public void startLive(String type, String title, String imgUrl, String money) {
        add(mService.startLive(type, title, imgUrl, String.valueOf(model), money, "")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<OneToOneEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<OneToOneEntity> response) {
                        dismissLoading(mContext);
                        mView.setStartInfo(response.getData());

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

    @Override
    public void getLastTimeLive() {
        add(mService.getLastTimeLive("2")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<LastTimeLiveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<LastTimeLiveEntity> response) {
                        response.getData();
//                        mView.userInfo();
//                        mView.setLastTimeLiveInfo(response.getData());
                        mView.setLastLive(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
