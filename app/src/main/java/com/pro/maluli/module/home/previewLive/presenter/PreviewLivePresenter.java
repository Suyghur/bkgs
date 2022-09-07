package com.pro.maluli.module.home.previewLive.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.LivePayInfoEntity;
import com.pro.maluli.common.networkRequest.ResponseCode;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class PreviewLivePresenter extends BasePresenter<IPreviewLiveContraction.View> implements IPreviewLiveContraction.Presenter {
    public int page = 1;
    public String anchorId;

    public PreviewLivePresenter(Context context) {
        super(context);
    }

    @Override
    public void getAnchorInfo(String anchorID) {
        add(mService.getAnchorInformation(anchorID)
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
    public void anchorSub() {
        add(mService.removeLike(anchorId)
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
    public void joinLive(String roomId) {
        add(mService.joinLive(roomId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        JSONObject jsonObject = JSONObject.parseObject(myJson);
                        String data = jsonObject.getString("data");
                        if (response.getCode() == ResponseCode.SUCCESS) {
//                            JoinLiveEntity entity = JSONObject.parseObject(data, JoinLiveEntity.class);
                            mView.setJoinLiveSuccess(jsonObject);
                        } else if (response.getCode() == ResponseCode.PAY_LIVE) {
                            LivePayInfoEntity entity = JSONObject.parseObject(data, LivePayInfoEntity.class);
                            mView.setGotoPay(entity);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void payInLive(int id) {
        add(mService.payGoInLive(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        JSONObject jsonObject = JSONObject.parseObject(myJson);
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = JSONObject.parseObject(data);
                        if (response.getCode() == 200) {
                            joinLive(jsonObject2.getString("room_id"));
                        } else if (response.getCode() == 1005) {//百科币余额不足
                            mView.setNoMoney();
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
