package com.pro.maluli.module.home.oneToOne.answerPhone;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.builder.InviteParamBuilder;
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelFullInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.home.oneToOne.answerPhone.presenter.AnswerPhonePresenter;
import com.pro.maluli.module.home.oneToOne.answerPhone.presenter.IAnswerPhoneContraction;
import com.pro.maluli.module.home.startLive.StartLiveAct;
import com.pro.maluli.module.main.base.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class AnswerPhoneAct extends BaseMvpActivity<IAnswerPhoneContraction.View, AnswerPhonePresenter> implements IAnswerPhoneContraction.View {
    @BindView(R.id.avatarCiv)
    CircleImageView avatarCiv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.classifyTv)
    TextView classifyTv;
    @BindView(R.id.ivReject)
    LinearLayout ivReject;
    @BindView(R.id.ivAccept)
    LinearLayout ivAccept;
    /**
     * 播放铃声
     */
    MediaPlayer mMediaPlayer;
    private InvitedEvent invitedEvent;
    private String roomId, avatar, userName, liveId, liveClassify;

//    Observer<ChannelCommonEvent> observer = new Observer<ChannelCommonEvent>() {
//        @Override
//        public void onEvent(ChannelCommonEvent channelCommonEvent) {
//            SignallingEventType eventType = channelCommonEvent.getEventType();
//            Logger.d("AnswerPhoneAct onEvent: " + eventType.toString());
//            if (eventType == SignallingEventType.CANCEL_INVITE) {
//                AnswerPhoneAct.this.finish();
//            }
//        }
//    };

    @Override
    public AnswerPhonePresenter initPresenter() {
        return new AnswerPhonePresenter(this);
    }

    @Override
    public void baseInitialization() {
        Logger.d("baseInitialization");
        BarUtils.setStatusBarColor(this, Color.parseColor("#201D20"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
//        StatusBarUtils.SetStatusBarLightMode(this, true);
        invitedEvent = (InvitedEvent) getIntent().getSerializableExtra("LIVE_INFO");
//        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(observer, true);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_answer_phone;
    }

    @Override
    public void viewInitialization() {
//        nertcCallingDelegate =
        if (invitedEvent != null) {
            JSONObject json = JSONObject.parseObject(invitedEvent.getCustomInfo());
            roomId = json.getString("roomId");
            avatar = json.getString("avatar");
            userName = json.getString("userName");
            liveId = json.getString("liveId");
            liveClassify = json.getString("liveClassify");
            classifyTv.setText(liveClassify);
            GlideUtils.loadImage(AnswerPhoneAct.this, avatar, avatarCiv);
            userNameTv.setText(userName);
        }
        try {
            PlayRingTone(this, RingtoneManager.TYPE_RINGTONE);
//            defaultCallMediaPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        MainActivity.hasShowAnswerPage = false;
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 播放系统默认来电铃声
     */
    public void defaultCallMediaPlayer() throws Exception {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }

    /**
     * 获取的是铃声的Uri
     */
    public Uri getDefaultRingtoneUri(Context ctx, int type) {
        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);
    }

    public void PlayRingTone(Context ctx, int type) {
        mMediaPlayer = MediaPlayer.create(ctx, getDefaultRingtoneUri(ctx, type));
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    /**
     * 接受对方的的邀请并加入频道
     */
    private void acceptInvite() {
        enableClick(false);
        InviteParamBuilder inviteParam = new InviteParamBuilder(invitedEvent.getChannelBaseInfo().getChannelId(), invitedEvent.getFromAccountId(), invitedEvent.getRequestId());
        NIMClient.getService(SignallingService.class).acceptInviteAndJoin(inviteParam, 0).setCallback(new RequestCallbackWrapper<ChannelFullInfo>() {
            @Override
            public void onResult(int code, ChannelFullInfo channelFullInfo, Throwable throwable) {
                enableClick(true);                    //参考官方文档中关于api以及错误码的说明
                if (code == ResponseCode.RES_SUCCESS) {
//                    channelInfo = channelFullInfo.getChannelBaseInfo();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isAnchor", false);
                    bundle.putString("liveId", liveId);
                    gotoActivity(StartLiveAct.class, true, bundle);
                    releaseMediaPlayer();
                    leave();
                } else {
                    ToastHelper.showToast(AnswerPhoneAct.this, "接收邀请返回的结果 ， code = " + code + (throwable == null ? "" : ", throwable = " + throwable.getMessage()));
                }
                finish();
            }
        });
    }

    /**
     * 离开频道,方便再次呼叫，防止返回10407，对方已经频道内
     */
    private void leave() {
        NIMClient.getService(SignallingService.class).leave(invitedEvent.getChannelBaseInfo().getChannelId(), false, null).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int i, Void aVoid, Throwable throwable) {
            }
        });
    }

    /**
     * 拒绝对方的邀请
     */
    private void rejectInvite(String customInfo) {
        InviteParamBuilder inviteParam = new InviteParamBuilder(invitedEvent.getChannelBaseInfo().getChannelId(), invitedEvent.getFromAccountId(), invitedEvent.getRequestId());
        if (!TextUtils.isEmpty(customInfo)) {
            inviteParam.customInfo(customInfo);
        }
        NIMClient.getService(SignallingService.class).rejectInvite(inviteParam);
        releaseMediaPlayer();
        finish();
    }

    /**
     * 操作过程中是否能点击
     *
     * @param b
     */
    private void enableClick(boolean b) {

    }

    @Override
    public void doBusiness() {

    }


    @OnClick({R.id.ivReject, R.id.ivAccept, R.id.jumpLL})
    public void onClick(View view) {
        if (!ToolUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ivReject://拒绝
                rejectInvite("拒绝");
                break;
            case R.id.ivAccept://接受
                acceptInvite();
                break;
            case R.id.jumpLL://接受
                rejectInvite("跳过");
                break;
        }
    }

}