package com.pro.maluli.module.home.startLive.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.ShareLiveEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class StartLivePresenter extends BasePresenter<IStartLiveContraction.View> implements IStartLiveContraction.Presenter {
    public int page = 1;
    public String liveId;
    public String zhuboId;
    public String liveBgShareImg;
    public GiftForMeEntity giftForMeEntity;

    public StartLivePresenter(Context context) {
        super(context);
    }

    @Override
    public void anchorSub() {
        add(mService.removeLike(zhuboId)
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
    public void getSeeLiveUserInfo() {
        add(mService.getNestLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SeeLiveUserEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SeeLiveUserEntity> response) {
                        mView.setSeeLiveInfo(response.getData());
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
    public void closeLive() {
        add(mService.getCloseLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.closeLiveSuccess();
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

    //    live/set_room_tim
    @Override
    public void setLiveTime(String time) {
        add(mService.setOTOLiveTime(time)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setTimeSucess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void addLiveTime(String time) {
        add(mService.addOTOLiveTime(time)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.addTimeSucess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void endLive() {
        add(mService.endOTOLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.endLiveSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void jumpLive() {
        add(mService.jumpOTOLive()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.jumpLiveSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getGoingLive() {
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
    public void subScoreToLive(String abilityNumber, String srviceNumber) {
        add(mService.oneToOneScore(liveId, abilityNumber, srviceNumber)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<JoinLiveEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<JoinLiveEntity> response) {
//                        mView.setJoinLiveSuccess(response.getData());
                        ToastUtils.showShort(response.getMsg());
                        mView.finishAct();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void subGuest(String id) {
        add(mService.callGuestList(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SeeLiveUserEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SeeLiveUserEntity> response) {
                        mView.setSeeLiveInfo(response.getData());
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
//                        mView.setSeeLiveInfo(response.getData());
                        liveBgShareImg = response.getData().getInfo().getLogo();
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
                        mView.setGiftForMe(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
