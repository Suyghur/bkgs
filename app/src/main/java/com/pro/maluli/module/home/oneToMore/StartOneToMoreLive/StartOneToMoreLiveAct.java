package com.pro.maluli.module.home.oneToMore.StartOneToMoreLive;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.netease.lava.nertc.sdk.NERtcCallbackEx;
import com.netease.lava.nertc.sdk.NERtcConstants;
import com.netease.lava.nertc.sdk.NERtcEx;
import com.netease.lava.nertc.sdk.NERtcParameters;
import com.netease.lava.nertc.sdk.stats.NERtcAudioVolumeInfo;
import com.netease.lava.nertc.sdk.video.NERtcRemoteVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoView;
import com.netease.neliveplayer.playerkit.sdk.LivePlayerObserver;
import com.netease.neliveplayer.playerkit.sdk.PlayerManager;
import com.netease.neliveplayer.playerkit.sdk.VodPlayer;
import com.netease.neliveplayer.playerkit.sdk.constant.CauseCode;
import com.netease.neliveplayer.playerkit.sdk.model.MediaInfo;
import com.netease.neliveplayer.playerkit.sdk.model.SDKOptions;
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo;
import com.netease.neliveplayer.playerkit.sdk.model.VideoBufferStrategy;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;
import com.netease.neliveplayer.playerkit.sdk.view.AdvanceTextureView;
import com.netease.neliveplayer.proxy.config.NEPlayerConfig;
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
import com.netease.nimlib.sdk.avsignalling.constant.SignallingEventType;
import com.netease.nimlib.sdk.avsignalling.event.CanceledInviteEvent;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCloseEvent;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.ControlEvent;
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserJoinEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserLeaveEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelFullInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.CdnRequestData;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomUpdateInfo;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.base.activityManager.ActivityTaskManager;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.constant.AppIdConstants;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.networkRequest.Url;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.CanFinishDialog;
import com.pro.maluli.common.view.dialogview.InputTextLiveDialog;
import com.pro.maluli.common.view.dialogview.LianmaiDialog;
import com.pro.maluli.common.view.dialogview.LiveMoreDialog;
import com.pro.maluli.common.view.dialogview.OnlineMemberDialog;
import com.pro.maluli.common.view.dialogview.ShareAppDialog;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.common.view.dialogview.gift.GiftDialog;
import com.pro.maluli.common.view.myselfView.MagicTextView;
import com.pro.maluli.common.view.slideView.SlideLayout;
import com.pro.maluli.ktx.ext.ToastExtKt;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.chatRoom.entity.CustomizeInfoEntity;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.presenter.IStartOneToMoreLiveContraction;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.presenter.StartOneToMoreLivePresenter;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.pushstream.PushStream;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.home.previewLive.adapter.PreviewLiveAdapter;
import com.pro.maluli.module.home.startLive.AudioInputPanel;
import com.pro.maluli.module.home.startLive.adapter.StartLiveAdapter;
import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service.FloatingViewMoreService;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;
import com.pro.maluli.module.socketService.SocketLiveUtils;
import com.pro.maluli.module.socketService.event.OTOEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class StartOneToMoreLiveAct extends BaseMvpActivity<IStartOneToMoreLiveContraction.View, StartOneToMoreLivePresenter> implements IStartOneToMoreLiveContraction.View, NERtcCallbackEx, ModuleProxy {
    static boolean isActive = false;
    protected VodPlayer player;
    @BindView(R.id.vv_local_user)
    NERtcVideoView vv_local_user;
    @BindView(R.id.changeCameraIV)
    ImageView changeCameraIV;
    @BindView(R.id.comment_list)
    RecyclerView comment_list;
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
    @BindView(R.id.textureView)
    AdvanceTextureView textureView;
    @BindView(R.id.render_layout)
    FrameLayout render_layout;
    @BindView(R.id.checkOnlineIv)
    ImageView checkOnlineIv;
    @BindView(R.id.lianmaiIv)
    ImageView lianmaiIv;
    @BindView(R.id.checkImg)
    ImageView checkImg;
    @BindView(R.id.leveImg)
    ImageView leveImg;
    @BindView(R.id.anchorAvaterCiv)
    CircleImageView anchorAvaterCiv;
    @BindView(R.id.anchorNameTv)
    TextView anchorNameTv;
    @BindView(R.id.liveIdTv)
    TextView liveIdTv;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.lianmaiYesTv)//连麦同意
    TextView lianmaiYesTv;
    @BindView(R.id.lianmaiNotv)//连麦拒绝
    TextView lianmaiNotv;
    @BindView(R.id.lianmaiStatusTv)//
    TextView lianmaiStatusTv;
    @BindView(R.id.cancelLianmaiTv)//
    TextView cancelLianmaiTv;
    @BindView(R.id.connectLL)
    LinearLayout connectLL;
    @BindView(R.id.lianmaiAchonrIv)
    CircleImageView lianmaiAchonrIv;
    @BindView(R.id.camareIv)//关闭、打开摄像头
    ImageView camareIv;
    @BindView(R.id.microphoneIv)//关闭、打开摄像头
    ImageView microphoneIv;
    @BindView(R.id.anchorControLL)//关闭、打开摄像头
    LinearLayout anchorControLL;
    @BindView(R.id.slide_layout)
    SlideLayout slide_layout;
    @BindView(R.id.clearScreenIV)
    TextView clearScreenIV;
    @BindView(R.id.onlineMemberTV)//在线观众人数
    TextView onlineMemberTV;
    @BindView(R.id.anchorImgRv)//在线观众人数
    RecyclerView anchorImgRv;
    @BindView(R.id.chronometer)//在线观众人数
    Chronometer chronometer;
    @BindView(R.id.ReserveTv)//预约人数
    TextView ReserveTv;
    @BindView(R.id.ll_gift_group)
    LinearLayout ll_gift_group;
    Container container;
    AnchorInfoEntity anchorInfoEntity;//主播信息
    String LMavatar, lmAccid;//被邀请连麦Accid;
    JoinLiveEntity joinLiveEntity;
    PreviewLiveAdapter previewLiveAdapter;
    //    List<List<AnchorInfoEntity.PictureBean>> pictureBeanList = new ArrayList<>();
    List<AnchorInfoEntity.PictureBean> pictureBeanList = new ArrayList<>();
    OnlineMemberDialog memberDialog;
    LianmaiDialog lianmaiDialog;
    LianmaiEntity lianmaiEntity;
    LianmaiDialog lianmaiDialogPD;
    private boolean isLianmai = false;
    /**
     * 邀请别人
     */
    String invitedRequestId;
    InputTextLiveDialog inputTextLiveDialog;
    //要用Handler回到主线程操作UI，否则会报错
    Handler handler = new Handler(Looper.getMainLooper()) {
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
    Observer<CdnRequestData> cdnReqData = new Observer<CdnRequestData>() {
        @Override
        public void onEvent(CdnRequestData data) {
            if (data == null) {
                return;
            }
            NimLog.i("@CJL/cdn req data", String.format("reaDate=%s, failFinal=%s", data.getUrlReqData(), data.getFailFinal()));
        }
    };
    /**
     * 获取在线观众
     */
    List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
    List<ChatRoomMember> memberAll = new ArrayList<>();
    /**
     * 最小化
     */
    BaseTipsDialog baseTipsDialog;
    private AudioInputPanel audioInputPanel;
    private String roomId, liveId;
    private String push_url;//推流地址
    private String rtmp_pull_url;//拉流地址
    ServiceConnection mVideoServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatingViewMoreService.MyBinder binder = (FloatingViewMoreService.MyBinder) service;
            binder.getService();
            //这里测试 设置通话从10秒开始
            if (rtmp_pull_url != null) {
                binder.setData(rtmp_pull_url);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private Long uid;
    private boolean joinedChannel = false;
    private boolean enableLocalVideo = true;
    private boolean enableLocalAudio = true;
    private String nertc_token = "";
    private String channelName;
    private StartLiveAdapter adapter;
    /**
     * 898
     * 子页面
     */
    private ChatRoomMessageFragment messageFragment;
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    private List<ChatRoomMessage> messageList;
    private SeeLiveUserEntity seeliveEntity;
    private UserInfoEntity userInfoEntity;
    private List<NoticeEntity.ListBean> listBeans = new ArrayList<>();
    private PushStream pushStream;
    /**
     * 6.0权限处理
     **/
    private boolean bPermission = false;
    private boolean isAvater;
    private MediaInfo mediaInfo;
    private SDKOptions config;
    private String anchorAccid, userAccid;
    private GiftEntity giftEntity;
    private boolean isJoinChatroom;
    private String channelId;
    private boolean isSHowImg = false;//是否查看主播图片
    private boolean isLike;//是否关注
    private int numberReserve;//用户预约人数
    /**
     * 信令回调
     */
    //收到的邀请参数,reject 用到lianmai111
    private InvitedEvent invitedEvent;
    private List<LianmaiEntity> lianmaiEntities = new ArrayList<>();//同时连麦的集合
    /**
     * 邀请别人
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (lianmaiStatusTv.getText().toString().equalsIgnoreCase("连麦申请中")) {
                ToastUtils.showShort("对方没有响应");
                connectLL.setVisibility(View.GONE);
                cancelInvite();
            }
        }
    };
    /**
     * 刷礼物的方法
     */
    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private NumberAnim giftNumberAnim;
    // 聊天信息回调
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            // 处理新收到的消息
            if (messages == null || messages.isEmpty()) {
                return;
            }
            boolean needRefresh = false;
            for (ChatRoomMessage message : messages) {
                // 保证显示到界面上的消息，来自同一个聊天室
                if (isMyMessage(message) && message.getMsgType() != MsgTypeEnum.notification) {
                    if (message.getMsgType() == MsgTypeEnum.custom) {
                        String allData = message.getAttachStr();
                        JSONObject jsonObjectTop = JSONObject.parseObject(allData);
                        int type = jsonObjectTop.getInteger("type");
                        try {
                            switch (type) {
                                case CustomAttachmentType.RedPacket:
                                    showGift(message);
                                    messageList.add(message);
                                    needRefresh = true;
                                    break;
                                case CustomAttachmentType.lianmai:
                                    JSONObject jsonObject = jsonObjectTop.getJSONObject("data");
                                    String anchorPhoto = jsonObject.getString("playingStatus");//1打开麦克风，2关闭麦克风，3打开摄像头，4关闭摄像头
                                    showTipsPlaying(anchorPhoto);
                                    break;
                                case CustomAttachmentType.SystemMsg:
                                    // 有人进入直播间了
                                    Logger.d("有人预约直播, numReserve: " + numberReserve);
                                    numberReserve++;
                                    ReserveTv.setText(String.format("已预约：%1$s/%2$s", numberReserve, anchorInfoEntity.getReport_num()));
                                    messageList.add(message);
                                    needRefresh = true;
                                    break;
                                case CustomAttachmentType.SystemMsgOut:
                                    // 有人取消预约直播
                                    Logger.d("有人取消预约直播, numReserve: " + numberReserve);
                                    numberReserve--;
                                    ReserveTv.setText(String.format("已预约：%1$s/%2$s", numberReserve, anchorInfoEntity.getReport_num()));
                                    break;
                                case CustomAttachmentType.ModifyReserveNum:
                                    JSONObject jsonObject1 = jsonObjectTop.getJSONObject("data");
                                    int people = jsonObject1.getInteger("people");
                                    Logger.d("主播修改了预约人数, people: " + people);
                                    anchorInfoEntity.setReport_num(people);
                                    ReserveTv.setText(String.format("已预约：%1$s/%2$s", numberReserve, anchorInfoEntity.getReport_num()));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        messageList.add(message);
                        needRefresh = true;
                    }
                } else {
                    try {
                        // 来自聊天室通知
                        Logger.e("来自聊天室通知");
                        ChatRoomNotificationAttachment attachment = (ChatRoomNotificationAttachment) message.getAttachment();
                        if (attachment.getType() == NotificationType.ChatRoomMemberIn || attachment.getType() == NotificationType.ChatRoomMemberExit || attachment.getType() == NotificationType.LeaveTeam) {
                            //有用户进入或者退出直播间
                            getOnlineMumber();
                            if (attachment.getType() == NotificationType.ChatRoomMemberExit) {
                                List<String> accounts = attachment.getTargets();
                                if (attachment.getTargetNicks() != null) {
                                    for (int i = 0; i < accounts.size(); i++) {
                                        if (accounts.get(i).equalsIgnoreCase(anchorAccid)) {
                                            anchorLeave();//主播退出了直播间
                                        }
                                    }
                                }
                            }
                        }
                        if (attachment.getType() == NotificationType.ChatRoomClose) {
                            stopLiveDialog();
                        }
                        if (attachment.getType() == NotificationType.ChatRoomInfoUpdated) {
                            Map<String, Object> remote = attachment.getExtension();
                            if (remote != null && !remote.isEmpty()) {
                                try {
                                    String avatar = (String) remote.get("avatar");
                                    if (connectLL.isShown() && !avatar.equals("1")) {
                                        return;
                                    }
                                    showLianmaiView(avatar, !avatar.equals("1"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (needRefresh) {
                adapter.setList(messageList);
                doScrollToBottom();
            }
//            messageListPanel.onIncomingMessage(messages);
        }
    };    //这里直播可以用 LivePlayerObserver 点播用 VodPlayerObserver bofagnqi

    @Override
    public StartOneToMoreLivePresenter initPresenter() {
        return new StartOneToMoreLivePresenter(this);
    }

    private final LivePlayerObserver playerObserver = new LivePlayerObserver() {
        @Override
        public void onPreparing() {
        }

        @Override
        public void onPrepared(MediaInfo info) {
            mediaInfo = info;
        }

        @Override
        public void onError(int code, int extra) {
            if (code == CauseCode.CODE_VIDEO_PARSER_ERROR) {
                showToast("视频解析出错");
            } else if (code == CauseCode.CODE_BUFFERING_ERROR) {
                initPlayer();
                player.switchContentUrl(rtmp_pull_url);

            } else if (code == CauseCode.CODE_RTMP_CONNECT_ERROR) {
                stopLiveDialog();
            }
        }

        @Override
        public void onFirstVideoRendered() {
        }

        @Override
        public void onFirstAudioRendered() {
        }

        @Override
        public void onBufferingStart() {
        }

        @Override
        public void onBufferingEnd() {
        }

        @Override
        public void onBuffering(int percent) {
        }

        @Override
        public void onVideoDecoderOpen(int value) {
        }

        @Override
        public void onStateChanged(StateInfo stateInfo) {
            if (stateInfo != null && stateInfo.getCauseCode() == CauseCode.CODE_VIDEO_STOPPED_AS_NET_UNAVAILABLE) {
                showToast("网络已断开");
            }
        }

        @Override
        public void onHttpResponseInfo(int code, String header) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        ActivityTaskManager.getInstance().putSingleInstanceActivity(this);
        isActive = true;
    }

    @Override
    public void baseInitialization() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕长时间亮
        BarUtils.transparentStatusBar(this);
        EventBus.getDefault().register(this);
        messageList = new ArrayList<>();
        isAvater = getIntent().getBooleanExtra("IS_AVATER", false);
        uid = Long.valueOf(getIntent().getIntExtra("UID", 0));
        liveId = getIntent().getStringExtra("LIVE_ID");
        presenter.ancharId = getIntent().getStringExtra("ANCHOR_ID");
        presenter.liveId = liveId;
//        WyToken = Preferences.getLoginInfo().getToken();
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_start_onetomore;
    }

    @Override
    public void viewInitialization() {
        bPermission = ToolUtils.checkPublishPermission(this);
        setupNERtc();
        setupLocalVideo(vv_local_user);
        adapter = new StartLiveAdapter();
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(adapter);

        checkImg.setVisibility(View.GONE);
        registerObservers(true);

        anchorImgRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        anchorImgRv.setVisibility(View.GONE);


        slide_layout.setmListeners(new SlideLayout.DragListener() {
            @Override
            public void onDragToOut(@NonNull @NotNull View dragView) {
                clearScreenIV.setVisibility(View.GONE);

            }

            @Override
            public void onDragToIn(@NonNull @NotNull View dragView) {
                clearScreenIV.setVisibility(View.VISIBLE);
            }
        });

        clearTiming(); // 开启定时清理礼物列表
        initAnim(); // 初始化动画
    }

    @Override
    public void setJoinLiveSuccess(JoinLiveEntity data) {
        joinLiveEntity = data;
        channelId = data.getInfo().getSignal_channel_id();
        roomId = String.valueOf(data.getInfo().getChat().getRoomid());
        liveId = String.valueOf(data.getInfo().getRoom_id());
        uid = Long.valueOf(data.getInfo().getUid());
        push_url = data.getInfo().getPush_url();
        channelName = data.getInfo().getChannelName();
        nertc_token = data.getInfo().getNertc_token();
        rtmp_pull_url = data.getInfo().getRtmp_pull_url();
        userAccid = data.getInfo().getAccid();

        ReserveTv.setSelected(data.getInfo().getIs_appoint() == 1);
        if (lianmaiEntities != null && lianmaiEntities.size() > 0) {
        } else {
            if (isAvater) {
                joinChannel(nertc_token, channelName, uid);
            }
            joinChatRoom();
            initPlayer();
        }

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                //当前时间-基准时间>20秒
                if (SystemClock.elapsedRealtime() - chronometer.getBase() > 20 * 1000) {
                    chronometer.setCountDown(false);
                }
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime() - data.getInfo().getStart_time() * 1000);
        chronometer.setFormat("已播: %s ");
        chronometer.start();
    }

    @Override
    public void setLikeAnNoLike(String msg) {
        if (isLike) {
            attentionTv.setSelected(true);
            attentionTv.setText("+关注");
        } else {
            attentionTv.setSelected(false);
            attentionTv.setText("取消关注");
        }
        isLike = !isLike;
    }

    @Override
    public void setNomomey() {
        BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
        Bundle bundle2 = new Bundle();
        bundle2.putString("showContent", "您剩余的高手币不足，请充值后再进行打赏");
        bundle2.putString("comfirm", "去充值");
        baseTipsDialog1.setArguments(bundle2);
        baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isJump", true);
                gotoActivity(RechargeAct.class, false, bundle);
            }
        });
    }

    @Override
    public void setShareImgSuccess(String logo) {
        showShareImgDialog();
    }

    @Override
    public void giftFormeSuccess(GiftForMeEntity giftForMeEntity) {
        GiftForMeDialog dialog = new GiftForMeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("GIFT_FOR_ME", presenter.giftForMeEntity);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "GiftForMeDialog");
    }

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        // 获取主播信息页
        ReserveTv.setText(String.format("已预约：%1$s/%2$s", data.getAppoint_num(), data.getReport_num()));
        anchorInfoEntity = data;
        numberReserve = data.getAppoint_num();
        GlideUtils.loadImage(this, data.getAvatar(), anchorAvaterCiv);
        GlideUtils.loadImage(this, data.getLevel(), leveImg);
        anchorNameTv.setText(data.getNickname());
        liveIdTv.setText("ID:" + data.getAnchor_no());
        anchorAccid = data.getAnchor_accid();
//        serviceTv.setText("已服务：" + data.getService_num() + "");
//        ReserveTv.setText("已预约：" + data.getService_num() + "");
        if (data.getIs_edit() == 1) {//判断是否是主播
            isAvater = true;
            attentionTv.setVisibility(View.GONE);
        } else {
            isAvater = false;
            attentionTv.setVisibility(View.VISIBLE);
            if (data.getIs_sub() == 1) {
                attentionTv.setText("取消关注");
                isLike = true;
            } else {
                attentionTv.setText("+关注");
                isLike = false;
            }
        }
        if (isAvater) {
            checkOnlineIv.setVisibility(View.VISIBLE);
            checkImg.setVisibility(View.GONE);
        } else {
            LMavatar = userInfoEntity.getAvatar();
            checkImg.setVisibility(View.VISIBLE);
            checkOnlineIv.setVisibility(View.GONE);
        }
        if (isAvater) {
            return;
        }
        //图片展示
        pictureBeanList.clear();
        pictureBeanList.addAll(data.getPicture());
        previewLiveAdapter = new PreviewLiveAdapter(pictureBeanList, this);
        anchorImgRv.setAdapter(previewLiveAdapter);

        previewLiveAdapter.addChildClickViewIds(R.id.anchorListRiv);
        previewLiveAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                ArrayList<String> urls = new ArrayList<>();
                for (int i = 0; i < anchorInfoEntity.getPicture().size(); i++) {
                    urls.add(anchorInfoEntity.getPicture().get(i).getUrl());
                }
                CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                bigPictureDialog.setArguments(bundle);
                bigPictureDialog.show(getSupportFragmentManager(), "CheckBigPictureDialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfoEntity = (UserInfoEntity) ACache.get(this).getAsObject(ACEConstant.USERINFO);
        if (!TextUtils.isEmpty(liveId)) {
            String lasdak = AcacheUtil.getToken(StartOneToMoreLiveAct.this, false).substring(7);
            String url = Url.SOCKET_URL + "many?room_id=" + liveId + "&token=" + lasdak;
            SocketLiveUtils.INSTANCE.onStartCommand(url);
        }
    }

    @Override
    public void sendGiftSuccess(GiftEntity.ListBean giftId) {
        if (audioInputPanel != null) {
            audioInputPanel.sendGift(giftId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int ret : grantResults) {
                if (ret != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            bPermission = true;
            if (TextUtils.isEmpty(rtmp_pull_url)) {
                return;
            }
            initPlayer();
        }
    }

    //初始化拉流播放器
    private void initPlayer() {
        releasePlayer();
        if (isAvater) {
            render_layout.setVisibility(View.GONE);
            vv_local_user.setVisibility(View.VISIBLE);
            anchorControLL.setVisibility(View.VISIBLE);
            return;
        }
        anchorControLL.setVisibility(View.GONE);
        config = new SDKOptions();
        config.privateConfig = new NEPlayerConfig();
        PlayerManager.init(this, config);
        initPlaye2r();
    }

    private void initPlaye2r() {
        config = new SDKOptions();
        config.privateConfig = new NEPlayerConfig();
        PlayerManager.init(this, config);
        VideoOptions options = new VideoOptions();
        options.hardwareDecode = false;
        options.isPlayLongTimeBackground = true;
        options.bufferStrategy = VideoBufferStrategy.ANTI_JITTER;
        player = PlayerManager.buildVodPlayer(this, rtmp_pull_url, options);
        //NONE：无缩放(原始大小)
        //FIT：等比例缩放至长边充满容器，短边可能填充黑边
        //FILL：拉伸至充满容器，画面可能会变形
        //FULL：等比例缩放至短边充满容器，长边可能会被裁剪
        player.setVideoScaleMode(VideoScaleMode.FIT);
        start();
        if (textureView != null) {
            vv_local_user.setVisibility(View.GONE);
            textureView.setVisibility(View.VISIBLE);
            player.setupRenderView(textureView, VideoScaleMode.FIT);
        }
    }

    private void start() {
        player.registerPlayerObserver(playerObserver, true);
        player.start();
    }

    Observer<ChannelCommonEvent> nimOnlineObserver = new Observer<ChannelCommonEvent>() {
        @Override
        public void onEvent(ChannelCommonEvent channelCommonEvent) {
            SignallingEventType eventType = channelCommonEvent.getEventType();
            switch (eventType) {
                case CLOSE://关闭频道
                    ChannelCloseEvent channelCloseEvent = (ChannelCloseEvent) channelCommonEvent;
                    connectLL.setVisibility(View.GONE);
                    break;
                case JOIN://加入频道
                    UserJoinEvent userJoinEvent = (UserJoinEvent) channelCommonEvent;

                    break;
                case INVITE://接收到邀请通知
                    Logger.d("接收到邀请通知");
                    invitedEvent = (InvitedEvent) channelCommonEvent;
                    String customInfo = invitedEvent.getCustomInfo();
                    JSONObject json = JSONObject.parseObject(customInfo);
                    String type = json.getString("type");
                    String avatar = json.getString("avatar");
                    Logger.d("json: " + json);

                    if (!TextUtils.isEmpty(type) && "2".equals(type)) {
                        LianmaiEntity entity = new LianmaiEntity();
                        entity.setAccid(invitedEvent.getFromAccountId());
                        entity.setAvatar(avatar);
                        entity.setInvitedEvent(invitedEvent);
                        boolean isCongfu = false;
                        for (int i = 0; i < lianmaiEntities.size(); i++) {
                            if (lianmaiEntities.get(i).getAccid().equalsIgnoreCase(entity.getAccid())) {
                                isCongfu = true;
                            }
                        }
                        if (!isCongfu) {
                            lianmaiEntities.add(entity);
                        }
                        Logger.d(userInfoEntity.toString());
                        Logger.d(isAvater);
                        if (lianmaiEntities.size() > 1) {//如果多人同时发起连麦，弹出选择框，
                            lianmaiPaidui(lianmaiEntities);
                            connectLL.setVisibility(View.GONE);//收到连麦邀请
                        } else {
                            // 如果同时只有一个人发起连麦，直接显示
                            connectLL.setVisibility(View.VISIBLE);//收到连麦邀请
                            lianmaiYesTv.setVisibility(View.VISIBLE);
                            lianmaiNotv.setVisibility(View.VISIBLE);
                            lianmaiStatusTv.setText("连麦申请中");
                            lianmaiYesTv.setText("同意连麦");
                            lianmaiNotv.setText("拒绝连麦");
                            cancelLianmaiTv.setVisibility(View.GONE);
                            GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, avatar, lianmaiAchonrIv);

//                            if (isAvater) {
//                                GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, avatar, lianmaiAchonrIv);
//                            } else {
//                                GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, userInfoEntity.getAvatar(), lianmaiAchonrIv);
//                            }
                        }
                    }
                    break;
                case CANCEL_INVITE:
                    CanceledInviteEvent canceledInviteEvent = (CanceledInviteEvent) channelCommonEvent;
                    if (lianmaiEntities.size() > 0) {
                        for (int i = 0; i < lianmaiEntities.size(); i++) {
                            if (lianmaiEntities.get(i).getAccid().equalsIgnoreCase(canceledInviteEvent.getFromAccountId())
                                    || lianmaiEntities.get(i).getAccid().equalsIgnoreCase(canceledInviteEvent.getToAccount())) {
                                lianmaiEntities.remove(i--);//i--删除了数据索引变少了，所以要减一下
                            }
                        }
                    }
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    //更新列表数据
                    if (lianmaiDialogPD != null && lianmaiDialogPD.getDialog() != null && lianmaiDialogPD.getDialog().isShowing()) {
                        lianmaiDialogPD.setDataForLianmai(lianmaiEntities);
                    }
//                    lianmaiPaidui(lianmaiEntities);
                    if (lianmaiEntities.size() > 0) {
                        return;
                    }
                    if (lianmaiYesTv.getText().toString().trim().equalsIgnoreCase("断开连麦")) {
                        return;
                    }
                    connectLL.setVisibility(View.GONE);//收到连麦邀请
                    ToastHelper.showToast(StartOneToMoreLiveAct.this, "对方取消连麦");
                    break;
                case REJECT://拒绝
                    connectLL.setVisibility(View.GONE);//收到连麦邀请
                    ToastHelper.showToast(StartOneToMoreLiveAct.this, "对方拒绝连麦");
                    leave();
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    break;
                case ACCEPT://接受
                    lianmainIngView();
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    updateInfoChatroom(LMavatar);
                    if (!isAvater) {
                        releasePlayer();
                        joinChannel(nertc_token, channelName, uid);
//                        setLocalVideoEnable(false);
                        ToastHelper.showToast(StartOneToMoreLiveAct.this, "连麦成功！");
                        return;
                    }


                    break;
                case LEAVE:
                    UserLeaveEvent userLeaveEvent = (UserLeaveEvent) channelCommonEvent;
                    if (lianmaiEntities.size() > 0) {
                        for (int i = 0; i < lianmaiEntities.size(); i++) {
                            if (lianmaiEntities.get(i).getAccid().equalsIgnoreCase(userLeaveEvent.getFromAccountId())) {
                                lianmaiEntities.remove(i--);//i--删除了数据索引变少了，所以要减一下
                            }
                        }
                    }
                    //更新列表数据
                    if (lianmaiDialogPD != null && lianmaiDialogPD.getDialog() != null && lianmaiDialogPD.getDialog().isShowing()) {
                        lianmaiDialogPD.setDataForLianmai(lianmaiEntities);
                    }
                    if (lianmaiEntities.size() > 1) {
                        return;
                    }
                    updateInfoChatroom("1");
                    break;
                case CONTROL:
                    ControlEvent controlEvent = (ControlEvent) channelCommonEvent;
                    connectLL.setVisibility(View.GONE);
                    if (lianmaiEntities.size() > 0) {
                        for (int i = 0; i < lianmaiEntities.size(); i++) {
                            if (lianmaiEntities.get(i).getAccid().equalsIgnoreCase(controlEvent.getFromAccountId())) {
                                lianmaiEntities.remove(i--);//i--删除了数据索引变少了，所以要减一下
                            }
                        }
                    }
                    if (!isAvater) {
                        leaveChannel(true);
                        initPlayer();
                    } else {
                        lianmaiEntities.clear();
                    }


                    updateInfoChatroom("1");
                    leave();
                    break;
            }


        }
    };

    //聊天列表滚动到最下面
    private void doScrollToBottom() {
        comment_list.scrollToPosition(adapter.getItemCount() - 1);
    }

    /**
     * 主播退出了直播间
     */
    private void anchorLeave() {
        BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
        Bundle bundle2 = new Bundle();
        bundle2.putString("showContent", "主播退出了直播间");
        bundle2.putString("comfirm", "退出");
        bundle2.putString("cancel", "取消");
        baseTipsDialog1.setArguments(bundle2);
        baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                finish();
            }
        });
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

    /**
     * 在线成员弹框
     */
    public void getMembers() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("roomId", roomId);
        bundle.putString("ANCHORID", anchorAccid);
        if (memberDialog == null) {
            memberDialog = new OnlineMemberDialog();
        }
        memberDialog.setArguments(bundle);
        memberDialog.show(getSupportFragmentManager(), "OnlineMemberDialog");

    }

    public void lianmaiPaidui(List<LianmaiEntity> lianmaiEntities) {
        Logger.d("aaaaa");
        connectLL.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("roomId", roomId);
        if (lianmaiEntities != null) {
            bundle.putSerializable("Lianmai_list", (Serializable) lianmaiEntities);
        }
        bundle.putString("AnchorACCID", anchorAccid);
        if (lianmaiDialogPD == null) {
            lianmaiDialogPD = new LianmaiDialog();
            lianmaiDialogPD.setArguments(bundle);
            lianmaiDialogPD.show(getSupportFragmentManager(), "OnlineMemberDialog");
            lianmaiDialogPD.setOnLianMaiListtener(new LianmaiDialog.OnLianMaiListtener() {
                @Override
                public void comfirm(LianmaiEntity entity) {
                    invitedEvent = entity.getInvitedEvent();
                    lianmaiEntity = entity;
                    agreeLianmai();
                }
            });
        } else {
            lianmaiDialogPD.setDataForLianmai(lianmaiEntities);
            lianmaiDialogPD.show(getSupportFragmentManager(), "OnlineMemberDialog");
        }
    }

    /**
     * 主播连麦弹框，关注发起连麦
     */
    public void getLianmai() {
        if (isAvater) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("roomId", roomId);
            bundle.putString("AnchorACCID", anchorAccid);
            if (lianmaiDialog == null) {
                lianmaiDialog = new LianmaiDialog();
                lianmaiDialog.setArguments(bundle);
                lianmaiDialog.show(getSupportFragmentManager(), "OnlineMemberDialog");
            } else {
                lianmaiDialog.setArguments(bundle);
                lianmaiDialog.show(getSupportFragmentManager(), "OnlineMemberDialog");
            }
            lianmaiDialog.setOnConfirmListener(new LianmaiDialog.OnBaseTipsListener() {
                @Override
                public void comfirm(String accid, String avatar) {
                    LMavatar = avatar;
                    joinRoom(accid);
                }

                @Override
                public void onBan() {
                    ToastExtKt.showToast(StartOneToMoreLiveAct.this, "该用户已被您禁言，请先解除禁言后，再发起连麦");
                }
            });
        } else {
//            getChannelNumber();
            gzLianmai();
        }


    }

    /**
     * 观众发起连麦
     */
    private void gzLianmai() {
        //判断是否被禁言了
        List<String> userAcidList = new ArrayList<>();
        userAcidList.add(userAccid);
        NIMClient.getService(ChatRoomService.class).fetchRoomMembersByIds(roomId, userAcidList).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> param) {
                ChatRoomMember roomMember = null;
                try {
                    roomMember = param.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (roomMember != null && roomMember.isMuted()) {
                    ToastExtKt.showToast(StartOneToMoreLiveAct.this, "你被禁言，不能发起连麦");
                } else {
                    BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("showContent", "是否和主播发起连麦");
                    bundle2.putString("comfirm", "连麦");
                    baseTipsDialog1.setArguments(bundle2);
                    baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            LMavatar = userInfoEntity.getAvatar();
                            joinRoom(anchorAccid);
                        }
                    });
                }
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 获取信令频道人数
     */
    private void getChannelNumber() {
        if (TextUtils.isEmpty(channelName)) {
            ToastUtils.showShort("发起连麦失败！");
            return;
        }
        NIMClient.getService(SignallingService.class).queryChannelMemberCount(channelName).setCallback(new RequestCallback<Integer>() {
            @Override
            public void onSuccess(Integer param) {
                if (param >= 2) {
                    ToastUtils.showShort("主播占线中，请稍后重新尝试连麦");
                } else {
                    gzLianmai();
                }
            }

            @Override
            public void onFailed(int code) {
                ToastUtils.showShort("发起连麦失败！");
            }

            @Override
            public void onException(Throwable exception) {
                ToastUtils.showShort("发起连麦失败！");
            }
        });
//        InvocationFuture<Integer> queryChannelMemberCount(String channelName);
    }

    /**
     * 发起连麦申请布局显示
     */
    private void LmCalling() {
        connectLL.setVisibility(View.VISIBLE);
        lianmaiStatusTv.setText("连麦申请中");
        cancelLianmaiTv.setVisibility(View.VISIBLE);
        lianmaiNotv.setVisibility(View.GONE);
        lianmaiYesTv.setVisibility(View.GONE);
        GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, LMavatar, lianmaiAchonrIv);
        handler.postDelayed(runnable, 20 * 1000);

    }

    private void joinRoom(String accid) {
        lmAccid = accid;
        if (channelId == null) {
            ToastUtils.showShort("发起连麦失败！");
            return;
        }
        long selfUid = System.nanoTime();
        NIMClient.getService(SignallingService.class).join(channelId, selfUid, "", false).setCallback(new RequestCallbackWrapper<ChannelFullInfo>() {
            @Override
            public void onResult(int i, ChannelFullInfo channelFullInfo, Throwable throwable) {
                if (i == ResponseCode.RES_SUCCESS || i == ResponseCode.RES_CHANNEL_MEMBER_HAS_EXISTS) {
                    inviteOther(accid);
                } else {
                    showToast("加入房间失败 code=" + i);
                }
            }
        });
    }

    private void inviteOther(String account) {
        if (channelId == null) {
            Toast.makeText(this, "请先创建频道或加入频道", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("roomId", roomId);//聊天室ID
        map.put("liveId", liveId);//直播间ID
        map.put("avatar", userInfoEntity.getAvatar());//发起者头像
        map.put("userName", userInfoEntity.getNickname());//发起者用户名
        map.put("type", "2");
//        map.put("consulationID", consulationID);
        String callInformation = JSON.toJSONString(map);
        invitedRequestId = System.currentTimeMillis() + "_id";
        InviteParamBuilder param = new InviteParamBuilder(channelId, account, invitedRequestId);
        param.customInfo(callInformation);
        param.offlineEnabled(true);
        NIMClient.getService(SignallingService.class).invite(param).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                LmCalling();
            }

            @Override
            public void onFailed(int code) {
                ToastUtils.showShort("连麦申请失败");
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    @OnClick({R.id.changeCameraIV, R.id.finishIv, R.id.startInputTv, R.id.cancelLianmaiTv, R.id.checkImg,
            R.id.giftImg, R.id.checkOnlineIv, R.id.liveMoreIv, R.id.liveShareIv, R.id.clearScreenIV,
            R.id.lianmaiIv, R.id.lianmaiYesTv, R.id.lianmaiNotv, R.id.camareIv, R.id.ReserveTv,
            R.id.attentionTv, R.id.microphoneIv})
    public void onclick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.ReserveTv://跳转到一对一页面
                Bundle bundleReserve = new Bundle();
                bundleReserve.putString("ANCHOR_ID", presenter.ancharId);
                gotoActivity(OneToOneQueueAct.class, false, bundleReserve);

                break;
            case R.id.clearScreenIV://回复清屏
//                slide_layout.restoreContent();
//                clearScreenIV.setVisibility(View.GONE);
                break;
            case R.id.changeCameraIV://切换摄像头

                int i = NERtcEx.getInstance().switchCamera();

                break;
            case R.id.attentionTv://关注取消关注
                if (!ToolUtils.isLoginTips(StartOneToMoreLiveAct.this, getSupportFragmentManager())) {
                    return;
                }
                presenter.anchorSub();

                break;
            case R.id.liveMoreIv://点击弹出更多
                LiveMoreDialog liveMoreDialog = new LiveMoreDialog();
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("ISANCHOR", isAvater);
                liveMoreDialog.setArguments(bundle1);
                liveMoreDialog.show(getSupportFragmentManager(), "LiveMoreDialog");
                liveMoreDialog.setOnLiveControlListener(new LiveMoreDialog.OnLiveControlListener() {
                    @Override
                    public void operation(int type) {
                        switch (type) {//1举报 2投屏 3清屏 4分享
                            case 1:
                                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                                    ToastUtils.showShort("青少年模式不能使用该功能");
                                    return;
                                }
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("SUB_TYPE", "2");
                                bundle2.putString("liveId", presenter.liveId);
                                gotoActivity(AppealAct.class, false, bundle2);
                                break;
                            case 2:
                                ToastUtils.showShort("此功能暂未开放");
                                break;
                            case 3:
                                slide_layout.closeDrawer();
                                clearScreenIV.setVisibility(View.VISIBLE);
//                                clear_screen.close();
                                break;
                            case 4:
                                presenter.getShareImg();
//                                showShareImgDialog();
                                break;
                        }
                    }
                });
                break;
            case R.id.camareIv://关闭或者开启摄像头
                //关闭摄像头
                if (camareIv.isSelected()) {
                    sendPlayingStatus("3");
                    setLocalVideoEnable(true);
                    camareIv.setSelected(false);
                } else {
                    sendPlayingStatus("4");
                    camareIv.setSelected(true);
                    setLocalVideoEnable(false);
                }


                break;
            case R.id.microphoneIv:
                //关闭麦克风
                if (microphoneIv.isSelected()) {
                    sendPlayingStatus("1");
                    setLocalAudioEnable(true);
                } else {
                    sendPlayingStatus("2");
                    setLocalAudioEnable(false);
                }
                break;
            case R.id.liveShareIv://微信分享
                presenter.getShareImg();

                break;
            case R.id.checkImg://观众查看主播图片
                if (anchorInfoEntity.getPicture().size() == 0) {
                    ToastUtils.showShort("没有图片展示");
                    return;
                }
                if (isSHowImg) {
                    anchorImgRv.setVisibility(View.GONE);
                } else {
                    anchorImgRv.setVisibility(View.VISIBLE);
                }
                isSHowImg = !isSHowImg;
                break;
            case R.id.finishIv:
                showFinishAct();
//                presenter.closeLive();
//                finish();
                break;
//            case R.id.send:
//
//                onTextMessageSendButtonPressed();
//                break;
            case R.id.checkOnlineIv://获取在线人员禁言
                getMembers();

                break;
            case R.id.lianmaiIv://获取在线人员连麦
                getLianmai();
//                updateInfoChatroom("1");
                break;
            case R.id.cancelLianmaiTv://取消连麦
//                getLianmai();
                cancelInvite();
                break;
            case R.id.lianmaiYesTv://同意连麦
                if (lianmaiYesTv.getText().toString().equalsIgnoreCase("断开连麦")) {
                    closeLianmai();
                    return;
                } else if (lianmaiYesTv.getText().toString().equalsIgnoreCase("同意连麦")) {
                    agreeLianmai();
                }

                break;
            case R.id.lianmaiNotv://拒绝连麦
                if (lianmaiNotv.getText().toString().equalsIgnoreCase("拒绝连麦")) {
                    disAgreeLianmai();
                } else if (lianmaiNotv.getText().toString().equalsIgnoreCase("关闭麦克风")) {
                    setLocalAudioEnable(false);
                    lianmaiNotv.setText("打开麦克风");

                } else if (lianmaiNotv.getText().toString().equalsIgnoreCase("打开麦克风")) {
                    setLocalAudioEnable(true);
                    lianmaiNotv.setText("关闭麦克风");
                }


                break;
            case R.id.startInputTv:
                if (!ToolUtils.isLoginTips(StartOneToMoreLiveAct.this, getSupportFragmentManager())) {
                    return;
                }
                initInputTextMsgDialog();
                break;
            case R.id.giftImg://礼物弹框
                if (!ToolUtils.isLoginTips(StartOneToMoreLiveAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (isAvater) {
                    presenter.getGiftForMe();

                    return;
                }
                presenter.getGiftInfo();


                break;
        }
    }

    /**
     * 发送主播关闭打开通知
     *
     * @param playingStatus
     */
    private void sendPlayingStatus(String playingStatus) {
        if (audioInputPanel != null) {//  1打开麦克风，2关闭麦克风，3打开摄像头，4关闭摄像头
            audioInputPanel.lianMai(playingStatus);
        }
    }

    /**
     * 输入聊天内容
     */
    private void initInputTextMsgDialog() {
        dismissInputDialog();
        if (inputTextLiveDialog == null) {
            inputTextLiveDialog = new InputTextLiveDialog(StartOneToMoreLiveAct.this, R.style.dialog);
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

    /**
     * 断开连麦
     */
    private void closeLianmai() {
        String accid = "";
        if (isAvater) {
            if (lianmaiEntities != null) {
                lianmaiEntities.clear();
            }
            accid = lmAccid;
        } else {
            accid = anchorAccid;
        }
        NIMClient.getService(SignallingService.class).sendControl(channelId, accid, "断开连麦").setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                isLianmai = false;
                connectLL.setVisibility(View.GONE);
                leave();
                if (!isAvater) {
                    leaveChannel(true);
                    initPlayer();
                }
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 拒绝连麦
     */
    private void disAgreeLianmai() {
        if (invitedEvent == null) {
            return;
        }
        if (lianmaiEntities.size() > 0) {
            for (int i = 0; i < lianmaiEntities.size(); i++) {
                if (lianmaiEntities.get(i).getAccid().equalsIgnoreCase(invitedEvent.getFromAccountId())) {
                    lianmaiEntities.remove(i--);//i--删除了数据索引变少了，所以要减一下
                }
            }
        }
        InviteParamBuilder inviteParam = new InviteParamBuilder(channelId,
                invitedEvent.getFromAccountId(),
                invitedEvent.getRequestId());
//        if (!TextUtils.isEmpty(customInfo)) {
//            inviteParam.customInfo(customInfo);
//        }
        NIMClient.getService(SignallingService.class).rejectInvite(inviteParam);
        connectLL.setVisibility(View.GONE);
//        finish();
    }

    @Override
    public void onError(int code, String msg) {
        super.onError(code, msg);
        ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show(msg);
    }

    /**
     * 取消连麦
     */
    private void cancelInvite() {
        try {
            InviteParamBuilder param = new InviteParamBuilder(channelId, lmAccid, invitedRequestId);
            param.offlineEnabled(true);
            NIMClient.getService(SignallingService.class).cancelInvite(param).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    connectLL.setVisibility(View.GONE);
                    leave();
                    if (isAvater) {
                        lianmaiEntities.clear();
                    }
                }

                @Override
                public void onFailed(int code) {
                    leave();
                }

                @Override
                public void onException(Throwable exception) {
                    leave();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 同意连麦
     */
    private void agreeLianmai() {
//        enableClick(false);
        InviteParamBuilder inviteParam = new InviteParamBuilder(invitedEvent.getChannelBaseInfo().getChannelId(), invitedEvent.getFromAccountId(), invitedEvent.getRequestId());
        NIMClient.getService(SignallingService.class).acceptInviteAndJoin(inviteParam, 0).setCallback(
                new RequestCallbackWrapper<ChannelFullInfo>() {

                    @Override
                    public void onResult(int code, ChannelFullInfo channelFullInfo, Throwable throwable) {
                        // 参考官方文档中关于api以及错误码的说明
                        if (code == ResponseCode.RES_SUCCESS) {
                            //接收邀请成功
                            if (!isAvater) {
                                releasePlayer();
                                joinChannel(nertc_token, channelName, uid);
                                isLianmai = true;
                            }
                            Logger.d("LMavatar: " + LMavatar);
                            ToastHelper.showToast(StartOneToMoreLiveAct.this, "连麦成功！");
                            lianmainIngView();

                        } else {
                            ToastHelper.showToast(StartOneToMoreLiveAct.this, "接收邀请返回的结果 ， code = " + code + (throwable == null ? "" : ", throwable = " + throwable.getMessage()));
                        }
                    }
                });
    }

    private void lianmainIngView() {
        if (lianmaiEntity != null) {
            if (isAvater) {
                updateInfoChatroom(lianmaiEntity.getAvatar());
                GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, lianmaiEntity.getAvatar(), lianmaiAchonrIv);
            } else {
                updateInfoChatroom(userInfoEntity.getAvatar());
                GlideUtils.loadHeardImg(StartOneToMoreLiveAct.this, userInfoEntity.getAvatar(), lianmaiAchonrIv);
            }
        }
        connectLL.setVisibility(View.VISIBLE);
        lianmaiStatusTv.setText("连麦中");
        cancelLianmaiTv.setVisibility(View.GONE);
        lianmaiNotv.setVisibility(View.VISIBLE);
        lianmaiYesTv.setVisibility(View.VISIBLE);
        lianmaiNotv.setText("关闭麦克风");
        setLocalAudioEnable(true);
        lianmaiYesTv.setText("断开连麦");
    }

    /**
     * 离开频道,方便再次呼叫，防止返回10407，对方已经频道内
     */
    private void leave() {
        try {
            NIMClient.getService(SignallingService.class).leave(channelId, false, null).setCallback(new RequestCallbackWrapper<Void>() {
                @Override
                public void onResult(int i, Void aVoid, Throwable throwable) {
                    if (i == ResponseCode.RES_SUCCESS) {
                    } else {
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed(String msg) {
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.showShort("消息不能为空");
            return;
        }
        if (container == null) {
            ToastUtils.showShort("进入聊天室失败，请重新进入");
            return;
        }
//        IMMessage textMessage = createTextMessage(text);
        IMMessage textMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, msg);
        container.proxy.sendMessage(textMessage);
//            restoreText(true);
//            messageEditText.setText("");
    }

    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }

    @Override
    public void finish() {
        super.finish();
        ActivityTaskManager.getInstance().removeSingleInstanceActivity(this);
    }

    /**
     * 被停播了
     */
    private void stopLiveDialog() {
        CanFinishDialog finishDialog = new CanFinishDialog();
        Bundle bundleFinish = new Bundle();
        if (isAvater) {
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
    }

    /**
     * 接收到主播关闭打开麦克风和摄像头 //1打开麦克风，2关闭麦克风，3打开摄像头，4关闭摄像头
     */
    private void showTipsPlaying(String anchorPhoto) {
        switch (anchorPhoto) {
            case "1":
            case "3":
                break;
            case "2":
                ToastUtils.showShort("主播关闭了麦克风");
                break;
            case "4":
                ToastUtils.showShort("主播关闭了摄像头");
                break;
        }
    }

    public boolean isMyMessage(ChatRoomMessage message) {
        try {
            return message.getSessionType() == container.sessionType && message.getSessionId() != null && message.getSessionId().equals(container.account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加入房间
     *
     * @param userID 用户ID
     * @param roomID 房间ID
     */
    private void joinChannel(String wyToken, String roomID, long userID) {
        Logger.d("joinChannel");
        NERtcEx.getInstance().setChannelProfile(1);// 0 - COMMUNICATION（通信模式），  1 - LIVE_BROADCASTING（直播模式）
        int i = NERtcEx.getInstance().joinChannel(wyToken, roomID, userID);
        vv_local_user.setZOrderMediaOverlay(true);
        vv_local_user.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_FILL);
        NERtcEx.getInstance().setupLocalVideoCanvas(vv_local_user);
        vv_local_user.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.GONE);
    }

    public void joinChatRoom() {
        if (isJoinChatroom) {
            return;
        }
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        data.setNick(userInfoEntity.getNickname());
        data.setAvatar(userInfoEntity.getAvatar());
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoomEx(data, 1);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                Logger.d("enterChatRoomEx: onSuccess");
                Map<String, Object> remote = result.getRoomInfo().getExtension();
                if (remote != null && !remote.isEmpty()) {
                    for (String key : remote.keySet()) {
                        Logger.d(key);
                        Logger.d(remote.get(key));
                    }
                    try {
                        String avatar = (String) remote.get("avatar");
                        if (connectLL.isShown() || avatar.equals("1")) {
                        } else if (!"1".equalsIgnoreCase(avatar)) {
                            showLianmaiView(avatar, !avatar.equals("1"));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isJoinChatroom = true;
                initMessageFragment();
                if (container != null) {
                    if (joinLiveEntity.getInfo().getChat_notice() != null) {
                        for (int i = 0; i < joinLiveEntity.getInfo().getChat_notice().size(); i++) {
                            SystemAttachment systemAttachment = new SystemAttachment();
                            systemAttachment.setTips(joinLiveEntity.getInfo().getChat_notice().get(i));
                            ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(container.account, systemAttachment);
                            messageList.add(message);
                        }

                        adapter.setList(messageList);
                    }
                    doScrollToBottom();
                } else {
                    ToastUtils.showShort("进入聊天室失败!");
                }


                if (isAvater) {//更新聊天室公告
                    getOnlineMumber();
                    updateInfoChatroom("1");
                }
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(StartOneToMoreLiveAct.this, "加入聊天室失败" + i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(StartOneToMoreLiveAct.this, "加入聊天室错误，" + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 更新聊天室信息，以方便连麦显示
     */
    private void updateInfoChatroom(String avatar) {
        Logger.d("updateInfoChatroom: " + avatar);
        ChatRoomUpdateInfo chatRoomUpdateInfo = new ChatRoomUpdateInfo();
        chatRoomUpdateInfo.setAnnouncement(avatar);
        Map<String, Object> extension = new HashMap<>();
        extension.put("avatar", avatar);
        chatRoomUpdateInfo.setExtension(extension);
        NIMClient.getService(ChatRoomService.class).updateRoomInfo(roomId, chatRoomUpdateInfo, true, extension).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {

            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });

    }

    private void getOnlineMumber() {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.ONLINE_NORMAL, 0, 1000).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                chatRoomMemberList.clear();
                memberAll.clear();
                chatRoomMemberList.addAll(chatRoomMembers);
                getMembersGuest();
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * 非固定成员 (又称临时成员,只有在线时才能在列表中看到,数量无上限)
     */

    public void getMembersGuest() {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.GUEST_DESC, 0, 1000).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                chatRoomMemberList.addAll(chatRoomMembers);
                Set set = new HashSet();
                set.addAll(chatRoomMemberList);
                memberAll.clear();
                memberAll.addAll(set);
                if (onlineMemberTV != null) {
                    onlineMemberTV.setText(memberAll.size() + "");
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void initMessageFragment() {

        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        audioInputPanel = new AudioInputPanel(container, layoutPlayAudio, timer_tip_container, timer, timer_tip, sendAudioIv);
        audioInputPanel.setAudioRecordCallback(new AudioInputPanel.IAudioRecrod() {
            @Override
            public void onStart() {
                if (player != null) {
                    player.setMute(true);
                }
            }

            @Override
            public void onEnd() {
                if (player != null) {
                    player.setMute(false);
                }
            }
        });
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
        videoView.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_FILL);
        NERtcEx.getInstance().setupLocalVideoCanvas(videoView);
        videoView.setVisibility(View.VISIBLE);
    }

    /**
     * 退出房间并关闭页面
     */
    private void exit() {
        stopVideoService();//关闭悬浮窗服务
        if (joinedChannel) {
            leaveChannel(false);
        }
        finish();
    }

    /**
     * 退出房间
     *
     * @return 返回码
     * @see com.netease.lava.nertc.sdk.NERtcConstants.ErrorCode
     */
    private boolean leaveChannel(boolean isLm) {
        joinedChannel = false;
        setLocalAudioEnable(false);
        setLocalVideoEnable(false);
        int ret = NERtcEx.getInstance().leaveChannel();
        if (!isLm) {
            NERtcEx.getInstance().release();
        }
        return ret == NERtcConstants.ErrorCode.OK;

    }

    /**
     * 设置本地音频的可用性
     */
    private void setLocalAudioEnable(boolean enable) {
        if (microphoneIv != null) {
            microphoneIv.setSelected(!enable);
        }
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
//        enableVideoIb.setImageResource(enable ? R.drawable.selector_meeting_close_video : R.drawable.selector_meeting_open_video);
//        localUserVv.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
//        localUserBgV.setBackgroundColor(getResources().getColor(enable ? R.color.white : R.color.black));
    }

    /**
     * 音频和视频同时设置
     *
     * @param enable
     */
    private void setVideoEndAudioEnable(boolean enable) {
        enableLocalVideo = enable;
        NERtcEx.getInstance().enableLocalVideo(enableLocalVideo);
        enableLocalAudio = enable;
        NERtcEx.getInstance().enableLocalAudio(enableLocalAudio);
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeCdnRequestData(cdnReqData, register);
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(nimOnlineObserver, register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SocketLiveUtils.INSTANCE.closeConnect();
            ActivityTaskManager.getInstance().removeSingleInstanceActivity(this);
            stopVideoService();
            cancelInvite();
            leave();
            EventBus.getDefault().unregister(this);
            registerObservers(false);
            if (isAvater) {
                updateInfoChatroom("1");
            }
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            NERtcEx.getInstance().release();
            releasePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (audioInputPanel != null) {
            audioInputPanel.onDestroy();
        }
    }

    /**
     * 关闭频道
     */
    private void closeChannel() {
//        if (!isAvater) {
//            return;
//        }
        if (channelId == null) {
            Toast.makeText(this, "请先创建频道或加入频道", Toast.LENGTH_SHORT).show();
            return;
        }
        NIMClient.getService(SignallingService.class).close(channelId,
                true, "关闭频道的自定义字段").setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
//                channelInfo = null;
            }

            @Override
            public void onFailed(int code) {
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    private void releasePlayer() {
        if (player == null) {
            return;
        }
        player.registerPlayerObserver(playerObserver, false);
        player.setupRenderView(null, VideoScaleMode.NONE);
        textureView.releaseSurface();
//        textureView = null;
        player.stop();
        player = null;

    }

//    @Override
//    public void setSeeLiveInfo(SeeLiveUserEntity data) {
//        this.seeliveEntity = data;
//    }
//
//    @Override
//    public void setDqLiveInfo(OneToOneLiveEntity data) {
//        roomId = String.valueOf(data.getChat().getRoomid());
//        joinLiaoTianSHi();
//        joinChannel(WyToken, roomId, uid);
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (audioInputPanel != null) {
            audioInputPanel.onPause();
        }
        if (isAvater) {
            setVideoEndAudioEnable(false);
        }
    }

    @Override
    public void doBusiness() {
        presenter.getliveInfo();
        presenter.getAnchorInfo();
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

    @Override
    public void onLeaveChannel(int i) {
        PushStream pushStream = this.pushStream;
        this.pushStream = null;
        if (isAvater) {
            return;
        }
        if (pushStream != null) {
            pushStream.stop();
        }
    }

    @Override
    public void onUserJoined(long l) {
    }

    @Override
    public void onUserLeave(long userId, int i) {
        if (pushStream != null) {
            pushStream.setOneToMore(true);
            pushStream.update(userId, false);
        }
        if (connectLL != null) {
            connectLL.setVisibility(View.GONE);
            updateInfoChatroom("1");
            leave();
        }
        if (!isAvater) {
//            initPlayer();
//            ToastUtils.showShort("主播退出了直播间");
            anchorLeave();
//            finish();
        }
    }

    @Override
    public void onUserAudioStart(long l) {
        Logger.d("onUserAudioStart");
    }

    @Override
    public void onUserAudioStop(long l) {
        Logger.d("onUserAudioStop");
    }

    @Override
    public void onUserVideoStart(long l, int i) {
//        otherUid = l;
        if (pushStream != null) {
            pushStream.setOneToMore(true);
            pushStream.update(l, true);
        }
        if (isAvater) {
            return;
        }
        textureView.setVisibility(View.GONE);
        NERtcEx.getInstance().subscribeRemoteVideoStream(l, NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh, true);
        vv_local_user.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_FILL);
        NERtcEx.getInstance().setupRemoteVideoCanvas(vv_local_user, l);


    }

    @Override
    public void onUserVideoStop(long l) {
        Logger.d("onUserVideoStop");
    }

    /**
     * --------------------------------------礼物动画效果-------------------------------------------------------
     */

    @Override
    public void onDisconnect(int i) {
        Logger.d("onDisconnect");
    }

    @Override
    public void onClientRoleChange(int i, int i1) {
        Logger.d("onClientRoleChange");
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        ChatRoomMessage message = (ChatRoomMessage) msg;
        // 检查是否转换成机器人消息
        ChatRoomHelper.buildMemberTypeInRemoteExt(message, roomId);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_CHATROOM_MUTED) {
                            ToastHelper.showToast(NimUIKit.getContext(), "用户被禁言");
                        } else if (code == ResponseCode.RES_CHATROOM_ROOM_MUTED) {
                            ToastHelper.showToast(NimUIKit.getContext(), "全体禁言");
                        } else {
                            ToastHelper.showToast(NimUIKit.getContext(), "消息发送失败：code:" + code);
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        ToastHelper.showToast(NimUIKit.getContext(), "消息发送失败！");
                    }
                });
        if (message.getMsgType() == MsgTypeEnum.custom) {
            String alldata = message.getAttachment().toJson(false);
            JSONObject jsonObjectTop = JSONObject.parseObject(alldata);
            int type = jsonObjectTop.getInteger("type");
            if (type == CustomAttachmentType.RedPacket) {
                showGift(message);
            }
            if (type == CustomAttachmentType.lianmai) {

                return true;
            }
        }
        messageList.add(message);
        adapter.addData(message);
        doScrollToBottom();
//        if (aitManager != null) {
//            aitManager.reset();
//        }
        return true;
    }

    private void showLianmaiView(String anchorPhoto, boolean isShow) {
        if (isShow) {
            connectLL.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(StartOneToMoreLiveAct.this, anchorPhoto, lianmaiAchonrIv);
            lianmaiStatusTv.setText("连麦中");
            lianmaiStatusTv.setVisibility(View.VISIBLE);
            lianmaiYesTv.setVisibility(View.GONE);
            lianmaiNotv.setVisibility(View.GONE);
            cancelLianmaiTv.setVisibility(View.GONE);
        } else {
            if (lianmaiYesTv.getText().toString().equals("断开连麦")) {
                return;
            }
            connectLL.setVisibility(View.GONE);
        }
    }

    private void showGift(ChatRoomMessage tag) {
        String allData = tag.getAttachment().toJson(false);
        CustomizeInfoEntity entity = new CustomizeInfoEntity();
        CustomizeInfoEntity entity1 = entity.goJsonYes(allData);
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
        View giftView = LayoutInflater.from(StartOneToMoreLiveAct.this).inflate(R.layout.item_gift_for_live, null);
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
        GlideUtils.loadImageNoImage(StartOneToMoreLiveAct.this, customizeInfoEntity.getGift_logo(), iv_gift);

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
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(StartOneToMoreLiveAct.this, R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(StartOneToMoreLiveAct.this, R.anim.gift_out); // 礼物退出时动画
    }

    /**
     * ---------------------------------------------------------------------------------------------
     */
    //    private ChatRoomMessage changeToRobotMsg(ChatRoomMessage message) {
//        if (aitManager == null) {
//            return message;
//        }
//        if (message.getMsgType() == MsgTypeEnum.robot) {
//            return message;
//        }
//        String robotAccount = aitManager.getAitRobot();
//        if (TextUtils.isEmpty(robotAccount)) {
//            return message;
//        }
//        String text = message.getContent();
//        String content = aitManager.removeRobotAitString(text, robotAccount);
//        content = content.equals("") ? " " : content;
//        message = ChatRoomMessageBuilder.createRobotMessage(roomId, robotAccount, text, RobotMsgType.TEXT, content, null, null);
//
//        return message;
//    }
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
            public void comfirm(GiftEntity.ListBean giftBean) {
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "是否确认赠送礼物给主播");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
//                                presenter.unbindWechat("1");
                        presenter.sendGift(giftBean);
                    }
                });
            }
        });
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

    @Override
    public void onUserSubStreamVideoStart(long l, int i) {

    }

    @Override
    public void onUserSubStreamVideoStop(long l) {

    }

    @Override
    public void onUserAudioMute(long l, boolean b) {
        Logger.d("onUserAudioMute");
    }

    @Override
    public void onUserVideoMute(long l, boolean b) {
        Logger.d("onUserVideoMute");
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
        Logger.d("onAudioDeviceChanged");
    }

    @Override
    public void onAudioDeviceStateChange(int i, int i1) {
        Logger.d("onAudioDeviceStateChange");
    }

    @Override
    public void onVideoDeviceStageChange(int i) {
        Logger.d("onVideoDeviceStageChange");
    }

    @Override
    public void onConnectionTypeChanged(int i) {
        Logger.d("onConnectionTypeChanged");
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
        Logger.d("onConnectionStateChanged");
//        if (i1 == 2) {//房间被关闭
//            BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
//            Bundle bundle2 = new Bundle();
//            bundle2.putString("showContent", "直播间已被封禁");
//            bundle2.putBoolean("CANCEL_SEE", true);
////            bundle2.putBoolean("isCanfinish", true);
//            baseTipsDialog1.setArguments(bundle2);
//            baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
//            baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
//                @Override
//                public void comfirm() {
////                                presenter.unbindWechat("1");
//                    exit();
//                }
//            });
//        }
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
        Logger.d("onError");
    }

    @Override
    public void onWarning(int i) {
        Logger.d("onWarning");
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
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.onActivityStop(false);
        }
    }

    @Override
    public void onBackPressed() {
//        exit();
        showFinishAct();
    }

    @Override
    public void setfinish() {
        exit();
    }

    private void showFinishAct() {
        Logger.e("showFinishAct");
        if (baseTipsDialog != null && baseTipsDialog.getDialog() != null && baseTipsDialog.getDialog().isShowing()) {
            return;
        }
        baseTipsDialog = new BaseTipsDialog();
        Bundle bundle1 = new Bundle();
        bundle1.putString("showContent", "是否确定退出直播间？");
        bundle1.putString("TITLE_DIO", "退出直播间");
        if (isAvater) {
            bundle1.putString("cancel", "结束直播");
            bundle1.putString("comfirm", "取消");
        } else {
            bundle1.putString("cancel", "最小化");
            bundle1.putString("comfirm", "退出");
        }
        baseTipsDialog.setArguments(bundle1);
        baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog.setOnTwoBaseTipsListener(new BaseTipsDialog.OnTwoBaseTipsListener() {
            @Override
            public void comfirm() {
                //退出直播间
                if (!isAvater) {
                    exit();
                }
                baseTipsDialog.dismiss();
//                if (!isAvater) {
//                exit();
//                    return;
//                }
//                presenter.closeLive();
//                baseTipsDialog.dismiss();
            }

            @Override
            public void cancel() {
                //最小化
                if (isAvater) {
                    presenter.closeLive();
                    return;
                }
                if (isLianmai) {
                    ToastUtils.showShort("连麦过程中不可以最小化");
                    return;
                }
                showFloatingView(StartOneToMoreLiveAct.this);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //关闭悬浮窗服务
        stopVideoService();
        presenter.getliveInfo();
        // 关闭悬浮窗之后从新设置视频布局
        if (camareIv.isSelected()) {
            setLocalVideoEnable(false);
        } else {
            setLocalVideoEnable(true);
        }
        if (microphoneIv.isSelected()) {
            setLocalAudioEnable(false);
        } else {
            setLocalAudioEnable(true);
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
            if (!Settings.canDrawOverlays(StartOneToMoreLiveAct.this)) {
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
                    XXPermissions.with(StartOneToMoreLiveAct.this).permission(Permission.SYSTEM_ALERT_WINDOW).request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            startVideoService();
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
        moveTaskToBack(true);//最小化Activity
        boolean isWork = isServiceWork(this, FloatingViewMoreService.class.getCanonicalName());
        Logger.e("startVideoService-isWork=" + isWork);
        if (!isWork) {
            releasePlayer();
            Intent intent = new Intent(this, FloatingViewMoreService.class);//开启服务显示悬浮框
            bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    /**
     * 关闭悬浮窗服务
     */
    public void stopVideoService() {
        boolean isWork = isServiceWork(this, FloatingViewMoreService.class.getCanonicalName());
        Logger.e("stopVideoService-isWork=" + isWork);
        if (isWork) {
            unbindService(mVideoServiceConnection);//不显示悬浮框
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param className 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String className) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doTradeListEvent(OTOEvent tradeListEvent) {
        if (tradeListEvent != null) {
            switch (tradeListEvent.getMsg_id()) {
                case 403://强制停播
                case 404://强制停播
                    CanFinishDialog finishDialog = new CanFinishDialog();
                    Bundle bundleFinish = new Bundle();
                    if (isAvater) {
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


}