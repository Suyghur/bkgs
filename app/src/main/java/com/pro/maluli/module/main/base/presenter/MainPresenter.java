package com.pro.maluli.module.main.base.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.netease.nim.uikit.common.ToastHelper;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.ACache;

import io.reactivex.functions.Consumer;

public class MainPresenter extends BasePresenter<IMainContraction.View> implements IMainContraction.Presenter {
    public MainPresenter(Context context) {
        super(context);
    }

    @Override
    public void getUserInfo() {
        add(mService.getUserInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<UserInfoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfoEntity> response) {
                        ACache.get(mContext).put(ACEConstant.USERINFO, response.getData());
                        mView.setUserInfoSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    /**
     * 获取私信未读数据
     */
    @Override
    public void getMessageList() {
        add(mService.getMessageList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageListEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageListEntity> response) {
                        mView.setMessageSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void checkISAnchor(String accid) {
        add(mService.checkIsAnchor(accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        JSONObject jsonObject = JSONObject.parseObject(myJson);
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = JSONObject.parseObject(data);
                        String anchorId = jsonObject2.getString("anchor_id");
                        mView.setGoAnchorInfo(anchorId);
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
    public void getProtocoList(String id) {
        add(mService.getProtocoDetail(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ProtocolDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ProtocolDetailEntity> response) {
                        mView.setProtocolDetail(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void RealConceal() {
        add(mService.readConceal()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ProtocolDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ProtocolDetailEntity> response) {
                        mView.setSubRealConceal();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void agreeNotice(int id) {
        add(mService.readNotic(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setNoticeSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void sendGift(int giftId, String accid) {
        add(mService.sendGiftforSX(String.valueOf(giftId), accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<ProtocolDetailEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<ProtocolDetailEntity> response) {
                        if (response.getCode() == 1005) {//百科币不足请
                            mView.setGotoRechargeAct();
                            return;
                        }
                        mView.sendGiftSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    /**
     * 获取用户收到的礼物列表
     *
     * @param accid
     */
    @Override
    public void getGiftgiveList(String accid) {
        add(mService.getGiftListForMe(accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<GiftForMeEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<GiftForMeEntity> response) {
//                        mView.sendGiftSuccess();
                        mView.getForMeGiftListSuccess(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subMitReCallLive(String link) {
        add(mService.reOrder(link)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.reOrderSuccess(response.getMsg());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getMessageCanScore(String accid) {
        add(mService.getMessageScore(accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageCanScoreEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageCanScoreEntity> response) {
                        mView.setMessageCanScore(response.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    /**
     * 发送评价接口
     *
     * @param accid
     */
    @Override
    public void sendMessageForGift(String accid) {
        add(mService.sendScoreForGz(accid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageCanScoreEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageCanScoreEntity> response) {
//                        mView.setMessageCanScore(response.getData());
                        ToastHelper.showToast(mContext, response.getMsg());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void sendScoreForAnchor(String accid, int ablitityNumber, int serviceNumber) {
        add(mService.sendevaluationForAnchor(accid, String.valueOf(serviceNumber), String.valueOf(ablitityNumber))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<MessageCanScoreEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<MessageCanScoreEntity> response) {
//                        mView.setMessageCanScore(response.getData());
                        ToastHelper.showToast(mContext, response.getMsg());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getTeenager() {
        add(mService.getYouthModelInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<YouthEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<YouthEntity> response) {
                        dismissLoading(mContext);
                        mView.setYouthSuccess(response.getData());
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
