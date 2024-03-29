package com.netease.nim.uikit.business.session.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.ait.AitManager;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.actions.GiftFromMeAction;
import com.netease.nim.uikit.business.session.actions.ImageAction;
import com.netease.nim.uikit.business.session.actions.ScoreAction;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftEvent;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.business.session.activity.my.dialog.MessageAssessDialog2;
import com.netease.nim.uikit.business.session.activity.my.dialog.gift.BaseTipsDialog;
import com.netease.nim.uikit.business.session.activity.my.dialog.gift.GiftDialog;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.dialogFragment.gift.GiftForMeDialog;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.input.InputPanel;
import com.netease.nim.uikit.business.session.module.list.MessageListPanelEx;
import com.netease.nim.uikit.business.session.myCustom.extension.RedPacketAttachment;
import com.netease.nim.uikit.business.session.myCustom.extension.ReservationMsgAttachment;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.robot.model.RobotMsgType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 聊天界面基类
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends TFragment implements ModuleProxy {

    protected static final String TAG = "MessageActivity";
    // p2p对方Account或者群id
    protected String sessionId;
    protected SessionTypeEnum sessionType;
    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;
    protected AitManager aitManager;
    GiftDialog giftDialog;
    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            onMessageIncoming(messages);
        }
    };
    private View rootView;
    private SessionCustomization customization;
    private GiftEntity entity;
    private GiftForMeEntity giftForMeEntity;
    private ImageView sendGiftIV;

    /**
     * 已读回执观察者
     */
    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            messageListPanel.receiveReceipt();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parseIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
        sendGiftIV = rootView.findViewById(R.id.sendGiftIV);
        sendGiftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendScoreCustorm();
                GoSettingEvent goSettingEvent = new GoSettingEvent(sessionId);
                goSettingEvent.setGift(true);
                EventBus.getDefault().post(goSettingEvent);

//                ToastHelper.showToast(getActivity(), "sadfa");221133

            }
        });
        return rootView;
    }

    private void showSendGiftView() {
        giftDialog = new GiftDialog();
        Bundle bundle = new Bundle();
        if (entity != null) {
            bundle.putSerializable("GIFT_INFO", entity);
        }
        giftDialog.setArguments(bundle);
        giftDialog.show(getChildFragmentManager(), "GiftDialog");
        giftDialog.setSelectGiftListener(new GiftDialog.OnSelectGiftListener() {
            @Override
            public void comfirm(GiftEntity.ListBean giftEntity) {
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "是否确认赠送礼物给主播");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getChildFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
//                                presenter.unbindWechat("1");
//                                presenter.sendGift(giftEntity);
                        GoSettingEvent goSettingEvent = new GoSettingEvent(sessionId);
                        goSettingEvent.setSendGift(true);
                        goSettingEvent.setListBean(giftEntity);
                        EventBus.getDefault().post(goSettingEvent);
                    }
                });
            }
        });
    }

    /**
     * 处理uiKit传过来的操作
     *
     * @param goSettingEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doGiftInfo(GiftEvent goSettingEvent) {
        if (goSettingEvent != null) {
            if (goSettingEvent.isSHowScore()) {//弹出评价弹框
                showScoreDialog();
                return;
            }
            if (goSettingEvent.getMessageCanScoreEntity() != null) {//聊天公告以及是否能评分
                if (goSettingEvent.isSeeNotic()) {
//                    P2PMessageActivity activity = (P2PMessageActivity) getActivity();
                    ((P2PMessageActivity) getActivity()).setTopMstView(goSettingEvent.getMessageCanScoreEntity().getNotice_str());
                    return;
                }
                if (goSettingEvent.getMessageCanScoreEntity().getIs_comment() == 0) {
                    showScoreDialog();
                } else {
                    ToastHelper.showToast(getActivity(), "你已经评价了");
                }

//                sendScoreCustorm();
                return;
            }
            if (goSettingEvent.getGiftEntity() != null) {//赠送礼物
                entity = goSettingEvent.getGiftEntity();
                showSendGiftView();
                return;
            }
            if (goSettingEvent.isSendGift()) {//调用接口成功后发送自定义礼物消息
                if (goSettingEvent.isGOtoRecharge()) {//没钱跳转到充值
                    BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("showContent", "您剩余的高手币不足，请充值后再进行打赏");
                    bundle2.putString("comfirm", "去充值");
                    baseTipsDialog1.setArguments(bundle2);
                    baseTipsDialog1.show(getChildFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            GoSettingEvent goSettingEvent = new GoSettingEvent(null);
                            goSettingEvent.setSendGift(false);
                            goSettingEvent.setGoRecharge(true);
                            goSettingEvent.setListBean(null);
                            EventBus.getDefault().post(goSettingEvent);
                        }
                    });

                    return;
                }
                sendCustorm(goSettingEvent.getGiftBean());
                return;
            }
            if (goSettingEvent.getGiftForMeEntity() != null) {//获取收到的礼物列表
                giftForMeEntity = goSettingEvent.getGiftForMeEntity();
                showGiftForMeView();
                return;
            }

            if (!TextUtils.isEmpty(goSettingEvent.getReOrder())) {//提示框，再次预约主播提醒

                messageListPanel.refreshMessageList();
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", goSettingEvent.getReOrder());
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getChildFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {

                    }
                });
            }
        }

    }

    private void showScoreDialog() {
        MessageAssessDialog2 dialog = new MessageAssessDialog2(requireActivity());
        dialog.setOnConfirmListener(new MessageAssessDialog2.OnBaseTipsListener() {
            @Override
            public void comfirm(int abilityNumber, int srviceNumber) {
                GoSettingEvent goSettingEvent = new GoSettingEvent(sessionId);
                goSettingEvent.setAblitityNumber(abilityNumber);
                goSettingEvent.setServiceNumber(srviceNumber);
                EventBus.getDefault().post(goSettingEvent);
            }
        });
        dialog.show();
    }

    private void sendScoreCustorm() {
        List<String> objects = new ArrayList<>();
        objects.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1649494614&t=abc86ea6455a2533c8180e91095274a1");
        objects.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1649494614&t=abc86ea6455a2533c8180e91095274a1");
        ReservationMsgAttachment attachment = new ReservationMsgAttachment();
        // 红包id，红包信息，红包名称
        attachment.setImgs(objects);
        attachment.setReservation_message("中国故事 今天是2022年03月10日 星期四 全景中国 VR、航拍等技术手段展示祖国大好河山 " +
                "大国工匠 讲述不同岗位的劳动者以匠人之心追求技艺的极致 一带一路 记录身边的“一带一路”普通网友寻找丝路");
        IMMessage message;
        message = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, "[评价我吧]", attachment);

        sendMessage(message);
    }

    /**
     * 礼物弹框
     */
    private void showGiftForMeView() {
        if (giftForMeEntity == null || giftForMeEntity.getList().size() == 0) {
            ToastHelper.showToast(getActivity(), "暂时没有收到礼物哦");
            return;
        }
        GiftForMeDialog dialog = new GiftForMeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("GIFT_FOR_ME", giftForMeEntity);
        bundle.putBoolean("SHI_XIN", true);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "GiftForMeDialog");


    }

    private void sendCustorm(GiftEntity.ListBean giftBean) {
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
        attachment.setGift_id(String.valueOf(giftBean.getId()));
        attachment.setGift_logo(giftBean.getLogo());
        attachment.setGift_name(giftBean.getTitle());

        IMMessage message;
        message = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, "[赠送 " + giftBean.getTitle() + "]", attachment);

        sendMessage(message);


    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListPanel.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        // 默认使用听筒播放
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        GoSettingEvent goSettingEvent = new GoSettingEvent(sessionId);
        goSettingEvent.setCanScore(true);
        goSettingEvent.setSeeNotic(true);
        EventBus.getDefault().post(goSettingEvent);
//        NimHttpClient.getInstance().execute("https://api.fengxiangzb.com/live/gift",);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
        EventBus.getDefault().unregister(this);
        registerObservers(false);
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }
        if (aitManager != null) {
            aitManager.reset();
        }
    }

    public boolean onBackPressed() {
        return inputPanel.collapse(true);
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        Bundle arguments = getArguments();
        sessionId = arguments.getString(Extras.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) arguments.getSerializable(Extras.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) arguments.getSerializable(Extras.EXTRA_ANCHOR);

        customization = (SessionCustomization) arguments.getSerializable(Extras.EXTRA_CUSTOMIZATION);
        Container container = new Container(getActivity(), sessionId, sessionType, this, true);

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList());
            inputPanel.setCustomization(customization);
        } else {
            inputPanel.reload(container, customization);
        }

        initAitManager();

        inputPanel.switchRobotMode(NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null);

        registerObservers(true);

        if (customization != null) {
            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
        }
    }

    private void initAitManager() {
        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            aitManager = new AitManager(getContext(), options.aitTeamMember && sessionType == SessionTypeEnum.Team ? sessionId : null, options.aitIMRobot);
            inputPanel.addAitTextWatcher(aitManager);
            aitManager.setTextChangeListener(inputPanel);
        }
    }

    /**
     * ************************* 消息收发 **********************************
     */
    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return customization.isAllowSendMessage(message);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        // 已读回执监听
        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
            service.observeMessageReceipt(messageReceiptObserver, register);
        }
    }

    private void onMessageIncoming(List<IMMessage> messages) {
        if (CommonUtil.isEmpty(messages)) {
            return;
        }
        messageListPanel.onIncomingMessage(messages);
        // 发送已读回执
        messageListPanel.sendReceipt();
    }

    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
        if (isAllowSendMessage(message)) {
            appendTeamMemberPush(message);
            message = changeToRobotMsg(message);
            appendPushConfigAndSend(message);
        } else {
            // 替换成tip
            message = MessageBuilder.createTipMessage(message.getSessionId(), message.getSessionType());
            message.setContent("该消息无法发送");
            message.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(message, false);
        }

        messageListPanel.onMsgSend(message);
        if (aitManager != null) {
            aitManager.reset();
        }
        return true;
    }

    private void appendPushConfigAndSend(IMMessage message) {
        final IMMessage msg = message;
        appendPushConfig(message);
        MsgService service = NIMClient.getService(MsgService.class);
        // send message to server and save to db
        final IMMessage replyMsg = inputPanel.getReplyMessage();
        if (replyMsg == null) {
            service.sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                }

                @Override
                public void onFailed(int code) {
                    sendFailWithBlackList(code, msg);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        } else {
            service.replyMessage(message, replyMsg, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    String threadId = message.getThreadOption().getThreadMsgIdClient();
                    messageListPanel.refreshMessageItem(threadId);
                }

                @Override
                public void onFailed(int code) {
                    sendFailWithBlackList(code, msg);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
        inputPanel.resetReplyMessage();
    }

    // 被对方拉入黑名单后，发消息失败的交互处理
    private void sendFailWithBlackList(int code, IMMessage msg) {
        if (code == ResponseCode.RES_IN_BLACK_LIST) {
            // 如果被对方拉入黑名单，发送的消息前不显示重发红点
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            messageListPanel.refreshMessageList();
            // 同时，本地插入被对方拒收的tip消息
            IMMessage tip = MessageBuilder.createTipMessage(msg.getSessionId(), msg.getSessionType());
            tip.setContent(getActivity().getString(R.string.black_list_send_tip));
            tip.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            tip.setConfig(config);
            NIMClient.getService(MsgService.class).saveMessageToLocal(tip, true);
        }
    }

    private void appendTeamMemberPush(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (sessionType == SessionTypeEnum.Team) {
            List<String> pushList = aitManager.getAitTeamMember();
            if (pushList == null || pushList.isEmpty()) {
                return;
            }
            MemberPushOption memberPushOption = new MemberPushOption();
            memberPushOption.setForcePush(true);
            memberPushOption.setForcePushContent(message.getContent());
            memberPushOption.setForcePushList(pushList);
            message.setMemberPushOption(memberPushOption);
        }
    }

    private IMMessage changeToRobotMsg(IMMessage message) {
        if (aitManager == null) {
            return message;
        }
        if (message.getMsgType() == MsgTypeEnum.robot) {
            return message;
        }
        if (isChatWithRobot()) {
            if (message.getMsgType() == MsgTypeEnum.text && message.getContent() != null) {
                String content = message.getContent().equals("") ? " " : message.getContent();
                message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), message.getSessionId(), content, RobotMsgType.TEXT, content, null, null);
            }
        } else {
            String robotAccount = aitManager.getAitRobot();
            if (TextUtils.isEmpty(robotAccount)) {
                return message;
            }
            String text = message.getContent();
            String content = aitManager.removeRobotAitString(text, robotAccount);
            content = content.equals("") ? " " : content;
            message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), robotAccount, text, RobotMsgType.TEXT, content, null, null);

        }
        return message;
    }

    private boolean isChatWithRobot() {
        return NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null;
    }

    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
        if (customConfig == null) {
            return;
        }
        String content = customConfig.getPushContent(message);
        Map<String, Object> payload = customConfig.getPushPayload(message);
        if (!TextUtils.isEmpty(content)) {
            message.setPushContent(content);
        }
        if (payload != null) {
            message.setPushPayload(payload);
        }

    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onItemFooterClick(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (messageListPanel.isSessionMode()) {
            RobotAttachment attachment = (RobotAttachment) message.getAttachment();
            NimRobotInfo robot = NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(attachment.getFromRobotAccount());
            aitManager.insertAitRobot(robot.getAccount(), robot.getName(), inputPanel.getEditSelectionStart());
        }
    }

    @Override
    public void onReplyMessage(IMMessage message) {
        inputPanel.setReplyMessage(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (aitManager != null) {
            aitManager.onActivityResult(requestCode, resultCode, data);
        }
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new ImageAction());
        actions.add(new ScoreAction());
        actions.add(new GiftFromMeAction());
//        actions.add(new VideoAction());
//        actions.add(new LocationAction());

        if (customization != null && customization.actions != null) {
            actions.addAll(customization.actions);
        }
        return actions;
    }

}
