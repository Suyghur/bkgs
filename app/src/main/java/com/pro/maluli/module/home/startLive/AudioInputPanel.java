package com.pro.maluli.module.home.startLive;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.myCustom.extension.LianmaiAttachment;
import com.netease.nim.uikit.business.session.myCustom.extension.RedPacketAttachment;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;

import java.io.File;

public class AudioInputPanel implements IAudioRecordCallback {


    protected Container container;
    protected Handler uiHandler;

    protected LinearLayout messageActivityBottomLayout;
    protected Button audioRecordBtn; // 录音按钮
    protected View audioAnimLayout; // 录音动画布局
    protected View switchToAudioButtonInInputBar;// 语音消息选择按钮

    // 语音
    protected AudioRecorder audioMessageHelper;
    private Chronometer time;
    private TextView timerTip;
    private LinearLayout timerTipContainer;
    private boolean started = false;
    private boolean cancelled = false;
    private boolean touched = false; // 是否按着
    private IAudioRecrod audioRecrodCallback = null;

    private IMMessage replyMessage = null;

    public AudioInputPanel(Container container, View audioAnimLayout, LinearLayout timerTipContainer, Chronometer time, TextView timer_tip, Button audioRecordBtn) {
        this.container = container;
        this.audioAnimLayout = audioAnimLayout;
        this.time = time;
        this.timerTipContainer = timerTipContainer;
        this.timerTip = timer_tip;
        this.audioRecordBtn = audioRecordBtn;
        this.uiHandler = new Handler();
        init();
    }

    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40;
    }

    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    public void onDestroy() {
        // release
        if (audioMessageHelper != null) {
            audioMessageHelper.destroyAudioRecorder();
        }
    }

    private void init() {
        initAudioRecordButton();
    }


    public void setAudioRecordCallback(IAudioRecrod callback) {
        this.audioRecrodCallback = callback;
    }

    /**
     * 连麦IM
     */
    public void lianMai(String type) {
        LianmaiAttachment lianmaiAttachment = new LianmaiAttachment();
        lianmaiAttachment.setPlayingStatus(type);
        IMMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(container.account, lianmaiAttachment);
        container.proxy.sendMessage(message);
    }

    /**
     * 送礼物
     */
    public void sendGift(GiftEntity.ListBean entity) {
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
        attachment.setGift_id(String.valueOf(entity.getId()));
        attachment.setGift_logo(entity.getLogo());
        attachment.setGift_name(entity.getTitle());
        IMMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(container.account, attachment);
        container.proxy.sendMessage(message);
    }

    public IMMessage getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(IMMessage replyMessage) {
        this.replyMessage = replyMessage;
    }

    public void resetReplyMessage() {
        setReplyMessage(null);
    }

    public void reload(Container container, SessionCustomization customization) {
        this.container = container;
    }

    /**
     * ************************* 键盘布局切换 *******************************
     */

    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }

    /**
     * ****************************** 语音 ***********************************
     */
    private void initAudioRecordButton() {
        audioRecordBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ToastUtils.showShort("长按录音");
                    touched = true;
                    initAudioRecord();
                    onStartAudioRecord();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    touched = false;
                    onEndAudioRecord(isCancelled(v, event));
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    touched = true;
                    cancelAudioRecord(isCancelled(v, event));
                }
                return false;
            }
        });
    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            UIKitOptions options = NimUIKitImpl.getOptions();
            audioMessageHelper = new AudioRecorder(container.activity, options.audioRecordType, options.audioRecordMaxTime, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        container.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioMessageHelper.startRecord();
        if (audioRecrodCallback != null) {
            audioRecrodCallback.onStart();
        }
        cancelled = false;
    }

    /**
     * 结束语音录制
     */
    private void onEndAudioRecord(boolean cancel) {
        started = false;
        container.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioMessageHelper.completeRecord(cancel);
        if (audioRecrodCallback != null) {
            audioRecrodCallback.onEnd();
        }
        stopAudioRecordAnim();
    }

    /**
     * 取消语音录制
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel);
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private void updateTimerTip(boolean cancel) {
        if (cancel) {
            timerTip.setText(R.string.recording_cancel_tip);
            timerTipContainer.setBackgroundResource(R.drawable.nim_cancel_record_red_bg);
        } else {
            timerTip.setText(R.string.recording_cancel);
            timerTipContainer.setBackgroundResource(0);
        }
    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.VISIBLE);
        time.setBase(SystemClock.elapsedRealtime());
        time.start();
    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.GONE);
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());
    }

    // 录音状态回调
    @Override
    public void onRecordReady() {

    }

    @Override
    public void onRecordStart(File audioFile, RecordType recordType) {
        started = true;
        if (!touched) {
            return;
        }
//        audioRecordBtn.setBackgroundResource(R.drawable.nim_message_input_edittext_box_pressed);

        updateTimerTip(false); // 初始化语音动画状态
        playAudioRecordAnim();
    }

    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
//        IMMessage audioMessage = MessageBuilder.createAudioMessage(container.account, container.sessionType, audioFile, audioLength);
        IMMessage audioMessage = ChatRoomMessageBuilder.createChatRoomAudioMessage(container.account, audioFile, audioLength);
        container.proxy.sendMessage(audioMessage);
    }

    @Override
    public void onRecordFail() {
        if (started) {
            ToastHelper.showToast(container.activity, R.string.recording_error);
        }
    }

    @Override
    public void onRecordCancel() {

    }

    @Override
    public void onRecordReachedMaxTime(final int maxTime) {
        stopAudioRecordAnim();
        EasyAlertDialogHelper.createOkCancelDiolag(container.activity, "", container.activity.getString(R.string.recording_max_time), false, new EasyAlertDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {
            }

            @Override
            public void doOkAction() {
                audioMessageHelper.handleEndRecord(true, maxTime);
            }
        }).show();
    }

    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }


    public interface IAudioRecrod {
        void onStart();

        void onEnd();
    }

}