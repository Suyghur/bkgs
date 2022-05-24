package com.pro.maluli.module.home.base.fragment.rechild.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.util.List;

import io.reactivex.functions.Consumer;

public class ChildLiveListPresenter extends BasePresenter<IChildLiveListContraction.View> implements IChildLiveListContraction.Presenter {
    public int page=1;

    public ChildLiveListPresenter(Context context) {
        super(context);
    }

    //List<LiveListEntity>
    @Override
    public void getHomeLiveList(String id) {
        add(mService.getHomeLiveList(id, String.valueOf(page), "10")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        LiveListEntity entity = JSONObject.parseObject(myJson, LiveListEntity.class);
                        mView.setHomeLiveListData(entity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
