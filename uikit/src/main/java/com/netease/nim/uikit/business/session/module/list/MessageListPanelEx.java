package com.netease.nim.uikit.business.session.module.list;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.preference.UserPreferences;
import com.netease.nim.uikit.business.robot.parser.elements.group.LinkElement;
import com.netease.nim.uikit.business.session.activity.MsgSelectActivity;
import com.netease.nim.uikit.business.session.activity.VoiceTrans;
import com.netease.nim.uikit.business.session.activity.my.GiftEvent;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.helper.MessageHelper;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.viewholder.robot.RobotLinkView;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.IRecyclerView;
import com.netease.nim.uikit.common.ui.recyclerview.listener.OnItemClickListener;
import com.netease.nim.uikit.common.ui.recyclerview.loadmore.MsgListFetchLoadMoreView;
import com.netease.nim.uikit.common.util.log.sdk.wrapper.NimLog;
import com.netease.nim.uikit.common.util.media.BitmapDecoder;
import com.netease.nim.uikit.common.util.sys.ClipboardUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RevokeMsgNotification;
import com.netease.nimlib.sdk.msg.model.SessionMsgDeleteOption;
import com.netease.nimlib.sdk.msg.model.TeamMessageReceipt;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.robot.model.RobotMsgType;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.session.MessageReceiptHelper;

import org.apache.lucene.portmobile.util.Objects;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 基于RecyclerView的消息收发模块
 *
 * @author huangjun
 * @date 2016/12/27
 */
public class MessageListPanelEx {
    public static final int REQUEST_CODE_FORWARD_PERSON = 0x01;
    public static final int REQUEST_CODE_FORWARD_TEAM = 0x02;
    /**
     * MsgSelectActivity的识别码
     */
    public static final int REQUEST_CODE_FORWARD_MULTI_RETWEET = 0x03;
    /**
     * WatchMultiRetweetActivity的识别码，用于二次转发
     */
    public static final int REQUEST_CODE_WATCH_MULTI_RETWEET = 0x04;
    private final static String TAG = "MessageListPanelEx";
    /**
     * 背景图片缓存
     */
    private static Pair<String, Bitmap> background;
    private static Comparator<IMMessage> comp = new Comparator<IMMessage>() {

        @Override
        public int compare(IMMessage o1, IMMessage o2) {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time < 0 ? -1 : 1);
        }
    };
    /**
     * container
     */
    private Container container;
    private View rootView;
    /**
     * message list view
     */
    private RecyclerView messageListView;
    private List<IMMessage> items;
    private MsgAdapter adapter;
    private ImageView ivBackground;
    /**
     * 新消息到达提醒
     */
    private IncomingMsgPrompt incomingMsgPrompt;
    private Handler uiHandler;
    /**
     * 仅显示消息记录，不接收和发送消息
     */
    private boolean recordOnly;
    /**
     * 从服务器拉取消息记录
     */
    private boolean remote;
    /**
     * 语音转文字
     */
    private VoiceTrans voiceTrans;
    /**
     * 待转发消息
     */
    private IMMessage forwardMessage;
    /**
     * 是否忽略缓存记录，拉取消息时存储被清除的消息
     */
    private boolean persistClear;
    /**
     * 如果在发需要拍照 的消息时，拍照回来时页面可能会销毁重建，重建时会在MessageLoader 的构造方法中调一次 loadFromLocal
     * 而在发送消息后，list 需要滚动到底部，又会通过RequestFetchMoreListener 调用一次 loadFromLocal
     * 所以消息会重复
     */
    private boolean mIsInitFetchingLocal;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(IRecyclerView adapter, View view, int position) {

        }

        @Override
        public void onItemLongClick(IRecyclerView adapter, View view, int position) {
        }

        @Override
        public void onItemChildClick(IRecyclerView adapter2, View view, int position) {

            if (!isSessionMode() || !(view instanceof RobotLinkView)) {
                return;
            }

            RobotLinkView robotLinkView = (RobotLinkView) view;
            LinkElement element = robotLinkView.getElement();
            if (element == null) {
                return;
            }
            if (LinkElement.TYPE_URL.equals(element.getType())) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri contentUrl = Uri.parse(element.getTarget());
                intent.setData(contentUrl);
                try {
                    container.activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    ToastHelper.showToast(container.activity, "路径错误");
                }

            } else if (LinkElement.TYPE_BLOCK.equals(element.getType())) {
                // 发送点击的block
                IMMessage message = adapter.getItem(position);
                if (message != null) {
                    String robotAccount = ((RobotAttachment) message.getAttachment()).getFromRobotAccount();
                    IMMessage robotMsg = MessageBuilder.createRobotMessage(message.getSessionId(),
                            message.getSessionType(),
                            robotAccount,
                            robotLinkView.getShowContent(),
                            RobotMsgType.LINK,
                            "",
                            element.getTarget(),
                            element.getParams());
                    container.proxy.sendMessage(robotMsg);
                }
            }
        }
    };
    /**
     * 消息状态变化观察者
     */
    private Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                NimLog.i(TAG, String.format("uuid: %s, callbackExt: %s", message.getUuid(), message.getCallbackExtension()));
                onMessageStatusChange(message);
            }
        }
    };
    /**
     * 消息附件上传/下载进度观察者
     */
    private Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };
    /**
     * 消息撤回观察者
     */
    private Observer<RevokeMsgNotification> revokeMessageObserver = new Observer<RevokeMsgNotification>() {
        @Override
        public void onEvent(RevokeMsgNotification notification) {
            if (notification == null || notification.getMessage() == null) {
                return;
            }
            IMMessage message = notification.getMessage();
            // 获取通知类型： 1表示是离线，2表示是漫游 ，默认 0
            NimLog.i(TAG, String.format("notification type=%s, postscript=%s, attach=%s, callbackExt=%s",
                    notification.getNotificationType(), notification.getCustomInfo(), notification.getAttach(),
                    notification.getCallbackExt()));

            if (!container.account.equals(message.getSessionId())) {
                return;
            }

            deleteItem(message, false);
        }
    };
    /**
     * 群消息已读回执观察者
     */
    private Observer<List<TeamMessageReceipt>> teamMessageReceiptObserver = new Observer<List<TeamMessageReceipt>>() {
        @Override
        public void onEvent(List<TeamMessageReceipt> teamMessageReceipts) {
            for (TeamMessageReceipt teamMessageReceipt : teamMessageReceipts) {
                int index = getItemIndex(teamMessageReceipt.getMsgId());
                if (index >= 0 && index < items.size()) {
                    refreshViewHolderByIndex(index);
                }
            }
        }
    };
    private Observer<IMMessage> deleteMsgSelfObserver =
            (Observer<IMMessage>) message -> deleteItem(message, true, false);
    private Observer<List<IMMessage>> deleteMsgSelfBatchObserver =
            (Observer<List<IMMessage>>) msgList -> deleteItems(msgList, true, false);
    private Observer<List<SessionMsgDeleteOption>> deleteSessionHistoryMsgsObserver = (optionList) -> {
        for (SessionMsgDeleteOption option : optionList) {
            deleteItemsRange(option.getSessionId(), option.getSessionType(), 0, option.getTime());
        }
    };
    /**
     * 用户信息观察者
     */
    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (container.sessionType == SessionTypeEnum.P2P) {
                if (accounts.contains(container.account) || accounts.contains(NimUIKit.getAccount())) {
                    adapter.notifyDataSetChanged();
                }
            } else { // 群的，简单的全部重刷
                adapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 本地消息接收观察者
     */
    private MessageListPanelHelper.LocalMessageObserver incomingLocalMessageObserver = new MessageListPanelHelper.LocalMessageObserver() {
        @Override
        public void onAddMessage(IMMessage message) {
            if (message == null || !container.account.equals(message.getSessionId())) {
                return;
            }
            onMsgSend(message);
        }

        @Override
        public void onClearMessages(String account) {
            items.clear();
//            refreshMessageList();
            adapter.notifyDataSetChanged();
            adapter.fetchMoreEnd(null, true);
        }
    };

    public MessageListPanelEx(Container container, View rootView, boolean recordOnly, boolean remote) {
        this(container, rootView, null, recordOnly, remote, true);
    }

    public MessageListPanelEx(Container container, View rootView, IMMessage anchor, boolean recordOnly, boolean remote) {
        this(container, rootView, anchor, recordOnly, remote, true);
    }

    public MessageListPanelEx(Container container, View rootView, IMMessage anchor, boolean recordOnly, boolean remote, boolean persistClear) {
        this.container = container;
        this.rootView = rootView;
        this.recordOnly = recordOnly;
        this.remote = remote;
        this.persistClear = persistClear;

        init(anchor);
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable(), false);
    }

    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    public void onBackPressed() {
        uiHandler.removeCallbacks(null);
        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
    }

    public void reload(Container container, IMMessage anchor) {
        this.container = container;
        if (adapter != null) {
            adapter.clearData();
        }
        initFetchLoadListener(anchor);
    }

    private void init(IMMessage anchor) {
        initListView(anchor);

        this.uiHandler = new Handler();
        if (!recordOnly) {
            incomingMsgPrompt = new IncomingMsgPrompt(container.activity, rootView, messageListView, adapter, uiHandler);
        }

        registerObservers(true);
    }

    private void initListView(IMMessage anchor) {
        ivBackground = rootView.findViewById(R.id.message_activity_background);

        // RecyclerView
        messageListView = rootView.findViewById(R.id.messageListView);
        messageListView.setLayoutManager(new LinearLayoutManager(container.activity));
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    container.proxy.shouldCollapseInputPanel();
                }
            }
        });
        messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // adapter
        items = new ArrayList<>();
        adapter = new MsgAdapter(messageListView, items, container);
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
        adapter.setEventListener(new MsgItemEventListener());
        initFetchLoadListener(anchor);
        messageListView.setAdapter(adapter);
        messageListView.addOnItemTouchListener(listener);
    }

    public boolean isSessionMode() {
        return !recordOnly && !remote;
    }

    private void initFetchLoadListener(IMMessage anchor) {
        MessageLoader loader = new MessageLoader(anchor, remote);
        if (recordOnly && !remote) {
            // 双向Load
            adapter.setOnFetchMoreListener(loader);
            adapter.setOnLoadMoreListener(loader);
        } else {
            // 只下来加载old数据
            adapter.setOnFetchMoreListener(loader);
        }
    }

    // 刷新消息列表
    public void refreshMessageList() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshMessageItem(String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return;
        }
        IMMessage msg;
        for (int i = 0; i < items.size(); ++i) {
            msg = items.get(i);
            if (msg == null) {
                continue;
            }
            if (uuid.equals(msg.getUuid())) {
                refreshViewHolderByIndex(i);
                break;
            }
        }
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToBottom();
            }
        }, 200);
    }

    private void doScrollToBottom() {
        messageListView.scrollToPosition(adapter.getBottomDataPosition());
    }

    public void onIncomingMessage(List<IMMessage> messages) {
        boolean needScrollToBottom = isLastMessageVisible();
        boolean needRefresh = false;
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            if (isMyMessage(message)) {
                MsgTypeEnum msgType = message.getMsgType();

                try {
                    if (MsgTypeEnum.tip.equals(msgType)) {
                        String link = "";
                        int type = 1;
                        Map<String, Object> remote = message.getRemoteExtension();
                        if (remote != null && !remote.isEmpty()) {
                            link = (String) remote.get("link");
                            try {
                                type = (int) remote.get("type");
                            } catch (Exception e) {

                            }
                        }
                        if (!TextUtils.isEmpty(link)) {
                            MsgDirectionEnum direct = message.getDirect();
                            if (direct == MsgDirectionEnum.Out || type == 2) {
                                continue;
                            }
                        }
                    }
                    JSONObject jsonObjectTop = JSONObject.parseObject(message.getAttachment().toJson(false));
                    int type = jsonObjectTop.getInteger("type");
                    if (type == 8) {
                        MsgDirectionEnum direct = message.getDirect();
                        if (direct == MsgDirectionEnum.Out) {
                            continue;
                        }
                        GiftEvent giftEvent = new GiftEvent();
                        giftEvent.setSHowScore(true);
                        EventBus.getDefault().post(giftEvent);
                    }
                } catch (Exception e) {

                } finally {
                    items.add(message);
                    addedListItems.add(message);
                    needRefresh = true;
                }


            }
        }
        if (needRefresh) {
            sortMessages(items);
            adapter.notifyDataSetChanged();
        }

        adapter.updateShowTimeItem(addedListItems, false, true);

        // incoming messages tip
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg)) {
            if (needScrollToBottom) {
                doScrollToBottom();
            } else if (incomingMsgPrompt != null && lastMsg.getSessionType() != SessionTypeEnum.ChatRoom) {
                incomingMsgPrompt.show(lastMsg);
            }
        }
    }

    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) messageListView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= adapter.getBottomDataPosition();
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        if (!container.account.equals(message.getSessionId())) {
            return;
        }
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, false, true);

        adapter.appendData(message);

        doScrollToBottom();
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortMessages(List<IMMessage> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    /**
     * ************************* 观察者 ********************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
        service.observeRevokeMessage(revokeMessageObserver, register);
        service.observeTeamMessageReceipt(teamMessageReceiptObserver, register);
        service.observeDeleteMsgSelf(deleteMsgSelfObserver, register);
        service.observeDeleteMsgSelfBatch(deleteMsgSelfBatchObserver, register);
        service.observeDeleteSessionHistoryMsgs(deleteSessionHistoryMsgsObserver, register);
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
        MessageListPanelHelper.getInstance().registerObserver(incomingLocalMessageObserver, register);
    }


    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
//            IMMessage item = items.get(index);
//            item.setStatus(message.getStatus());
//            item.setAttachStatus(message.getAttachStatus());
//            // 处理语音、音视频通话
//            if (item.getMsgType() == MsgTypeEnum.audio || item.getMsgType() == MsgTypeEnum.avchat) {
//                item.setAttachment(message.getAttachment()); // 附件可能更新了
//            }
//
            items.set(index, message);
//            // resend的的情况，可能时间已经变化了，这里要重新检查是否要显示时间
            List<IMMessage> msgList = new ArrayList<>(1);
            msgList.add(message);
            adapter.updateShowTimeItem(msgList, false, true);
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    private boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     */
    private void refreshViewHolderByIndex(final int index) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (index < 0) {
                    return;
                }
                adapter.notifyDataItemChanged(index);
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void setChattingBackground(String uriString, int color) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            if (uri.getScheme().equalsIgnoreCase("file") && uri.getPath() != null) {
                ivBackground.setImageBitmap(getBackground(uri.getPath()));
            } else if (uri.getScheme().equalsIgnoreCase("android.resource")) {
                List<String> paths = uri.getPathSegments();
                if (paths == null || paths.size() != 2) {
                    return;
                }
                String type = paths.get(0);
                String name = paths.get(1);
                String pkg = uri.getHost();
                int resId = container.activity.getResources().getIdentifier(name, type, pkg);
                if (resId != 0) {
                    ivBackground.setBackgroundResource(resId);
                }
            }
        } else if (color != 0) {
            ivBackground.setBackgroundColor(color);
        }
    }

    private void setEarPhoneMode(boolean earPhoneMode, boolean update) {
        if (update) {
            UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        }
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }

    private Bitmap getBackground(String path) {
        if (background != null && path.equals(background.first) && background.second != null) {
            return background.second;
        }

        if (background != null && background.second != null) {
            background.second.recycle();
        }

        Bitmap bitmap = null;
        if (path.startsWith("/android_asset")) {
            String asset = path.substring(path.indexOf("/", 1) + 1);
            try {
                InputStream ais = container.activity.getAssets().open(asset);
                bitmap = BitmapDecoder.decodeSampled(ais, ScreenUtil.screenWidth, ScreenUtil.screenHeight);
                ais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapDecoder.decodeSampled(path, ScreenUtil.screenWidth, ScreenUtil.screenHeight);
        }
        background = new Pair<>(path, bitmap);
        return bitmap;
    }

    /**
     * 收到已读回执（更新VH的已读label）
     */
    public void receiveReceipt() {
        updateReceipt(items);
        refreshMessageList();
    }

    private void updateReceipt(final List<IMMessage> messages) {
        boolean find = false;
        boolean update = false;

        for (int i = messages.size() - 1; i >= 0; i--) {
            if (receiveReceiptCheck(messages.get(i))) {
                find = true;
                update = !TextUtils.equals(adapter.getUuid(), messages.get(i).getUuid());
                if (!update) {
                    NimLog.e(TAG, String.format("receiveReceiptCheck uuid is the same:%s %s %s", messages.get(i).getUuid(), messages.get(i).getContent(), messages.get(i).getAttachStr()));
                }

                adapter.setUuid(messages.get(i).getUuid());
                break;
            }
        }

        long timeTag = MessageReceiptHelper.getReceivedReceiptTime(container.account);
        NimLog.e(TAG, String.format("updateReceipt find:%s update:%s size:%s timeTag:%s", find, update, messages.size(), timeTag));
    }

    private boolean receiveReceiptCheck(final IMMessage msg) {
        return msg != null
                && msg.getSessionType() == SessionTypeEnum.P2P
                && msg.getDirect() == MsgDirectionEnum.Out
                && msg.getMsgType() != MsgTypeEnum.tip
                && msg.getMsgType() != MsgTypeEnum.notification
                && msg.isRemoteRead();

    }

    /**
     * 发送已读回执（需要过滤）
     */

    public void sendReceipt() {
        // 查询全局已读回执功能开关配置
        if (!NimUIKitImpl.getOptions().shouldHandleReceipt) {
            return;
        }

        if (container.account == null || container.sessionType != SessionTypeEnum.P2P) {
            return;
        }

        IMMessage message = getLastReceivedMessage();
        if (!sendReceiptCheck(message)) {
            return;
        }

        NIMClient.getService(MsgService.class).sendMessageReceipt(container.account, message);
    }

    private IMMessage getLastReceivedMessage() {
        IMMessage lastMessage = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (sendReceiptCheck(items.get(i))) {
                lastMessage = items.get(i);
                break;
            }
        }

        return lastMessage;
    }

    private boolean sendReceiptCheck(final IMMessage msg) {
        return msg != null
                && msg.getDirect() == MsgDirectionEnum.In
                && msg.getMsgType() != MsgTypeEnum.tip
                && msg.getMsgType() != MsgTypeEnum.notification;
    }

    // 删除消息
    private void deleteItem(IMMessage messageItem, boolean isRelocateTime) {
        deleteItem(messageItem, isRelocateTime, true);
    }

    private void deleteItem(IMMessage messageItem, boolean isRelocateTime, boolean recordOpe) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem, !recordOpe);
        List<IMMessage> messages = new ArrayList<>();
        for (IMMessage message : items) {
            if (message.getUuid().equals(messageItem.getUuid())) {
                continue;
            }
            messages.add(message);
        }
        updateReceipt(messages);
        adapter.deleteItem(messageItem, isRelocateTime);
    }

    private void deleteItems(List<IMMessage> messageList, boolean isRelocateTime, boolean recordOpe) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageList, !recordOpe);
        List<IMMessage> messages = new ArrayList<>();
        Set<String> itemUuidSet = MessageHelper.getUuidSet(items);
        for (IMMessage message : messageList) {
            if (itemUuidSet.contains(message.getUuid())) {
                continue;
            }
            messages.add(message);
        }
        updateReceipt(messages);
        adapter.deleteItems(messageList, isRelocateTime);
    }

    private void deleteItemsRange(String sessionId, SessionTypeEnum sessionType, long start, long end) {
        if (!container.account.equals(sessionId) || container.sessionType != sessionType) {
            return;
        }
        NIMClient.getService(MsgService.class).deleteRangeHistory(sessionId, sessionType, start, end);
        adapter.deleteItemsRange(start, end, true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
        if (CommonUtil.isEmpty(selected)) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_FORWARD_TEAM:
                doForwardMessage(forwardMessage, selected.get(0), SessionTypeEnum.Team);
                break;
            case REQUEST_CODE_FORWARD_PERSON:
                doForwardMessage(forwardMessage, selected.get(0), SessionTypeEnum.P2P);
                break;
            case REQUEST_CODE_FORWARD_MULTI_RETWEET:
            case REQUEST_CODE_WATCH_MULTI_RETWEET:
                int typeExtra = data.getIntExtra(Extras.EXTRA_TYPE, SessionTypeEnum.None.getValue());
                doForwardMessage((IMMessage) data.getSerializableExtra(Extras.EXTRA_DATA), selected.get(0), SessionTypeEnum.typeOfValue(typeExtra));
                break;
            default:
                break;
        }
    }

    // 转发消息
    private void doForwardMessage(IMMessage forwardMessage, final String sessionId, SessionTypeEnum sessionTypeEnum) {
        IMMessage message;
        if (forwardMessage.getMsgType() == MsgTypeEnum.robot) {
            message = buildForwardRobotMessage(sessionId, sessionTypeEnum);
        } else {
            message = MessageBuilder.createForwardMessage(forwardMessage, sessionId, sessionTypeEnum);
        }
        if (message == null) {
            ToastHelper.showToast(container.activity, "该类型不支持转发");
            return;
        }
        if (container.proxySend) {
            container.proxy.sendMessage(message);
        } else {
            NIMClient.getService(MsgService.class).sendMessage(message, false);
            if (container.account.equals(sessionId)) {
                onMsgSend(message);
            }
        }
    }

    private IMMessage buildForwardRobotMessage(final String sessionId, SessionTypeEnum sessionTypeEnum) {
        if (forwardMessage.getMsgType() == MsgTypeEnum.robot && forwardMessage.getAttachment() != null) {
            RobotAttachment robotAttachment = (RobotAttachment) forwardMessage.getAttachment();
            if (robotAttachment.isRobotSend()) {
                return null; // 机器人发的消息不能转发了
            }
            return MessageBuilder.createTextMessage(sessionId, sessionTypeEnum, forwardMessage.getContent());
        }
        return null;
    }

    /**
     * ***************************************** 数据加载 *********************************************
     */
    private class MessageLoader implements BaseFetchLoadAdapter.RequestLoadMoreListener, BaseFetchLoadAdapter.RequestFetchMoreListener {

        private int loadMsgCount = NimUIKitImpl.getOptions().messageCountLoadOnce;

        private QueryDirectionEnum direction = null;

        private IMMessage anchor;
        private boolean remote;

        private boolean firstLoad = true;
        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                mIsInitFetchingLocal = false;
                if (code != ResponseCode.RES_SUCCESS || exception != null) {
                    if (direction == QueryDirectionEnum.QUERY_OLD) {
                        adapter.fetchMoreFailed();
                    } else if (direction == QueryDirectionEnum.QUERY_NEW) {
                        adapter.loadMoreFail();
                    }

                    return;
                }

                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };

        public MessageLoader(IMMessage anchor, boolean remote) {
            this.anchor = anchor;
            this.remote = remote;
            if (remote) {
                loadFromRemote();
            } else {
                if (anchor == null) {
                    loadFromLocal(QueryDirectionEnum.QUERY_OLD);
                    mIsInitFetchingLocal = true;
                } else {
                    loadAnchorContext(); // 加载指定anchor的上下文
                }
            }
        }

        private void loadAnchorContext() {
            // query new, auto load old
            direction = QueryDirectionEnum.QUERY_NEW;
            NIMClient.getService(MsgService.class)
                    .queryMessageListEx(anchor(), direction, loadMsgCount, true)
                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                        @Override
                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                return;
                            }
                            onAnchorContextMessageLoaded(messages);
                        }
                    });
        }

        private void loadFromLocal(QueryDirectionEnum direction) {
            if (mIsInitFetchingLocal) {
                return;
            }
            this.direction = direction;
            NIMClient.getService(MsgService.class)
                    .queryMessageListEx(anchor(), direction, loadMsgCount, true)
                    .setCallback(callback);
        }

        private void loadFromRemote() {
            this.direction = QueryDirectionEnum.QUERY_OLD;
            NIMClient.getService(MsgService.class)
//            (anchor, 0, limit, QueryDirectionEnum.QUERY_OLD, persist, null, persistClear, null)
                    .pullMessageHistoryExType(anchor(), 0, loadMsgCount, QueryDirectionEnum.QUERY_OLD,
                            null, true, MessageListPanelEx.this.persistClear)
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return anchor == null ? MessageBuilder.createEmptyMessage(container.account, container.sessionType, 0) : anchor;
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        private void onMessageLoaded(final List<IMMessage> messages) {

            if (messages == null) {
                return;
            }

            boolean noMoreMessage = messages.size() < loadMsgCount;

            if (remote) {
                Collections.reverse(messages);
            }

            // 在第一次加载的过程中又收到了新消息，做一下去重
            if (firstLoad && items.size() > 0) {
                for (IMMessage message : messages) {
                    int removeIndex = 0;
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            adapter.remove(removeIndex);
                            break;
                        }
                        removeIndex++;
                    }
                }
            }

            // 加入anchor
            if (firstLoad && anchor != null) {
                messages.add(anchor);
            }

            // 在更新前，先确定一些标记
            List<IMMessage> total = new ArrayList<>(items);
            boolean isBottomLoad = direction == QueryDirectionEnum.QUERY_NEW;
            if (isBottomLoad) {
                total.addAll(messages);
            } else {
                total.addAll(0, messages);
            }
            // 更新要显示时间的消息
            adapter.updateShowTimeItem(total, true, firstLoad);
            // 更新已读回执标签
            updateReceipt(total);

            // 加载状态修改,刷新界面
            if (isBottomLoad) {
                // 底部加载
                if (noMoreMessage) {
                    adapter.loadMoreEnd(messages, true);
                } else {
                    adapter.loadMoreComplete(messages);
                }
            } else {
                // 顶部加载
                if (noMoreMessage) {
                    adapter.fetchMoreEnd(messages, true);
                } else {
                    adapter.fetchMoreComplete(messages);
                }
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                doScrollToBottom();
                sendReceipt(); // 发送已读回执
            }

            // 通过历史记录加载的群聊消息，需要刷新一下已读未读最新数据
            if (container.sessionType == SessionTypeEnum.Team) {
                NIMClient.getService(TeamService.class).refreshTeamMessageReceipt(messages);
            }

            firstLoad = false;
        }

        private void onAnchorContextMessageLoaded(final List<IMMessage> messages) {
            if (messages == null) {
                return;
            }

            if (remote) {
                Collections.reverse(messages);
            }

            int loadCount = messages.size();
            if (firstLoad && anchor != null) {
                messages.add(0, anchor);
            }

            // 在更新前，先确定一些标记
            adapter.updateShowTimeItem(messages, true, firstLoad); // 更新要显示时间的消息
            updateReceipt(messages); // 更新已读回执标签

            // new data
            if (loadCount < loadMsgCount) {
                adapter.loadMoreEnd(messages, true);
            } else {
                adapter.appendData(messages);
            }

            firstLoad = false;
        }

        @Override
        public void onFetchMoreRequested() {
            // 顶部加载历史数据
            if (remote) {
                loadFromRemote();
            } else {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            }
        }

        @Override
        public void onLoadMoreRequested() {
            // 底部加载新数据
            if (!remote) {
                loadFromLocal(QueryDirectionEnum.QUERY_NEW);
            }
        }
    }

    private class MsgItemEventListener extends MsgAdapter.BaseViewHolderEventListener {

        @Override
        public void onFailedBtnClick(IMMessage message) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                if (message.getStatus() == MsgStatusEnum.fail) {
                    resendMessage(message); // 重发
                } else {
                    if (message.getAttachment() instanceof FileAttachment) {
                        FileAttachment attachment = (FileAttachment) message.getAttachment();
                        if (TextUtils.isEmpty(attachment.getPath())
                                && TextUtils.isEmpty(attachment.getThumbPath())) {
                            showReDownloadConfirmDlg(message);
                        }
                    } else {
                        resendMessage(message);
                    }
                }
            } else {
                showReDownloadConfirmDlg(message);
            }
        }

        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {
            if (container.proxy.isLongClickEnabled()) {
//                showLongClickAction(item);
            }
            return true;
        }

        @Override
        public void onFooterClick(IMMessage message) {
            // 与 robot 对话
            container.proxy.onItemFooterClick(message);
        }

        // 重新下载(对话框提示)
        private void showReDownloadConfirmDlg(final IMMessage message) {
            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
                @Override
                public void doCancelAction() {
                }

                @Override
                public void doOkAction() {
                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment) {
                        NIMClient.getService(MsgService.class).downloadAttachment(message, true);
                    }
                }
            };

            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
                    container.activity.getString(R.string.repeat_download_message), true, listener);
            dialog.show();
        }

        // 重发消息到服务器
        private void resendMessage(IMMessage message) {
            // 重置状态为unsent
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                IMMessage item = items.get(index);
                item.setStatus(MsgStatusEnum.sending);
                deleteItem(item, true);
                onMsgSend(item);
            }

            NIMClient.getService(MsgService.class).sendMessage(message, true);
        }

        /**
         * ****************************** 长按菜单 ********************************
         */

        // 长按消息Item后弹出菜单控制
        private void showLongClickAction(IMMessage selectedItem) {
            NimLog.i(TAG, String.format("msg long clicked: {uuid: %s, content: %s, serverId: %s, type: %s subtype: %s}",
                    selectedItem.getUuid(),
                    selectedItem.getContent(),
                    selectedItem.getServerId(),
                    selectedItem.getMsgType(),
                    selectedItem.getSubtype()));
            onNormalLongClick(selectedItem);
        }

        /**
         * 长按菜单操作
         *
         * @param item
         */
        private void onNormalLongClick(IMMessage item) {
            CustomAlertDialog alertDialog = new CustomAlertDialog(container.activity);
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);

            prepareDialogItems(item, alertDialog);
            alertDialog.show();
        }

        // 长按消息item的菜单项准备。如果消息item的MsgViewHolder处理长按事件(MsgViewHolderBase#onItemLongClick),且返回为true，
        // 则对应项的长按事件不会调用到此处
        private void prepareDialogItems(final IMMessage selectedItem, CustomAlertDialog alertDialog) {
            MsgTypeEnum msgType = selectedItem.getMsgType();

            MessageAudioControl.getInstance(container.activity).stopAudio();

            // 0 reply
//            longClickItemReply(selectedItem, alertDialog, msgType);

            // 1 copy
            longClickItemCopy(selectedItem, alertDialog, msgType);
            // 2 forward
            if (!NimUIKitImpl.getMsgForwardFilter().shouldIgnore(selectedItem) && !recordOnly) {
                // forward to person
                longClickItemForwardToPerson(selectedItem, alertDialog);
                // forward to team
                longClickItemForwardToTeam(selectedItem, alertDialog);
            }
            // 3 collect
            // 4 pin
            // 5 revoke
            if (enableRevokeButton(selectedItem)) {
                longClickRevokeMsg(selectedItem, alertDialog, null);
                longClickRevokeMsg(selectedItem, alertDialog, "WithoutNotification");
            }
            // 6 delete
            longClickItemDelete(selectedItem, alertDialog, true);

            longClickItemDelete(selectedItem, alertDialog, false);

            // 7 multiple selection
            longClickItemMultipleSelection(selectedItem, alertDialog);

            // 8 temp 单向删除
            longClickItemDeleteSelf(selectedItem, alertDialog);

            // 9 临时 播放器转换
            longClickItemEarPhoneMode(alertDialog, msgType);

            // 10 临时 语音转文字
            longClickItemVoidToText(selectedItem, alertDialog, msgType);
        }

        private boolean enableRevokeButton(IMMessage selectedItem) {
            String account = NimUIKit.getAccount();
            if (selectedItem.getStatus() == MsgStatusEnum.success
                    && !NimUIKitImpl.getMsgRevokeFilter().shouldIgnore(selectedItem)
                    && !recordOnly) {
                if (selectedItem.getDirect() == MsgDirectionEnum.Out || Objects.equals(selectedItem.getFromAccount(), account)) {
                    return true;
                } else if (NimUIKit.getOptions().enableTeamManagerRevokeMsg && selectedItem.getSessionType() == SessionTypeEnum.Team) {
                    TeamMember member = NimUIKit.getTeamProvider().getTeamMember(selectedItem.getSessionId(), account);
                    return member != null && (member.getType() == TeamMemberType.Owner || member.getType() == TeamMemberType.Manager);
                }
            }
            return false;
        }

        // 长按菜单项 -- 音频转文字
        private void longClickItemVoidToText(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            if (msgType != MsgTypeEnum.audio) return;

            if (item.getDirect() == MsgDirectionEnum.In && item.getAttachStatus() != AttachStatusEnum.transferred)
                return;
            if (item.getDirect() == MsgDirectionEnum.Out && item.getAttachStatus() != AttachStatusEnum.transferred)
                return;

            alertDialog.addItem(container.activity.getString(R.string.voice_to_text), new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    onVoiceToText(item);
                }
            });
        }

        // 语音转文字
        private void onVoiceToText(IMMessage item) {
            if (voiceTrans == null)
                voiceTrans = new VoiceTrans(container.activity);
            voiceTrans.voiceToText(item);
            if (item.getDirect() == MsgDirectionEnum.In && item.getStatus() != MsgStatusEnum.read) {
                item.setStatus(MsgStatusEnum.read);
                NIMClient.getService(MsgService.class).updateIMMessageStatus(item);
                adapter.notifyDataSetChanged();
            }
        }

        // 长按菜单项 -- 听筒扬声器切换
        private void longClickItemEarPhoneMode(CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            if (msgType != MsgTypeEnum.audio) {
                return;
            }

            String content = UserPreferences.isEarPhoneModeEnable() ? "切换成扬声器播放" : "切换成听筒播放";
            final String finalContent = content;
            alertDialog.addItem(content, () -> {
                ToastHelper.showToast(container.activity, finalContent);
                setEarPhoneMode(!UserPreferences.isEarPhoneModeEnable(), true);
            });
        }

        //长按菜单项--回复
        private void longClickItemReply(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
            alertDialog.addItem(container.activity.getString(R.string.reply_has_blank), new CustomAlertDialog.onSeparateItemClickListener() {
                @Override
                public void onClick() {
                    container.proxy.onReplyMessage(item);
                }
            });
        }

        // 长按菜单项--复制
        private void longClickItemCopy(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {

            if (msgType == MsgTypeEnum.text
                    || (msgType == MsgTypeEnum.robot && item.getAttachment() != null && !((RobotAttachment) item.getAttachment()).isRobotSend())) {
                alertDialog.addItem(container.activity.getString(R.string.copy_has_blank), new CustomAlertDialog.onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        onCopyMessageItem(item);
                    }
                });
            }
        }

        private void onCopyMessageItem(IMMessage item) {
            ClipboardUtil.clipboardCopyText(container.activity, item.getContent());
        }

        // 长按菜单项--转发到个人
        private void longClickItemForwardToPerson(final IMMessage item, CustomAlertDialog alertDialog) {
            alertDialog.addItem(container.activity.getString(R.string.forward_to_person), new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    forwardMessage = item;
                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
                    option.title = "选择转发的人";
                    option.type = ContactSelectActivity.ContactSelectType.BUDDY;
                    option.multi = false;
                    option.maxSelectNum = 1;
                    NimUIKit.startContactSelector(container.activity, option, REQUEST_CODE_FORWARD_PERSON);
                }
            });
        }

        // 长按菜单项--转发到群组
        private void longClickItemForwardToTeam(final IMMessage item, CustomAlertDialog alertDialog) {
            alertDialog.addItem(container.activity.getString(R.string.forward_to_team), new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    forwardMessage = item;
                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
                    option.title = "选择转发的群";
                    option.type = ContactSelectActivity.ContactSelectType.TEAM;
                    option.multi = false;
                    option.maxSelectNum = 1;
                    NimUIKit.startContactSelector(container.activity, option, REQUEST_CODE_FORWARD_TEAM);
                }
            });
        }

        // 长按菜单项--撤回消息
        private void longClickRevokeMsg(final IMMessage item, CustomAlertDialog alertDialog, String attach) {
            alertDialog.addItem(container.activity.getString(!TextUtils.isEmpty(attach) ? R.string.withdrawn_msg_not_show_notification : R.string.withdrawn_msg_show_notification), () -> {
                if (!NetworkUtil.isNetAvailable(container.activity)) {
                    ToastHelper.showToast(container.activity, R.string.network_is_not_available);
                    return;
                }
                Map<String, Object> payload = null;

                CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
                if (customConfig != null) {
                    payload = customConfig.getPushPayload(item);
                }
                NIMClient.getService(MsgService.class).revokeMessage(item, "撤回一条消息", payload, false, "", attach).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        deleteItem(item, false);
                        MessageHelper.getInstance().onRevokeMessage(item, NimUIKit.getAccount());
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_OVERDUE) {
                            ToastHelper.showToast(container.activity, R.string.revoke_failed);
                        } else {
                            ToastHelper.showToast(container.activity, "revoke msg failed, code:" + code);
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            });
        }

        // 长按菜单项--删除
        private void longClickItemDelete(final IMMessage selectedItem, CustomAlertDialog alertDialog, boolean recordOpe) {
            if (recordOnly) {
                return;
            }
            alertDialog.addItem(container.activity.getString(R.string.delete_has_blank) + (recordOpe ? "record" : "ignore"), new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    deleteItem(selectedItem, true, recordOpe);
                }
            });
        }

        // 长按菜单项--多选
        private void longClickItemMultipleSelection(IMMessage selectedItem, CustomAlertDialog alertDialog) {
            alertDialog.addItem(container.activity.getString(R.string.multiple_selection), new CustomAlertDialog.onSeparateItemClickListener() {
                @Override
                public void onClick() {
                    // 长按消息触发，因此items不会为空
                    int size = items.size();
                    // 查询被长按的消息位置
                    int selectedPosition = size - 1;
                    for (int i = 0; i < size; ++i) {
                        if (selectedItem.isTheSame(items.get(i))) {
                            selectedPosition = i;
                            break;
                        }
                    }

                    MsgSelectActivity.startForResult(REQUEST_CODE_FORWARD_MULTI_RETWEET, container.activity, items.get(0), items.size(), selectedPosition);
                }
            });
        }

        // 长按菜单项--单向删除
        private void longClickItemDeleteSelf(IMMessage selectedItem, CustomAlertDialog alertDialog) {
            alertDialog.addItem(container.activity.getString(R.string.delete_msg_self), new CustomAlertDialog.onSeparateItemClickListener() {
                @Override
                public void onClick() {
                    NIMClient.getService(MsgService.class).deleteMsgSelf(selectedItem, "").setCallback(new RequestCallback<Long>() {
                        @Override
                        public void onSuccess(Long param) {
                            NimLog.i(TAG, "delete self succeed");
                            deleteItem(selectedItem, true, false);
                        }

                        @Override
                        public void onFailed(int code) {
                            NimLog.i(TAG, "failed to delete self, code=" + code);
                        }

                        @Override
                        public void onException(Throwable exception) {
                            NimLog.i(TAG, "delete self error, e=" + exception);
                        }
                    });
                }
            });
        }

    }
}
