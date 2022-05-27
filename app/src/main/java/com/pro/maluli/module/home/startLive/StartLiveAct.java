package com.pro.maluli.module.home.startLive;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.netease.lava.nertc.sdk.NERtcCallbackEx;
import com.netease.lava.nertc.sdk.NERtcConstants;
import com.netease.lava.nertc.sdk.NERtcEx;
import com.netease.lava.nertc.sdk.NERtcParameters;
import com.netease.lava.nertc.sdk.stats.NERtcAudioVolumeInfo;
import com.netease.lava.nertc.sdk.video.NERtcRemoteVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.chatroom.fragment.ChatRoomMessageFragment;
import com.netease.nim.uikit.business.chatroom.helper.ChatRoomHelper;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.dialogFragment.gift.GiftForMeDialog;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.myCustom.extension.CustomAttachmentType;
import com.netease.nim.uikit.business.session.myCustom.extension.SystemAttachment;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.log.sdk.wrapper.NimLog;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.SignallingServiceObserver;
import com.netease.nimlib.sdk.avsignalling.builder.InviteParamBuilder;
import com.netease.nimlib.sdk.avsignalling.constant.ChannelType;
import com.netease.nimlib.sdk.avsignalling.constant.SignallingEventType;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo;
import com.netease.nimlib.sdk.avsignalling.model.ChannelFullInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.CdnRequestData;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.base.activityManager.ActivityTaskManager;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.constant.AppIdConstants;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.Url;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.CanFinishDialog;
import com.pro.maluli.common.view.dialogview.EditLiveTimeDialog;
import com.pro.maluli.common.view.dialogview.GuestListDialog;
import com.pro.maluli.common.view.dialogview.InputTextLiveDialog;
import com.pro.maluli.common.view.dialogview.LiveAssessDialog2;
import com.pro.maluli.common.view.dialogview.LiveMoreDialog;
import com.pro.maluli.common.view.dialogview.ShareAppDialog;
import com.pro.maluli.common.view.dialogview.checkMsg.CheckMsgDialog;
import com.pro.maluli.common.view.dialogview.gift.GiftDialog;
import com.pro.maluli.common.view.myselfView.MagicTextView;
import com.pro.maluli.common.view.slideView.ISlideListener;
import com.pro.maluli.common.view.slideView.SlideLayout;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.chatRoom.entity.CustomizeInfoEntity;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.pushstream.PushStream;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.home.startLive.adapter.StartLiveAdapter;
import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service.FloatingViewService;
import com.pro.maluli.module.home.startLive.presenter.IStartLiveContraction;
import com.pro.maluli.module.home.startLive.presenter.StartLivePresenter;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;
import com.pro.maluli.module.socketService.SocketLiveUtils;
import com.pro.maluli.module.socketService.event.CallEvent;
import com.pro.maluli.module.socketService.event.OTOEvent;
import com.pro.maluli.module.socketService.event.OnTwoOneStartEntity;
import com.pro.maluli.module.video.fragment.recyclerUtils.SoftKeyBoardListener;
import com.pro.maluli.toolkit.ToastExtKt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class StartLiveAct extends BaseMvpActivity<IStartLiveContraction.View, StartLivePresenter> implements IStartLiveContraction.View, NERtcCallbackEx, ModuleProxy {
    @BindView(R.id.vv_local_user)
    NERtcVideoView vv_local_user;
    @BindView(R.id.vv_remote_user)
    NERtcVideoView vv_remote_user;
    @BindView(R.id.changeCameraIV)
    ImageView changeCameraIV;
    @BindView(R.id.clearScreenIV)
    TextView clearScreenIV;
    //    @BindView(R.id.action_real_layout)
//    RelativeLayout action_real_layout;
    @BindView(R.id.actionShowLayout)
    LinearLayout actionShowLayout;
    @BindView(R.id.comment_list)
    RecyclerView comment_list;
    @BindView(R.id.send)
    View send;
    //    @BindView(R.id.messageEditText)
//    EditText messageEditText;
    @BindView(R.id.sendAudioIv)
    Button sendAudioIv;
    /**
     * 语音发送
     */
    @BindView(R.id.layoutPlayAudio)
    FrameLayout layoutPlayAudio;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.timer_tip_container)
    LinearLayout timer_tip_container;
    @BindView(R.id.timer_tip)
    TextView timer_tip;
    @BindView(R.id.startNetTv)
    TextView startNetTv;
    @BindView(R.id.endliveTv)
    TextView endliveTv;


    @BindView(R.id.anchorAvaterCiv)
    CircleImageView anchorAvaterCiv;
    @BindView(R.id.anchorNameTv)
    TextView anchorNameTv;
    @BindView(R.id.liveIdTv)
    TextView liveIdTv;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.ReserveTv)
    TextView ReserveTv;
    @BindView(R.id.leveImg)
    ImageView leveImg;
    @BindView(R.id.carmerIv)
    ImageView carmerIv;
    @BindView(R.id.microphoneIv)
    ImageView microphoneIv;
    @BindView(R.id.teyaoNumberTv)
    TextView teyaoNumberTv;
    @BindView(R.id.liveTipsTv)
    TextView liveTipsTv;
    @BindView(R.id.slide_layout)
    SlideLayout slide_layout;

    @BindView(R.id.ll_gift_group)
    LinearLayout ll_gift_group;
    Container container;
    boolean isChange;//切换显示布局
    InputTextLiveDialog inputTextLiveDialog;
    /**
     * ---------------------------------------------------------------------------------------------
     */

    //要用Handler回到主线程操作UI，否则会报错
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ登陆
                case 0:
                    ToastUtils.showShort("分享失败");
                    break;
                //微信登录
                case 1:
                    ToastUtils.showShort("分享成功");
                    break;
            }
        }
    };
    int openStaus = 0;
    InnerReceiver innerReceiver;//监听home键
    ChannelBaseInfo channelInfo;
    Observer<CdnRequestData> cdnReqData = new Observer<CdnRequestData>() {
        @Override
        public void onEvent(CdnRequestData data) {
            if (data == null) {
                return;
            }
            NimLog.i("@CJL/cdn req data", String.format("reaDate=%s, failFinal=%s", data.getUrlReqData(), data.getFailFinal()));
        }
    };
    String accid;//信令通话ID
    CheckMsgDialog checkMsgDialog;
    /**
     * 邀请别人
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (startNetTv.getText().toString().equalsIgnoreCase("邀请中")) {
                ToastUtils.showShort("对方没有响应");
                startNetTv.setText("下一位");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
            }
        }
    };
    JoinLiveEntity joinLiveEntity;
    /**
     * 接收socket数据
     * <p>
     * <p>
     * "is_self": 0,是否主播本人 <number>
     * "is_sub": 1,是否已关注 <number>
     * "anchor_no": "012579",主播编号 <string>
     * "avatar": "https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20211011/5659146790bfc46e9b18102dd913c044.png",主播头像 <string>
     * "nickname": "zxjcvlkzxcv",主播昵称 <string>
     * "level": "https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20211011/a7e58fe0cb7b1a0b0edaddc8721b9c6b.png",主播等级 <string>
     * "appoint_num": 0,预约人数 <number>
     * "report_num": 10,接收预约人数 <number>
     * "now_time": 0,已播 <number>
     * "set_time": 0,设定本场直播时间 <number>
     * "type": 0-1-未开始直播 0-直播已结束 1-直播中 <number>
     *
     * @param tradeListEvent msg_id 0-推送失败 1-正常推送 2-设定时间 3-加时 4-开始 5-结束 6-评分
     */
    OnTwoOneStartEntity socketEntity;
    BaseTipsDialog liveEndDialog;
    boolean isShowEndliveDialog;
    boolean ishasPingfen;
    //    @BindView(R.id.clear_screen)
//    ClearScreenLayout clear_screen;
    private AudioInputPanel audioInputPanel;
    private String roomId, liveId;
    private Long uid;//自己uid
    private Long otherUid = 0l;//别人uid
    ServiceConnection mVideoServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatingViewService.MyBinder binder = (FloatingViewService.MyBinder) service;
            binder.getService();
            //这里测试 设置通话从10秒开始
            if (otherUid != null) {
                binder.setData(otherUid);
            } else {
                binder.setData(uid);
            }
            binder.getService().setCallback(new FloatingViewService.CallBack() {
                @Override
                public void onDataChanged(String data) {
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private boolean joinedChannel = false;
    private boolean enableLocalVideo = true;
    private boolean enableLocalAudio = true;
    private String WyToken = "";
    //    private PanelSwitchHelper mHelper;
    private StartLiveAdapter adapter;
    /**
     * 898
     * 子页面
     */
    private ChatRoomMessageFragment messageFragment;
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    private List<ChatRoomMessage> messageList;
    private SeeLiveUserEntity seeliveEntity;
    private boolean touched = false; // 是否按着
    private boolean isAnchor = true;//判断是否是主播,
    private GiftEntity giftEntity;
    /**
     * --------------------------------------礼物动画效果-------------------------------------------------------
     */
    private UserInfoEntity userInfoEntity;
    private boolean isLike;//是否关注
    private String nertc_token = "";
    private PushStream pushStream;
    private String push_url, channelName;
    /**
     * 刷礼物的方法
     */
    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private NumberAnim giftNumberAnim;
    //聊天信息回调liaotian11
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            // 处理新收到的消息
            if (messages == null || messages.isEmpty()) {
                return;
            }
//            ToastUtils.showShort("nihaoya");
            boolean needRefresh = false;
            for (ChatRoomMessage message : messages) {
                // 保证显示到界面上的消息，来自同一个聊天室
//                if (isMyMessage(message) && message.getMsgType() != MsgTypeEnum.notification) {
                if (isMyMessage(message)) {
                    if (message.getMsgType() == MsgTypeEnum.notification) {
                        ChatRoomNotificationAttachment attachment = (ChatRoomNotificationAttachment) message.getAttachment();
                        List<String> accounts = attachment.getTargets();
                        boolean isforme = false;
                        for (int i = 0; i < accounts.size(); i++) {
                            isforme = !NimUIKit.getAccount().equals(accounts.get(i));
                        }
                        if (isforme) {
                            messageList.add(message);
                            needRefresh = true;
                        }

                    } else {
                        try {
                            if (message.getMsgType() == MsgTypeEnum.custom) {
                                String allData = message.getAttachStr();
                                Logger.d(allData);
                                JSONObject jsonObjectTop = JSONObject.parseObject(allData);
                                int type = jsonObjectTop.getInteger("type");
                                try {
                                    switch (type) {
                                        case CustomAttachmentType.RedPacket:
                                            showGift(message);
                                            messageList.add(message);
                                            needRefresh = true;
                                            break;
                                        case CustomAttachmentType.SystemMsgOut:
                                            break;
                                        default:
                                            messageList.add(message);
                                            needRefresh = true;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (needRefresh) {
                adapter.setList(messageList);
                doScrollToBottom();
            }
//            messageListPanel.onIncomingMessage(messages);
        }
    };
    // 礼物
    private int[] GiftIcon = new int[]{R.drawable.down_icon};
    private boolean isSmall;
    private SoftKeyBoardListener mKeyBoardListener;
    /**
     * 信令回调
     */
    //收到的邀请参数,reject 用到
    private InvitedEvent invitedEvent;
    Observer<ChannelCommonEvent> nimOnlineObserver = new Observer<ChannelCommonEvent>() {
        @Override
        public void onEvent(ChannelCommonEvent channelCommonEvent) {
            SignallingEventType eventType = channelCommonEvent.getEventType();
            if (eventType == SignallingEventType.INVITE) {
                invitedEvent = (InvitedEvent) channelCommonEvent;

            }

        }
    };
    // -----------------------------------------------------------------悬浮窗————————————————————————————
//    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private boolean issubPremission;

    @Override
    public StartLivePresenter initPresenter() {
        return new StartLivePresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityTaskManager.getInstance().putSingleInstanceActivity(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.transparentStatusBar(this);
//        WindowSoftModeAdjustResizeExecutor.assistActivity(this);
        messageList = new ArrayList<>();
        EventBus.getDefault().register(this);
        liveId = getIntent().getStringExtra("liveId");
        presenter.liveId = liveId;
        //保持屏幕长时间亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isAnchor = getIntent().getBooleanExtra("isAnchor", true);
        WyToken = Preferences.getLoginInfo().getToken();
        userInfoEntity = (UserInfoEntity) ACache.get(this).getAsObject(ACEConstant.USERINFO);
        uid = getIntent().getLongExtra("UID", 0);
        if (userInfoEntity != null) {
            uid = Long.valueOf(userInfoEntity.getId());
        }

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_start_live;
    }

    @OnClick({R.id.changeCameraIV, R.id.finishIv, R.id.startInputTv, R.id.liveMoreIv, R.id.clearScreenIV,
            R.id.send, R.id.giftImg, R.id.startNetTv, R.id.attentionTv,
            R.id.endliveTv, R.id.carmerIv, R.id.microphoneIv, R.id.vv_local_user, R.id.shareIv, R.id.ReserveTv})
    public void onclick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.vv_local_user:
                //切换布局
                if (otherUid == 0l) {
                    return;
                }
                if (isChange) {
                    NERtcEx.getInstance().setupLocalVideoCanvas(vv_local_user);
                    NERtcEx.getInstance().setupRemoteVideoCanvas(vv_remote_user, otherUid);
                } else {
                    NERtcEx.getInstance().setupLocalVideoCanvas(vv_remote_user);
                    NERtcEx.getInstance().setupRemoteVideoCanvas(vv_local_user, otherUid);
                }
                isChange = !isChange;

                break;
            case R.id.clearScreenIV:
//                slide_layout.restoreContent();
//                slide_layout.setStateCoutorm(2);
//                slide_layout.openDrawer();
//                clearScreenIV.setVisibility(View.GONE);
                break;
            case R.id.changeCameraIV:
                //切换摄像头
                int i = NERtcEx.getInstance().switchCamera();
                break;
            case R.id.ReserveTv://跳转到一对一页面
                Bundle bundleQueue = new Bundle();
                bundleQueue.putBoolean("isStartLive", true);
                bundleQueue.putBoolean("isJump", true);
                bundleQueue.putString("ANCHOR_ID", presenter.zhuboId);
                gotoActivity(OneToOneQueueAct.class, false, bundleQueue);
                //切换摄像头
                break;
            case R.id.liveMoreIv://点击弹出更多
                LiveMoreDialog liveMoreDialog = new LiveMoreDialog();
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("ISANCHOR", isAnchor);
                liveMoreDialog.setArguments(bundle1);
                liveMoreDialog.show(getSupportFragmentManager(), "LiveMoreDialog");
                liveMoreDialog.setOnLiveControlListener(new LiveMoreDialog.OnLiveControlListener() {
                    @Override
                    public void operation(int type) {
                        switch (type) {//1举报 2投屏 3清屏 4分享
                            case 1:
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("SUB_TYPE", "2");
                                bundle2.putString("liveId", presenter.liveId);
                                gotoActivity(AppealAct.class, false, bundle2);
                                break;
                            case 2:
                                ToastUtils.showShort("此功能暂未开放");
                                break;
                            case 3:
                                clearScreenIV.setVisibility(View.VISIBLE);
//                                slide_layout.setStateCoutorm(3);
                                slide_layout.closeDrawer();
                                break;
                            case 4:
                                showShareImgDialog();
                                break;
                        }
                    }
                });
                break;
            case R.id.carmerIv:
                //关闭摄像头
                if (carmerIv.isSelected()) {
                    setLocalVideoEnable(true);
                    carmerIv.setSelected(false);
                } else {
                    carmerIv.setSelected(true);
                    setLocalVideoEnable(false);
                }
                break;
            case R.id.microphoneIv:
                //关闭麦克风
                if (microphoneIv.isSelected()) {
                    setLocalAudioEnable(true);
                    microphoneIv.setSelected(false);
                } else {
                    microphoneIv.setSelected(true);
                    setLocalAudioEnable(false);
                }
                break;
            case R.id.shareIv://微信分享
//                ToastUtils.showShort("此功能暂未开放！");
                showShareImgDialog();
                break;
            case R.id.finishIv://关闭
                showFinishAct();
//                finish();
                break;
            case R.id.endliveTv://关闭当前一对一
//                showFinishAct();
                if (endliveTv.getText().equals("结束")) {
                    BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
                    Bundle bundleEnd = new Bundle();
                    bundleEnd.putString("showContent", "是否确认结束当前一对一直播？");
                    bundleEnd.putString("TITLE_DIO", "温馨提示");
                    bundleEnd.putString("comfirm", "确认");
                    bundleEnd.putString("cancel", "取消");
                    baseTipsDialog.setArguments(bundleEnd);
                    baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            //结束当前直播
                            presenter.endLive();
                        }

                        @Override
                        public void cancel() {
                        }
                    });


                } else if (endliveTv.getText().equals("特邀")) {
                    showGuestDialog();
                }

                break;
            case R.id.attentionTv://关闭
//                showFinishAct();
                presenter.anchorSub();

                break;
//            case R.id.send:
//                onTextMessageSendButtonPressed();
//                break;
            case R.id.startNetTv://开始直播，或者下一位
//                onTextMessageSendButtonPressed();
                if (startNetTv.getText().equals("开始")) {
                    showSetTime("1");
                } else if (startNetTv.getText().equals("加时")) {
                    showSetTime("0");

                } else if (startNetTv.getText().equals("下一位")) {
                    presenter.getSeeLiveUserInfo();
                    isShowEndliveDialog = false;
                }
                break;

            case R.id.startInputTv:
                initInputTextMsgDialog();

//                action_real_layout.setVisibility(View.VISIBLE);
//                actionShowLayout.setVisibility(View.GONE);
//                messageEditText.requestFocus();
                break;
            case R.id.giftImg://礼物弹框

                if (isAnchor) {
                    presenter.getGiftForMe();
                    return;
                }
                presenter.getGiftInfo();

                break;
        }
    }

    private void initInputTextMsgDialog() {
        dismissInputDialog();
        if (inputTextLiveDialog == null) {
            inputTextLiveDialog = new InputTextLiveDialog(StartLiveAct.this, R.style.dialog);
            inputTextLiveDialog.setmOnTextSendListener(new InputTextLiveDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    if (TextUtils.isEmpty(msg)) {
                        ToastUtils.showShort("请输入你的评论");
                        return;
                    }
                    onTextMessageSendButtonPressed(msg);

                }

                @Override
                public void dismiss() {
                }
            });
        }
        showInputTextMsgDialog();
    }

    private void showInputTextMsgDialog() {
        inputTextLiveDialog.show();
    }

    private void dismissInputDialog() {
        if (inputTextLiveDialog != null) {
            if (inputTextLiveDialog.isShowing()) inputTextLiveDialog.dismiss();
            inputTextLiveDialog.cancel();
            inputTextLiveDialog = null;
        }
    }

    /**
     * 分享海报
     */
    private void showShareImgDialog() {
        //分享
        ShareAppDialog dialogFragment = new ShareAppDialog();
        Bundle bundle = new Bundle();
        bundle.putString("IMG", presenter.liveBgShareImg);
        dialogFragment.setArguments(bundle);
        dialogFragment.setCancelable(true);
        dialogFragment.show(getSupportFragmentManager(), "ForgetPwdDialog");
        dialogFragment.setOnShareAppListener(new ShareAppDialog.OnShareAppListener() {
            @Override
            public void gotoShare(int type) {
                // 1 QQ 2 微信 3朋友圈，4 QQ空间
                switch (type) {
                    case 1:
                        ToolUtils.shareImg(handler, QQ.NAME, presenter.liveBgShareImg);
                        break;
                    case 2:
                        ToolUtils.shareImg(handler, Wechat.NAME, presenter.liveBgShareImg);
                        break;
                    case 3:
                        ToolUtils.shareImg(handler, WechatMoments.NAME, presenter.liveBgShareImg);
                        break;
                    case 4:
                        ToolUtils.shareImg(handler, QZone.NAME, presenter.liveBgShareImg);
                        break;
                }

            }
        });
    }

    private void showGift(ChatRoomMessage tag) {
        String alldata = tag.getAttachment().toJson(false);
        CustomizeInfoEntity entity = new CustomizeInfoEntity();
        CustomizeInfoEntity entity1 = entity.goJsonYes(alldata);
        String tag2 = tag.getFromAccount() + entity1.getGift_id();
        View newGiftView = ll_gift_group.findViewWithTag(tag2);
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 3) {
                // 获取前2个元素的最后更新时间
                View giftView01 = ll_gift_group.getChildAt(0);
                ImageView iv_gift01 = giftView01.findViewById(R.id.iv_gift);
                long lastTime1 = (long) iv_gift01.getTag();

                View giftView02 = ll_gift_group.getChildAt(1);
                ImageView iv_gift02 = giftView02.findViewById(R.id.iv_gift);
                long lastTime2 = (long) iv_gift02.getTag();

                if (lastTime1 > lastTime2) { // 如果第二个View显示的时间比较长
                    removeGiftView(1);
                } else { // 如果第一个View显示的时间长
                    removeGiftView(0);
                }
            }

            // 获取礼物
            newGiftView = getNewGiftView(tag, entity1);
            ll_gift_group.addView(newGiftView);

            // 播放动画
            newGiftView.startAnimation(inAnim);
            final MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumberAnim.showAnimator(mtv_giftNum);
                }
            });
        } else {
            // 如果列表中已经有了该类型的礼物，则不再新建，直接拿出
            // 更新标识，记录最新修改的时间，用于回收判断
            ImageView iv_gift = newGiftView.findViewById(R.id.iv_gift);
            iv_gift.setTag(System.currentTimeMillis());

            // 更新标识，更新记录礼物个数
            MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增
            mtv_giftNum.setText("x" + giftCount);
            mtv_giftNum.setTag(giftCount);
            giftNumberAnim.showAnimator(mtv_giftNum);
        }
    }

    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(final int index) {
        // 移除列表，外加退出动画
        final View removeGiftView = ll_gift_group.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_gift_group.removeViewAt(index);
            }
        });

        // 开启动画，因为定时原因，所以可能是在子线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeGiftView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 获取礼物
     */
    private View getNewGiftView(ChatRoomMessage tag, CustomizeInfoEntity customizeInfoEntity) {

        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = LayoutInflater.from(StartLiveAct.this).inflate(R.layout.item_gift_for_live, null);
        giftView.setTag(tag.getFromAccount() + customizeInfoEntity.getGift_id());

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        ImageView iv_gift = giftView.findViewById(R.id.iv_gift);
        TextView nameTv = giftView.findViewById(R.id.nameTv);
        HeadImageView cv_send_gift_userIcon = giftView.findViewById(R.id.cv_send_gift_userIcon);
        iv_gift.setTag(System.currentTimeMillis());

        // 添加标识，记录礼物个数
        MagicTextView mtv_giftNum = giftView.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setTag(1);
        mtv_giftNum.setText("x1");

        String avatar = ChatRoomViewHolderHelper.getAvatar(tag);
        cv_send_gift_userIcon.loadAvatar(tag.getSessionId(), avatar);


        nameTv.setText(ChatRoomViewHolderHelper.getNameText(tag));
        GlideUtils.loadImageNoImage(StartLiveAct.this, customizeInfoEntity.getGift_logo(), iv_gift);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        giftView.setLayoutParams(lp);

        return giftView;
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {

                    View childView = ll_gift_group.getChildAt(i);
                    ImageView iv_gift = (ImageView) childView.findViewById(R.id.iv_gift);
                    long lastUpdateTime = (long) iv_gift.getTag();

                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        removeGiftView(i);
                    }
                }
            }
        }, 0, 3000);
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        giftNumberAnim = new NumberAnim(); // 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(StartLiveAct.this, R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(StartLiveAct.this, R.anim.gift_out); // 礼物退出时动画
    }

    /**
     * 特邀列表
     */
    private void showGuestDialog() {
        if (socketEntity.getSpecial_count() <= 0) {
            ToastUtils.showShort("没有特邀人员哦");
            return;
        }
        GuestListDialog guestListDialog = new GuestListDialog();
        Bundle bundle = new Bundle();
        if (socketEntity != null && socketEntity.getSpecial_list() != null) {
            bundle.putSerializable("Guest_list", (Serializable) socketEntity.getSpecial_list());
        }
        guestListDialog.setArguments(bundle);
        guestListDialog.show(getSupportFragmentManager(), "GuestListDialog");
        guestListDialog.setOnConfirmListener(new GuestListDialog.OnBaseTipsListener() {
            @Override
            public void comfirm(String id, String userName) {
//                BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("showContent", "是否确认特邀" + userName + "进入直播间");
//                bundle1.putString("TITLE_DIO", "温馨提示");
//                bundle1.putString("comfirm", "特邀");
//                bundle1.putString("cancel", "取消");
//                baseTipsDialog.setArguments(bundle1);
//                baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
//                baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
//                    @Override
//                    public void comfirm() {
//                        //获取特邀人员信息
//                        //再次邀请
//                        accid = data.getAppoint().getAccid();
//
//                        inviteOther(accid);
//                        if (data.getAppoint() != null) {
////            inviteOther(data.getAppoint().getAccid());
//                            checkMsgDialog = new CheckMsgDialog();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("Message", data.getAppoint());
//                            checkMsgDialog.setArguments(bundle);
//                            checkMsgDialog.show(getSupportFragmentManager(), "CheckMsgDialog");
//                        }
                presenter.subGuest(id);
//                    }
//
//                    @Override
//                    public void cancel() {
//                    }
//                });
            }
        });

    }

    /**
     * @param timeType 1=设置时间，2=增加时间
     */
    private void showSetTime(String timeType) {
        EditLiveTimeDialog editLiveTimeDialog = new EditLiveTimeDialog();
//                inviteOther(accid);
        EditLiveTimeDialog dialog = new EditLiveTimeDialog();
        Bundle timebundle = new Bundle();
        timebundle.putString("tipsTitle", timeType);
        dialog.setArguments(timebundle);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "EditPersonDialog");
        dialog.setOnConfirmListener(new EditLiveTimeDialog.OnEditPersonListener() {
            @Override
            public void subNumber(String time) {
                if ("1".equalsIgnoreCase(timeType)) {
                    presenter.setLiveTime(time);
                } else {
                    presenter.addLiveTime(time);
                }
            }
        });
    }

    private void showFinishAct() {
        BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
        Bundle bundle1 = new Bundle();
        bundle1.putString("showContent", "是否确定退出直播间？");
        bundle1.putString("TITLE_DIO", "退出直播间");
        if (isAnchor) {
            bundle1.putString("comfirm", "结束直播");
        } else {
            bundle1.putString("comfirm", "退出");
        }
        bundle1.putString("cancel", "最小化");
        baseTipsDialog.setArguments(bundle1);
        baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
            @Override
            public void comfirm() {
                //退出直播间
                if (isAnchor) {
                    presenter.closeLive();
                } else {
                    exit();
                }
            }

            @Override
            public void cancel() {
                //最小化
                isSmall = true;
                showFloatingView(StartLiveAct.this);
            }
        });
    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed(String msg) {
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.showShort("消息不能为空");
            return;
        }
//        IMMessage textMessage = createTextMessage(text);
        IMMessage textMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, msg);
        container.proxy.sendMessage(textMessage);
    }

    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }

    @Override
    public void viewInitialization() {
        setupNERtc();
        setupLocalVideo(vv_local_user);

        adapter = new StartLiveAdapter();
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(adapter);


//        action_real_layout.setVisibility(View.GONE);
        actionShowLayout.setVisibility(View.VISIBLE);
        registerObservers(true);

        if (isAnchor) {//主播
            create();
            startNetTv.setVisibility(View.VISIBLE);
            startNetTv.setText("下一位");
            startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
            endliveTv.setVisibility(View.VISIBLE);
        } else {
            startNetTv.setVisibility(View.GONE);
            endliveTv.setVisibility(View.GONE);
        }
        teyaoNumberTv.setVisibility(View.GONE);


        mKeyBoardListener = new SoftKeyBoardListener(StartLiveAct.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });


        //创建广播
        innerReceiver = new InnerReceiver();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(innerReceiver, intentFilter);

        // 默认麦克风关闭
        if (!isAnchor) {
            microphoneIv.setSelected(true);
            carmerIv.setSelected(true);
            setLocalAudioEnable(false);
            setLocalVideoEnable(false);
            ToastExtKt.showToast(this, "为保护您的个人隐私，直播间摄像头与麦克风默认关闭，如有需要可点击打开");
//            ToastUtils.showShort("为保护您的个人隐私，直播间摄像头与麦克风默认关闭，如有需要可点击打开");
        }

        slide_layout.setmListeners(new SlideLayout.DragListener() {
            @Override
            public void onDragToOut(@NonNull @NotNull View dragView) {
                clearScreenIV.setVisibility(View.GONE);

            }

            @Override
            public void onDragToIn(@NonNull @NotNull View dragView) {
//                int[] location = new int[2];
//                dragView.getLocationInWindow(location);
//                int x = location[0];
//             if (x>200){
//                if (openStaus==1){
                clearScreenIV.setVisibility(View.VISIBLE);
//                }

//             }else {
//                 clearScreenIV.setVisibility(View.GONE);
//
//             }

            }
        });
        slide_layout.setSlideListener(new ISlideListener() {
            @Override
            public void onPositionChanged(float percent) {
                openStaus = (int) percent;
                if (percent == 1.0) {
                    clearScreenIV.setVisibility(View.VISIBLE);
                } else {
                    clearScreenIV.setVisibility(View.GONE);
                }
            }
        });

        clearTiming(); // 开启定时清理礼物列表
        initAnim(); // 初始化动画
    }

    /**
     * 创建房间
     */
    private void create() {
        NIMClient.getService(SignallingService.class).create(ChannelType.CUSTOM, "", "").setCallback(
                new RequestCallbackWrapper<ChannelBaseInfo>() {

                    @Override
                    public void onResult(int i, ChannelBaseInfo channelBaseInfo, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            channelInfo = channelBaseInfo;
                            joinRoom();
                            ToastHelper.showToast(StartLiveAct.this, "创建成功");
                        } else {
                            ToastHelper.showToast(StartLiveAct.this, "创建失败， code = " + i +
                                    (throwable == null ? "" : ", throwable = " +
                                            throwable.getMessage()));
                        }
                    }
                });


    }

    private void joinRoom() {
        if (channelInfo == null) {
            return;
        }
        long selfUid = System.nanoTime();
        NIMClient.getService(SignallingService.class).join(channelInfo.getChannelId(), selfUid, "", false).setCallback(
                new RequestCallbackWrapper<ChannelFullInfo>() {

                    @Override
                    public void onResult(int i, ChannelFullInfo channelFullInfo, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            showToast("加入房间成功");
                        } else if (i == ResponseCode.RES_CHANNEL_MEMBER_HAS_EXISTS) {
                            showToast("已经在房间中");
                        } else {
                            showToast("加入房间失败 code=" + i);
                        }
                    }
                });
    }

    //聊天列表滚动到最下面
    private void doScrollToBottom() {
        comment_list.scrollToPosition(adapter.getItemCount() - 1);
    }

    public boolean isMyMessage(ChatRoomMessage message) {
        return message.getSessionType() == container.sessionType &&
                message.getSessionId() != null && message.getSessionId().equals(container.account);
    }

    /**
     * 加入房间
     *
     * @param userID 用户ID
     * @param roomID 房间ID
     */
    private void joinChannel(String nertc_token, String roomID, long userID) {
        NERtcEx.getInstance().joinChannel(nertc_token, roomID, userID);
        vv_local_user.setZOrderMediaOverlay(true);
        vv_local_user.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED);
        NERtcEx.getInstance().setupLocalVideoCanvas(vv_local_user);
    }

    public void joinLiaoTianSHi() {
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        data.setNick(userInfoEntity.getNickname());
        data.setAvatar(userInfoEntity.getAvatar());
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoomEx(data, 1);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                initMessageFragment();
                if (container != null) {
                    SystemAttachment systemAttachment = new SystemAttachment();
                    systemAttachment.setTips(joinLiveEntity.getInfo().getChat().getAnnouncement());
                    ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(container.account, systemAttachment);
                    messageList.add(message);
                    adapter.setList(messageList);
                    doScrollToBottom();
                } else {
                    ToastUtils.showShort("进入聊天室失败!");
                }
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(StartLiveAct.this, "" + i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(StartLiveAct.this, "nonono" + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMessageFragment() {

        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        audioInputPanel = new AudioInputPanel(container, layoutPlayAudio, timer_tip_container, timer, timer_tip, sendAudioIv);
//        messageListPanel = new ChatRoomMsgListPanelTest(container, comment_list);


//        messageFragment = (ChatRoomMessageFragment) getSupportFragmentManager().findFragmentById(
//                R.id.chat_room_message_fragment);
//        if (messageFragment != null) {
//            messageFragment.init(roomId);
//        } else {
//            // 如果Fragment还未Create完成，延迟初始化
//            getHandler().postDelayed(() -> initMessageFragment(), 50);
//        }
    }

    private void setupNERtc() {
        NERtcParameters parameters = new NERtcParameters();
        NERtcEx.getInstance().setParameters(parameters); //先设置参数，后初始化

//        NERtcOption options = new NERtcOption();
//
//        if (BuildConfig.DEBUG) {
//            options.logLevel = NERtcConstants.LogLevel.INFO;
//        } else {
//            options.logLevel = NERtcConstants.LogLevel.WARNING;
//        }
        try {
            NERtcEx.getInstance().init(getApplicationContext(), AppIdConstants.WY_APP_KEY, this, null);
        } catch (Exception e) {
            // 可能由于没有release导致初始化失败，release后再试一次
            NERtcEx.getInstance().release();
            try {
                NERtcEx.getInstance().init(getApplicationContext(), AppIdConstants.WY_APP_KEY, this, null);
            } catch (Exception ex) {
                Toast.makeText(this, "SDK初始化失败", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        setLocalAudioEnable(true);
        setLocalVideoEnable(true);
    }

    protected void setupLocalVideo(NERtcVideoView videoView) {
        videoView.setZOrderMediaOverlay(true);
        videoView.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED);
        NERtcEx.getInstance().setupLocalVideoCanvas(videoView);
        videoView.setVisibility(View.VISIBLE);
    }

    /**
     * 退出房间并关闭页面
     */
    private void exit() {
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        if (joinedChannel) {
            leaveChannel();
        }
        finish();
    }

    /**
     * 退出房间
     *
     * @return 返回码
     * @see com.netease.lava.nertc.sdk.NERtcConstants.ErrorCode
     */
    private boolean leaveChannel() {
        joinedChannel = false;
        setLocalAudioEnable(false);
        setLocalVideoEnable(false);
        int ret = NERtcEx.getInstance().leaveChannel();
        return ret == NERtcConstants.ErrorCode.OK;
    }

    @Override
    public void onBackPressed() {
//        exit();
        showFinishAct();
    }

    /**
     * 设置本地音频的可用性
     */
    private void setLocalAudioEnable(boolean enable) {
        enableLocalAudio = enable;
        NERtcEx.getInstance().enableLocalAudio(enableLocalAudio);
//        enableAudioIb.setImageResource(enable ? R.drawable.selector_meeting_mute : R.drawable.selector_meeting_unmute);
    }

    /**
     * 设置本地视频的可用性
     */
    private void setLocalVideoEnable(boolean enable) {
        enableLocalVideo = enable;
        NERtcEx.getInstance().enableLocalVideo(enableLocalVideo);
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeCdnRequestData(cdnReqData, register);
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(nimOnlineObserver, register);
    }

    @Override
    public void finish() {
        super.finish();
        ActivityTaskManager.getInstance().removeSingleInstanceActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskManager.getInstance().removeSingleInstanceActivity(this);
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        if (innerReceiver != null) {
            unregisterReceiver(innerReceiver);
        }
        try {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            stopVideoService();
            SocketLiveUtils.INSTANCE.closeConnect();
            EventBus.getDefault().unregister(this);
            registerObservers(false);
            NERtcEx.getInstance().release();
            closeChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (audioInputPanel != null) {
            audioInputPanel.onDestroy();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isSmall) {
            carmerIv.setSelected(false);
            setLocalVideoEnable(true);
        }

        if (audioInputPanel != null) {
            audioInputPanel.onPause();
        }
    }

    @Override
    public void doBusiness() {
        presenter.getGoingLive();
    }

    /**
     * 获取到一对一最新用户
     *
     * @param data
     */
    @Override
    public void setSeeLiveInfo(SeeLiveUserEntity data) {
        this.seeliveEntity = data;
//        当前状态: 1-正常下一位用户 2-进行中用户 3-异常退出用户,弹窗提示跳过/继续邀请
        if (data.getStatus_code() == 3 || data.getStatus_code() == 2) {
            BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
            Bundle bundle1 = new Bundle();
            bundle1.putString("showContent", "当前用户已断开连接，是否再次发起邀请？");
            bundle1.putString("TITLE_DIO", "直播提示");
            bundle1.putString("comfirm", "邀请");
            bundle1.putString("cancel", "取消");
            baseTipsDialog.setArguments(bundle1);
            baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
            baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
                @Override
                public void comfirm() {
                    //再次邀请
                    accid = data.getAppoint().getAccid();

                    inviteOther(accid);
                    if (data.getAppoint() != null) {
//            inviteOther(data.getAppoint().getAccid());
                        checkMsgDialog = new CheckMsgDialog();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Message", data.getAppoint());
                        checkMsgDialog.setArguments(bundle);
                        checkMsgDialog.show(getSupportFragmentManager(), "CheckMsgDialog");
                    }
                }

                @Override
                public void cancel() {
                    //取消邀请
                    presenter.jumpLive();
                }
            });
            return;
        } else {
            BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
            Bundle bundleEnd = new Bundle();
            if (data.getAppoint() != null && !TextUtils.isEmpty(data.getAppoint().getNickname())) {
                bundleEnd.putString("showContent", "是否邀请" + data.getAppoint().getNickname() + "进入直播间?");
            } else {
                bundleEnd.putString("showContent", "是否邀请下一位用户进入直播间?");

            }
            bundleEnd.putString("TITLE_DIO", "温馨提示");
            bundleEnd.putString("comfirm", "确认");
            bundleEnd.putString("cancel", "取消");
            baseTipsDialog.setArguments(bundleEnd);
            baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
            baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
                @Override
                public void comfirm() {
                    //结束当前直播
                    accid = data.getAppoint().getAccid();

                    inviteOther(accid);
                    if (data.getAppoint() != null) {
//            inviteOther(data.getAppoint().getAccid());
                        checkMsgDialog = new CheckMsgDialog();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Message", data.getAppoint());
                        checkMsgDialog.setArguments(bundle);
                        checkMsgDialog.show(getSupportFragmentManager(), "CheckMsgDialog");
                    }
                }

                @Override
                public void cancel() {
                }
            });
        }


    }

    // 延迟2s弹Toasat
//        handler.postDelayed(runnable,20 * 1000);
    private void inviteOther(String account) {
        if (channelInfo == null) {
            Toast.makeText(this, "请先创建频道或加入频道", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("roomId", roomId);//聊天室ID
        map.put("liveId", liveId);//直播间ID
        map.put("avatar", userInfoEntity.getAvatar());//主播头像
        map.put("userName", userInfoEntity.getNickname());//主播用户名
        map.put("type", "1");//一对一通话识别
        map.put("liveClassify", joinLiveEntity.getInfo().getCate_one() + "_" + joinLiveEntity.getInfo().getCate());
        map.put("consulationID", userInfoEntity.getIs_read_conceal());
        String callInformation = JSON.toJSONString(map);
        String invitedRequestId = System.currentTimeMillis() + "_id";
        InviteParamBuilder param = new InviteParamBuilder(channelInfo.getChannelId(), account, invitedRequestId);
        param.customInfo(callInformation);
        param.offlineEnabled(true);
        NIMClient.getService(SignallingService.class).invite(param).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                startNetTv.setText("邀请中");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
                handler.postDelayed(runnable, 20 * 1000);

            }

            @Override
            public void onFailed(int code) {
                showConcectFailView();
                if (checkMsgDialog != null) {
                    checkMsgDialog.dismiss();
                }
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    private void showConcectFailView() {
        BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
        Bundle bundle1 = new Bundle();
        bundle1.putString("showContent", "当前排队用户暂时无法接听");
        bundle1.putString("TITLE_DIO", "直播提示");
        bundle1.putString("comfirm", "结束");
        bundle1.putString("cancel", "跳过");
        baseTipsDialog.setArguments(bundle1);
        baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
            @Override
            public void comfirm() {
                presenter.endLive();
            }

            @Override
            public void cancel() {
                //取消邀请
                presenter.jumpLive();
            }
        });
    }

    @Override
    public void setLikeAnNoLike(String msg) {
        ToastUtils.showShort(msg);
        if (isLike) {
            attentionTv.setSelected(true);
            attentionTv.setText("+关注");
        } else {
            attentionTv.setSelected(false);
            attentionTv.setText("取消关注");
        }
        isLike = !isLike;
    }

    /**
     * 设置一对一直播时间成功
     */
    @Override
    public void setTimeSucess() {
        startNetTv.setText("加时");
        startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
        endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_fc595e_26));
        endliveTv.setText("结束");
        isShowEndliveDialog = false;
    }

    /**
     * 增加一对一直播时间成功
     */
    @Override
    public void addTimeSucess() {
        isShowEndliveDialog = false;
//        startNetTv.setText("下一位");
//        startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
//        endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
//        endliveTv.setText("特邀");
    }

    @Override
    public void endLiveSuccess() {
        startNetTv.setText("下一位");
        startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
        endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
        endliveTv.setText("特邀");
    }

    /**
     * 跳过下一位成功
     */
    @Override
    public void jumpLiveSuccess() {
        startNetTv.setText("下一位");
        startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
        endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
        endliveTv.setText("特邀");
    }

    /**
     * 进入直播见成功，设置提示公告
     *
     * @param data
     */
    @Override
    public void setJoinLiveSuccess(JoinLiveEntity data) {
        joinLiveEntity = data;
        presenter.zhuboId = String.valueOf(data.getInfo().getAnchor_id());
        channelName = data.getInfo().getChannelName();
        roomId = String.valueOf(data.getInfo().getChat().getRoomid());
        push_url = data.getInfo().getPush_url();
        nertc_token = data.getInfo().getNertc_token();

        ReserveTv.setSelected(data.getInfo().getIs_appoint() == 1);
        if (!TextUtils.isEmpty(roomId) && !TextUtils.isEmpty(liveId)) {
            joinLiaoTianSHi();
            joinChannel(nertc_token, channelName, uid);
        }
    }

    @Override
    public void setNomomey() {
        com.netease.nim.uikit.business.session.activity.my.dialog.gift.BaseTipsDialog baseTipsDialog1 = new com.netease.nim.uikit.business.session.activity.my.dialog.gift.BaseTipsDialog();
        Bundle bundle2 = new Bundle();
        bundle2.putString("showContent", "您剩余的高手币不足，请充值后再进行打赏");
        bundle2.putString("comfirm", "去充值");
        baseTipsDialog1.setArguments(bundle2);
        baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog1.setOnConfirmListener(new com.netease.nim.uikit.business.session.activity.my.dialog.gift.BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                gotoActivity(RechargeAct.class);
            }
        });
    }

    @Override
    public void setGiftForMe(GiftForMeEntity giftForMeEntity) {
        GiftForMeDialog dialog = new GiftForMeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("GIFT_FOR_ME", presenter.giftForMeEntity);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "GiftForMeDialog");
    }

    @Override
    public void finishAct() {
        exit();
    }

    @Override
    public void closeLiveSuccess() {
        exit();
    }

    @Override
    public void setGiftInfo(GiftEntity data) {
        this.giftEntity = data;
        GiftDialog commentDF = new GiftDialog();
        Bundle bundle = new Bundle();
        if (giftEntity != null) {
            bundle.putSerializable("GIFT_INFO", giftEntity);
        }
        commentDF.setArguments(bundle);
        commentDF.show(getSupportFragmentManager(), "GiftDialog");
        commentDF.setSelectGiftListener(new GiftDialog.OnSelectGiftListener() {
            @Override
            public void comfirm(GiftEntity.ListBean giftEntity) {
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "是否确认赠送礼物给主播");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
//                                presenter.unbindWechat("1");
                        presenter.sendGift(giftEntity);
                    }
                });
            }
        });
    }

    @Override
    public void sendGiftSuccess(GiftEntity.ListBean giftId) {
        audioInputPanel.sendGift(giftId);

    }

    @Override
    public void onUserSubStreamVideoStart(long l, int i) {

    }

    @Override
    public void onUserSubStreamVideoStop(long l) {

    }

    @Override
    public void onUserAudioMute(long l, boolean b) {

//        uid	用户 ID，提示是哪个用户的音频流。
//        muted	是否停止发送音频流。
//        true：该用户已暂停发送音频流。
//        false：该用户已恢复发送音频流。


    }

    @Override
    public void onUserVideoMute(long l, boolean b) {

//        uid	用户 ID，提示是哪个用户的视频流。
//        muted	是否停止发送视频流。
//        true：该用户已暂停发送视频流。
//        false：该用户已恢复发送视频流。
        if (b) {
            ToastUtils.showShort("打开了摄像头");
        } else {
            ToastUtils.showShort("关闭了摄像头");
        }

    }

    @Override
    public void onFirstAudioDataReceived(long l) {

    }

    @Override
    public void onFirstVideoDataReceived(long l) {

    }

    @Override
    public void onFirstAudioFrameDecoded(long l) {

    }

    @Override
    public void onFirstVideoFrameDecoded(long l, int i, int i1) {

    }

    @Override
    public void onUserVideoProfileUpdate(long l, int i) {

    }

    @Override
    public void onAudioDeviceChanged(int i) {

    }

    @Override
    public void onAudioDeviceStateChange(int i, int i1) {

    }

    @Override
    public void onVideoDeviceStageChange(int i) {

    }

    @Override
    public void onConnectionTypeChanged(int i) {

    }

    @Override
    public void onReconnectingStart() {

    }

    @Override
    public void onReJoinChannel(int i, long l) {

    }

    @Override
    public void onAudioMixingStateChanged(int i) {

    }

    @Override
    public void onAudioMixingTimestampUpdate(long l) {

    }

    @Override
    public void onAudioEffectFinished(int i) {

    }

    @Override
    public void onLocalAudioVolumeIndication(int i) {

    }

    @Override
    public void onRemoteAudioVolumeIndication(NERtcAudioVolumeInfo[] neRtcAudioVolumeInfos, int i) {

    }

    @Override
    public void onLiveStreamState(String s, String s1, int i) {
        if (pushStream != null) {
            pushStream.updateState(i);
        }
    }

    @Override
    public void onConnectionStateChanged(int i, int i1) {

    }

    @Override
    public void onCameraFocusChanged(Rect rect) {

    }

    @Override
    public void onCameraExposureChanged(Rect rect) {

    }

    @Override
    public void onRecvSEIMsg(long l, String s) {

    }

    @Override
    public void onAudioRecording(int i, String s) {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onWarning(int i) {

    }

    @Override
    public void onMediaRelayStatesChange(int i, String s) {

    }

    @Override
    public void onMediaRelayReceiveEvent(int i, int i1, String s) {

    }

    @Override
    public void onLocalPublishFallbackToAudioOnly(boolean b, NERtcVideoStreamType neRtcVideoStreamType) {

    }

    @Override
    public void onRemoteSubscribeFallbackToAudioOnly(long l, boolean b, NERtcVideoStreamType neRtcVideoStreamType) {

    }

    @Override
    public void onJoinChannel(int i, long l, long l1, long l2) {
        if (i == NERtcConstants.ErrorCode.OK) {

            joinedChannel = true;
            // 加入房间，准备展示己方视频
            vv_local_user.setVisibility(View.VISIBLE);
            pushStream = new PushStream(this, liveId, uid);
            togglePushStream();

        }
    }

    private void togglePushStream() {
        if (pushStream == null) {
            return;
        }
        if (!pushStream.isStarted()) {
            String pushUrl = push_url;
            if (TextUtils.isEmpty(pushUrl)) {
                return;
            }
            pushStream.start(pushUrl);
        } else {
            pushStream.stop();
        }
    }

    @Override
    public void onLeaveChannel(int i) {
        PushStream pushStream = this.pushStream;
        this.pushStream = null;
        if (pushStream != null) {
            pushStream.stop();
        }
    }

    @Override
    public void onUserJoined(long l) {
        // 已经有订阅，就不要变了
        if (vv_remote_user.getTag() != null) {
            return;
        }
        // 有用户加入，设置Tag，该用户离开前，只订阅和取消订阅此用户
        vv_remote_user.setTag(l);
        // 不用等待了
//        waitHintTv.setVisibility(View.INVISIBLE);

        if (!isCurrentUser(l)) {
            return;
        }
        otherUid = l;
        if (checkMsgDialog != null) {
            checkMsgDialog.dismiss();
        }

        NERtcEx.getInstance().subscribeRemoteVideoStream(l, NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh, true);
        vv_remote_user.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED);
        NERtcEx.getInstance().setupRemoteVideoCanvas(vv_remote_user, l);

        // 更新界面
        vv_remote_user.setVisibility(View.VISIBLE);
        if (isAnchor) {
            if (socketEntity != null && socketEntity.getType() == 1) {
                startNetTv.setText("加时");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
            } else {
                startNetTv.setText("开始");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
            }
            endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_fc595e_26));
            endliveTv.setText("结束");
            teyaoNumberTv.setVisibility(View.GONE);

        }
        if (pushStream != null) {
            pushStream.update(l, true);
        }
    }

    /**
     * 判断是否为onUserJoined中，设置了Tag的用户
     *
     * @param uid 用户ID
     * @return 用户ID是否匹配
     */
    private boolean isCurrentUser(long uid) {
        Object tag = vv_remote_user.getTag();
        return tag != null && tag.equals(uid);
    }

    @Override
    public void onUserLeave(long l, int i) {
        // 退出的不是当前订阅的对象，则不作处理
        if (!isCurrentUser(l)) {
            return;
        }
        otherUid = 0L;
        if (!isAnchor) {//如果是用户，主播退出了直播间，用户也自动关闭页面
//            ToastUtils.showShort("主播退出了直播间");
//            finish();
            liveTipsTv.setVisibility(View.VISIBLE);
            liveTipsTv.setText("主播退出了直播间");
            return;
        }
        // 设置TAG为null，代表当前没有订阅
        vv_remote_user.setTag(null);
        NERtcEx.getInstance().subscribeRemoteVideoStream(l, NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh, false);
    }

    @Override
    public void onUserAudioStart(long uid) {
        if (!isCurrentUser(uid)) {
            return;
        }
        NERtcEx.getInstance().subscribeRemoteAudioStream(uid, true);
    }

    @Override
    public void onUserAudioStop(long l) {

    }

    @Override
    public void onUserVideoStart(long l, int i) {
        if (!isCurrentUser(l)) {
            return;
        }
        otherUid = l;
        if (checkMsgDialog != null) {
            checkMsgDialog.dismiss();
        }

        NERtcEx.getInstance().subscribeRemoteVideoStream(l, NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh, true);
        vv_remote_user.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED);
        NERtcEx.getInstance().setupRemoteVideoCanvas(vv_remote_user, l);

        // 更新界面
        vv_remote_user.setVisibility(View.VISIBLE);
        if (isAnchor) {
            if (socketEntity != null && socketEntity.getType() == 1) {
                startNetTv.setText("加时");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
            } else {
                startNetTv.setText("开始");
                startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
            }
            endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_fc595e_26));
            endliveTv.setText("结束");
            teyaoNumberTv.setVisibility(View.GONE);

        }
        if (pushStream != null) {
            pushStream.update(l, true);
        }

    }

    @Override
    public void onUserVideoStop(long l) {

    }

    @Override
    public void onDisconnect(int i) {

    }

    @Override
    public void onClientRoleChange(int i, int i1) {

    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        ChatRoomMessage message = (ChatRoomMessage) msg;

        ChatRoomHelper.buildMemberTypeInRemoteExt(message, roomId);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        messageList.add(message);
                        adapter.addData(message);
                        if (message.getMsgType() == MsgTypeEnum.custom) {
                            try {
                                String allData = message.getAttachStr();
                                JSONObject jsonObjectTop = JSONObject.parseObject(allData);
                                int type = jsonObjectTop.getInteger("type");
                                if (type == 5) {
                                    showGift(message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_CHATROOM_MUTED) {
                            ToastHelper.showToast(NimUIKit.getContext(), "用户被禁言");
                        } else if (code == ResponseCode.RES_CHATROOM_ROOM_MUTED) {
                            ToastHelper.showToast(NimUIKit.getContext(), "全体禁言");
                        } else {
                            ToastHelper.showToast(NimUIKit.getContext(), "消息发送失败!");
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        ToastHelper.showToast(NimUIKit.getContext(), "消息发送失败！");
                    }
                });

//        if (aitManager != null) {
//            aitManager.reset();
//        }
        return true;
    }

    @Override
    public void onInputPanelExpand() {
//        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {

    }

    @Override
    public void onReplyMessage(IMMessage replyMsg) {

    }

    /**
     * 关闭频道
     */
    private void closeChannel() {
        if (!isAnchor) {
            return;
        }
        if (channelInfo == null) {
            Toast.makeText(this, "请先创建频道或加入频道", Toast.LENGTH_SHORT).show();
            return;
        }
        NIMClient.getService(SignallingService.class).close(channelInfo.getChannelId(),
                true, "关闭频道的自定义字段").setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                channelInfo = null;
            }

            @Override
            public void onFailed(int code) {
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(liveId)) {
//            SocketLiveUtils.INSTANCE.closeConnect();
            String lasdak = AcacheUtil.getToken(StartLiveAct.this, false).substring(7);
            String url = Url.SOCKET_URL + "room?room_id=" + liveId + "&token=" + lasdak;
            SocketLiveUtils.INSTANCE.onStartCommand(url);
        }
        presenter.getShareImg();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doTradeListEvent(OTOEvent tradeListEvent) {
        if (tradeListEvent != null) {
            switch (tradeListEvent.getMsg_id()) {
                case 1:
                    socketEntity = tradeListEvent.getEntity();
                    if (socketEntity != null) {
//                        presenter.zhuboId = socketEntity.;
                        Glide.with(StartLiveAct.this).load(socketEntity.getAvatar()).into(anchorAvaterCiv);
                        Glide.with(StartLiveAct.this).load(socketEntity.getLevel()).into(leveImg);
                        anchorNameTv.setText(socketEntity.getNickname());
//                        GlideUtils.loadImage(StartLiveAct.this, socketEntity.getAvatar(), anchorAvaterCiv);
//                        GlideUtils.loadImage(StartLiveAct.this, socketEntity.getAvatar(), leveImg);
                        liveIdTv.setText("ID:" + socketEntity.getAnchor_no());
                        timeTv.setText(String.format("已播/计时：%1$s/%2$s", (int) (socketEntity.getPlay_time() / 60), (int) (socketEntity.getSet_time() / 60)));
                        ReserveTv.setText(String.format("当前等待人数：%1$s", socketEntity.getAppoint_num() - socketEntity.getNow_appoint_num()));//预约人数-已经预约了的人数
                        if (socketEntity.getIs_self() == 1) {//判断是否是主播
                            attentionTv.setVisibility(View.GONE);
                        } else {
                            attentionTv.setVisibility(View.VISIBLE);
                            if (socketEntity.getIs_sub() == 1) {
                                isLike = true;
                                attentionTv.setText("取消关注");
                            } else {
                                isLike = false;
                                attentionTv.setText("+关注");
                            }
                        }
                        if (socketEntity.getNow_time() > 0) {
                            isShowEndliveDialog = false;
                        }
                        //-1-未开始直播 0-直播已结束 1-直播中
                        if (socketEntity.getType() == 0) {
                            if (liveEndDialog != null && liveEndDialog.getDialog() != null && liveEndDialog.getDialog().isShowing()) {

                            } else {
                                if (!isShowEndliveDialog) {
                                    liveEndDialog = new BaseTipsDialog();
                                    Bundle bundle2 = new Bundle();
                                    if (isAnchor) {
                                        bundle2.putString("showContent", "设定时间已经到了，请与观众确认是否加时");
                                    } else {
                                        bundle2.putString("showContent", "您与主播设定时间已经到了，请与主播确认是否加时");
                                    }
                                    bundle2.putString("comfirm", "知道了");
                                    bundle2.putBoolean("idSeeCancel", true);
                                    liveEndDialog.setArguments(bundle2);
                                    liveEndDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                                }
                                isShowEndliveDialog = true;
                            }
                        }
                        if (socketEntity.getSpecial_count() > 0 && "特邀".equalsIgnoreCase(endliveTv.getText().toString()) && isAnchor) {
                            teyaoNumberTv.setVisibility(View.VISIBLE);
                            teyaoNumberTv.setText(socketEntity.getSpecial_count() + "");
                        } else {
                            teyaoNumberTv.setVisibility(View.GONE);
                        }
                    }

                    break;
                case 5:
                    if (!isAnchor) {
                        liveTipsTv.setVisibility(View.VISIBLE);
                        liveTipsTv.setText("直播已经结束了");
                        leaveChannel();
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!ishasPingfen) {
                                    exit();
                                }
                            }
                        }, 3000);

                    }
                    break;
                case 6:
                    if (!isAnchor) {
                        ishasPingfen = true;
                        LiveAssessDialog2 liveAssessDialog = new LiveAssessDialog2(StartLiveAct.this);
                        liveAssessDialog.setCanceledOnTouchOutside(false);
                        liveAssessDialog.setCancelable(false);
                        liveAssessDialog.setOnConfirmListener(new LiveAssessDialog2.OnBaseTipsListener() {
                            @Override
                            public void comfirm(int abilityNumber, int serviceNumber) {
                                presenter.subScoreToLive(String.valueOf(abilityNumber), String.valueOf(serviceNumber));
                            }
                        });
                        liveAssessDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                exit();
                            }
                        });
                        liveAssessDialog.show();
                    }
                    break;
                case 7://用户异常退出
                    //用户退出了直播间，主播提示再次邀请
                    if (!isAnchor) {
                        exit();
                        return;
                    }
                    startNetTv.setText("下一位");
                    startNetTv.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32));
                    endliveTv.setBackground(getResources().getDrawable(R.drawable.shape_ffa743_23));
                    endliveTv.setText("特邀");
                    BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("showContent", "当前用户已断开连接，是否再次发起邀请");
                    bundle2.putString("comfirm", "再次邀请");
                    bundle2.putString("cancel", "取消");
                    baseTipsDialog1.setArguments(bundle2);
                    baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog1.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            inviteOther(accid);
                        }

                        @Override
                        public void cancel() {
                            presenter.jumpLive();
                        }
                    });
                    break;
                case 10://直播间已经结束
                    Logger.d("直播间已经结束");
                    break;
                case 403://强制停播
                case 404://强制停播
                    CanFinishDialog finishDialog = new CanFinishDialog();
                    Bundle bundleFinish = new Bundle();
                    if (isAnchor) {
                        bundleFinish.putString("showContent", "您已被平台强制停播");
                    } else {
                        bundleFinish.putString("showContent", "本场直播已被停播，去其他直播间看看吧");
                    }
                    finishDialog.setArguments(bundleFinish);
                    finishDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                    finishDialog.setOnConfirmListener(new CanFinishDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            exit();
                        }
                    });
                    break;
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doCallEvent(CallEvent callEvent) {
        /*
         * 拒绝邀请1
         * 接受邀请2
         */
        if (callEvent != null && callEvent.getAckStatus() == 1) {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
            BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
            Bundle bundle2 = new Bundle();
            bundle2.putString("showContent", "对方拒绝邀请");
            bundle2.putString("comfirm", "再次邀请");
            bundle2.putString("cancel", "结束");
            baseTipsDialog1.setArguments(bundle2);
            baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
            baseTipsDialog1.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
                @Override
                public void comfirm() {
                    inviteOther(accid);
                }

                @Override
                public void cancel() {
                    presenter.jumpLive();
                }
            });
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isSmall = false;
        stopVideoService();//关闭悬浮窗服务
        // 关闭悬浮窗之后从新设置视频布局
        NERtcEx.getInstance().setupLocalVideoCanvas(vv_local_user);
        // NERtcEx.getInstance().setupLocalVideoCanvas(vv_local_user);
        // NERtcEx.getInstance().setupRemoteVideoCanvas(vv_remote_user, uid);
        if (otherUid != 0L) {
            NERtcEx.getInstance().setupRemoteVideoCanvas(vv_remote_user, otherUid);
        }
    }

    /**
     * 显示悬浮窗
     */
    private void showFloatingView(Context context) {
        // API22以下直接启动
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startVideoService();
        } else {
            if (!Settings.canDrawOverlays(StartLiveAct.this)) {
                showDialog();
            } else {
                startVideoService();
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请开启悬浮窗权限");
        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + StartLiveAct.this.getPackageName()));
//                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            // 同意
                            startVideoService();
                        }

                        @Override
                        public void onDenied() {//不同意

                        }
                    });
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 打开悬浮窗服务
     */
    public void startVideoService() {
        boolean isWork = isServiceWork(this, FloatingViewService.class.getCanonicalName());
        Logger.e("startVideoService-isWork=" + isWork);
        if (!isWork) {
            Intent intent = new Intent(this, FloatingViewService.class);//开启服务显示悬浮框
//            startService(intent);
            bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
        }
        //最小化Activity
        moveTaskToBack(true);
    }

    /**
     * 关闭悬浮窗服务
     */
    public void stopVideoService() {
        boolean isWork = isServiceWork(this, FloatingViewService.class.getCanonicalName());
        Logger.e("stopVideoService-isWork=" + isWork);
        if (isWork) {
            unbindService(mVideoServiceConnection);//不显示悬浮框
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     */
    public boolean isServiceWork(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 广播监听home键盘，显示小窗口
     */
    static class InnerReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                        Toast.makeText(StartLiveAct.this, "Home键被监听", Toast.LENGTH_SHORT).show();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                        Toast.makeText(StartLiveAct.this, "多任务键被监听", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public class NumberAnim {
        private Animator lastAnimator;

        public void showAnimator(View v) {

            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.3f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(200);
            lastAnimator = animSet;
            animSet.start();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
//            issubPremission = true;
//        }
//    }

    // -----------------------------------------------------------------悬浮窗————————————————————————————
}