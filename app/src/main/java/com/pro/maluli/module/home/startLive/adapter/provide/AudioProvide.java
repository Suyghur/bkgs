package com.pro.maluli.module.home.startLive.adapter.provide;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.common.media.audioplayer.Playable;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;

import org.jetbrains.annotations.NotNull;

public class AudioProvide extends BaseItemProvider<ChatRoomMessage> {
    public static final int CLICK_TO_PLAY_AUDIO_DELAY = 500;
    // data
    protected ChatRoomMessage message;
    private TextView durationLabel;
    private View containerView;
    private View unreadIndicator;
    private ImageView animationView;
    private MessageAudioControl audioControl;
    private MessageAudioControl.AudioControlListener onPlayListener = new MessageAudioControl.AudioControlListener() {

        @Override
        public void updatePlayingProgress(Playable playable, long curPosition) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            if (curPosition > playable.getDuration()) {
                return;
            }
            updateTime(curPosition);
        }

        @Override
        public void onAudioControllerReady(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            play();
        }

        @Override
        public void onEndPlay(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            updateTime(playable.getDuration());

            stop();
        }


    };

    public static int getAudioMaxEdge() {
        return (int) (0.6 * ScreenUtil.screenMin);
    }

    public static int getAudioMinEdge() {
        return (int) (0.1875 * ScreenUtil.screenMin);
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, ChatRoomMessage data, int position) {
        super.onClick(helper, view, data, position);
        message = data;
        finview(helper);
        if (audioControl != null) {
            if (message.getDirect() == MsgDirectionEnum.In && message.getAttachStatus() != AttachStatusEnum.transferred) {
                if (message.getAttachStatus() == AttachStatusEnum.fail || message.getAttachStatus() == AttachStatusEnum.def) {
                    NIMClient.getService(MsgService.class).downloadAttachment(message, false);
                }
                return;
            }

            if (message.getStatus() != MsgStatusEnum.read) {
                // 将未读标识去掉,更新数据库
                unreadIndicator.setVisibility(View.GONE);
            }

            initPlayAnim(); // 设置语音播放动画
            NimUIKit.setEarPhoneModeEnable(false);
            audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, message, onPlayListener);

            audioControl.setPlayNext(!NimUIKitImpl.getOptions().disableAutoPlayNextAudio, null, message);
        }
    }

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.message_item_audio;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
        finview(baseViewHolder);


        HeadImageView circleImageView = baseViewHolder.getView(R.id.headCiv);
        String avatar = ChatRoomViewHolderHelper.getAvatar(chatRoomMessage);
        circleImageView.loadAvatar(chatRoomMessage.getSessionId(), avatar);
        baseViewHolder.setText(R.id.nameTv, ChatRoomViewHolderHelper.getNameText(chatRoomMessage));

        animationView.setBackgroundResource(0);
        audioControl = MessageAudioControl.getInstance(context);
        this.message = chatRoomMessage;

//        baseViewHolder.getView(R.id.message_item_audio_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (audioControl != null) {
//                    if (message.getDirect() == MsgDirectionEnum.In && message.getAttachStatus() != AttachStatusEnum.transferred) {
//                        if (message.getAttachStatus() == AttachStatusEnum.fail || message.getAttachStatus() == AttachStatusEnum.def) {
//                            NIMClient.getService(MsgService.class).downloadAttachment(message, false);
//                        }
//                        return;
//                    }
//
//                    if (message.getStatus() != MsgStatusEnum.read) {
//                        // 将未读标识去掉,更新数据库
//                        unreadIndicator.setVisibility(View.GONE);
//                    }
//
//                    initPlayAnim(); // 设置语音播放动画
//                    NimUIKit.setEarPhoneModeEnable(false);
//                    audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, message, onPlayListener);
//
//                    audioControl.setPlayNext(!NimUIKitImpl.getOptions().disableAutoPlayNextAudio, null, message);
//                }
//            }
//        });
        layoutByDirection();

        refreshStatus();

        controlPlaying();

    }

    private void finview(@NotNull BaseViewHolder baseViewHolder) {
        durationLabel = baseViewHolder.getView(R.id.message_item_audio_duration);
        containerView = baseViewHolder.getView(R.id.message_item_audio_container);
        unreadIndicator = baseViewHolder.getView(R.id.message_item_audio_unread_indicator);
        animationView = baseViewHolder.getView(R.id.message_item_audio_playing_animation);
    }

    // 判断消息方向，是否是接收到的消息
    protected boolean isReceivedMessage() {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    private void layoutByDirection() {
//        if (isReceivedMessage()) {
        setGravity(animationView, Gravity.LEFT | Gravity.CENTER_VERTICAL);
        setGravity(durationLabel, Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        containerView.setBackgroundResource(R.drawable.shape_20000000_4);
        containerView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_left);
        durationLabel.setTextColor(Color.BLACK);

//        } else {
//            setGravity(animationView, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//            setGravity(durationLabel, Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            unreadIndicator.setVisibility(View.GONE);
//
//            containerView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightBackground);
//            containerView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
//            animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_right);
//            durationLabel.setTextColor(Color.WHITE);
//        }
    }

    private void refreshStatus() {// 消息状态
        AudioAttachment attachment = (AudioAttachment) message.getAttachment();
        MsgStatusEnum status = message.getStatus();
        AttachStatusEnum attachStatus = message.getAttachStatus();

        // alert button
//        if (TextUtils.isEmpty(attachment.getPath())) {
//            if (attachStatus == AttachStatusEnum.fail || status == MsgStatusEnum.fail) {
//                alertButton.setVisibility(View.VISIBLE);
//            } else {
//                alertButton.setVisibility(View.GONE);
//            }
//        }

        // progress bar indicator
//        if (status == MsgStatusEnum.sending || attachStatus == AttachStatusEnum.transferring) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }

        // unread indicator
        if (!NimUIKitImpl.getOptions().disableAudioPlayedStatusIcon
                && isReceivedMessage()
                && attachStatus == AttachStatusEnum.transferred
                && status != MsgStatusEnum.read) {
            unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            unreadIndicator.setVisibility(View.GONE);
        }
    }

    private void controlPlaying() {
        final AudioAttachment msgAttachment = (AudioAttachment) message.getAttachment();
        long duration = msgAttachment.getDuration();
        setAudioBubbleWidth(duration);

        durationLabel.setTag(message.getUuid());

        if (!isMessagePlaying(audioControl, message)) {
            if (audioControl.getAudioControlListener() != null &&
                    audioControl.getAudioControlListener().equals(onPlayListener)) {
                audioControl.changeAudioControlListener(null);
            }

            updateTime(duration);
            stop();
        } else {
            audioControl.changeAudioControlListener(onPlayListener);
            play();
        }
    }

    private void setAudioBubbleWidth(long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);

        int currentBubbleWidth = calculateBubbleWidth(seconds, NimUIKitImpl.getOptions().audioRecordMaxTime);
        ViewGroup.LayoutParams layoutParams = containerView.getLayoutParams();
        layoutParams.width = currentBubbleWidth;
        containerView.setLayoutParams(layoutParams);
    }

    private int calculateBubbleWidth(long seconds, int MAX_TIME) {
        int maxAudioBubbleWidth = getAudioMaxEdge();
        int minAudioBubbleWidth = getAudioMinEdge();

        int currentBubbleWidth;
        if (seconds <= 0) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (seconds > 0 && seconds <= MAX_TIME) {
            currentBubbleWidth = (int) ((maxAudioBubbleWidth - minAudioBubbleWidth) * (2.0 / Math.PI)
                    * Math.atan(seconds / 10.0) + minAudioBubbleWidth);
        } else {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        if (currentBubbleWidth < minAudioBubbleWidth) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (currentBubbleWidth > maxAudioBubbleWidth) {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        return currentBubbleWidth;
    }

    private void updateTime(long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);

        if (seconds >= 0) {
            durationLabel.setText(seconds + "\"");
        } else {
            durationLabel.setText("");
        }
    }

    protected boolean isMessagePlaying(MessageAudioControl audioControl, IMMessage message) {
        if (audioControl.getPlayingAudio() != null && audioControl.getPlayingAudio().isTheSame(message)) {
            return true;
        } else {
            return false;
        }
    }

    private void play() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.start();
        }
    }

    private void stop() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.stop();

            endPlayAnim();
        }
    }

    private void initPlayAnim() {
//        if (isReceivedMessage()) {
        animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_left);
//        } else {
//            animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_right);
//        }
    }

    private void endPlayAnim() {
//        if (isReceivedMessage()) {
        animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_left_3);
//        } else {
//            animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_right_3);
//        }
    }

    private boolean isTheSame(String uuid) {
        String current = durationLabel.getTag().toString();
        return !TextUtils.isEmpty(uuid) && uuid.equals(current);
    }

}