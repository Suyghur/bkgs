package com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.ShareLiveEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class StartOneToMoreLivePresenter extends BasePresenter<IStartOneToMoreLiveContraction.View> implements IStartOneToMoreLiveContraction.Presenter {
    public String id;
    public String liveId;
    public String ancharId;
    public String liveBgShareImg;
    public GiftForMeEntity giftForMeEntity;

    public StartOneToMoreLivePresenter(Context context) {
        super(context);
    }

    @Override
    public void anchorSub() {
        add(mService.removeLike(ancharId)
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
    public void getShareImg() {
        add(mService.shareImgForOtOLive(liveId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ShareLiveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ShareLiveEntity> response) {
                        liveBgShareImg = response.getData().getInfo().getLogo();
                        mView.setShareImgSuccess(response.getData().getInfo().getLogo());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void closeLive() {
        add(mService.getCloseLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setfinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getliveInfo() {
        add(mService.joinLive(liveId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        JSONObject jsonObject = JSONObject.parseObject(myJson);
                        String data = jsonObject.getString("data");
                        JoinLiveEntity entity = JSONObject.parseObject(data, JoinLiveEntity.class);
                        mView.setJoinLiveSuccess(entity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getGiftInfo() {
        add(mService.getGiftInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<GiftEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<GiftEntity> response) {
                        mView.setGiftInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getAnchorInfo() {
        add(mService.getAnchorInformation(ancharId)
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
    public void sendGift(GiftEntity.ListBean giftId) {
        add(mService.sendGift(String.valueOf(liveId), String.valueOf(giftId.getId()))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        if (response.getCode() == 200) {
                            mView.sendGiftSuccess(giftId);
                        } else if (response.getCode() == 1005) {
                            mView.setNomomey();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 获得打赏列表
     */
    @Override
    public void getGiftForMe() {
        add(mService.getGiftforMeLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<GiftForMeEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<GiftForMeEntity> response) {
//                        mView.sendGiftSuccess(giftId);
                        giftForMeEntity = response.getData();
                        mView.giftFormeSuccess(giftForMeEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
