package com.pro.maluli.module.main.base;

import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftEvent;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avsignalling.SignallingServiceObserver;
import com.netease.nimlib.sdk.avsignalling.constant.SignallingEventType;
import com.netease.nimlib.sdk.avsignalling.event.CanceledInviteEvent;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCloseEvent;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.ControlEvent;
import com.netease.nimlib.sdk.avsignalling.event.InviteAckEvent;
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserJoinEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserLeaveEvent;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.eventBus.SettingSeeView;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.PackageUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.common.view.dialogview.NoticeDialog;
import com.pro.maluli.common.view.dialogview.PrivacyDialog;
import com.pro.maluli.common.view.dialogview.TeenagerDialog;
import com.pro.maluli.common.view.dialogview.TeenagerNoSeeDialog;
import com.pro.maluli.module.app.BKGSApplication;
import com.pro.maluli.module.home.base.HomeFrag;
import com.pro.maluli.module.home.oneToOne.answerPhone.AnswerPhoneAct;
import com.pro.maluli.module.main.base.presenter.DialogActivity;
import com.pro.maluli.module.main.base.presenter.IMainContraction;
import com.pro.maluli.module.main.base.presenter.MainPresenter;
import com.pro.maluli.module.message.base.MessageFrag;
import com.pro.maluli.module.message.fromUikit.messageSetting.MessageSettingAct;
import com.pro.maluli.module.myself.anchorInformation.base.AnchorInformationAct;
import com.pro.maluli.module.myself.base.MyselfFrag;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;
import com.pro.maluli.module.myself.setting.youthMode.base.YouthModeAct;
import com.pro.maluli.module.socketService.event.CallEvent;
import com.pro.maluli.module.video.base.SmallVideoFrag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class MainActivity extends BaseMvpActivity<IMainContraction.View, MainPresenter> implements IMainContraction.View {
    private int[] resId = new int[]{R.id.rbMainTabHome, R.id.rbMainTabWealth, R.id.rbMainTabMessage, R.id.rbMainTabMine};
    private List<Fragment> fragments = new ArrayList<>();
    private int position = 0;
    private FragmentTransaction ft;
    private Fragment currentFragment;
    private int finishWait = 2000;
    private int timeMillis = 0;
    private long exitTime = 0;
    private RadioButton rbMainTabHome, rbMainTabWealth, rbMainTabMessage, rbMainTabMine;
    private RadioGroup rgMainTab;
    private String token;
    private String imAccount, imToken;
    private boolean isinitCall;
    private String text = "asdadasda";
    @BindView(R.id.haveNews_iv)
    TextView haveNews_iv;
    @BindView(R.id.customerNewTv)
    View customerNewTv;
    @BindView(R.id.messageTestLL)
    LinearLayout messageTestLL;

    public String getText1() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        EventBus.getDefault().register(this);

        token = AcacheUtil.getToken(this, false);
        imAccount = Preferences.getLoginInfo().getAccount();
        imToken = Preferences.getLoginInfo().getToken();
    }

    @Override
    public int setR_Layout() {
        return R.layout.activity_main;
    }

    @Override
    public void viewInitialization() {
        rbMainTabHome = findViewById(R.id.rbMainTabHome);
        rbMainTabWealth = findViewById(R.id.rbMainTabWealth);
        rbMainTabMessage = findViewById(R.id.rbMainTabMessage);
        rbMainTabMine = findViewById(R.id.rbMainTabMine);
        rgMainTab = findViewById(R.id.rgMainTab);

        rbMainTabHome.setOnClickListener(v -> switchFragment(0));
        rbMainTabWealth.setOnClickListener(v -> switchFragment(1));
        rbMainTabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.switchFragment(2);
            }
        });
        rbMainTabMine.setOnClickListener(v -> switchFragment(3));
        initEvent();
        registerObserve(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        rgMainTab.check(resId[position]);
        if (ToolUtils.isLogin(MainActivity.this, false)) {
            presenter.getUserInfo();
//        if (!canAutoLogin()) {
            loginUiKit();
            setNosee();
            presenter.getMessageList();
        } else {
            haveNews_iv.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
//        GSYVideoManager.releaseAllVideos();
//        try {
//            GSYVideoManager.instance().getPlayer().release();
//        } catch (Exception e) {
//
//        }
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            // 更新未读数变化
            setNosee();
        }
    };
    /**
     * 在线状态监听
     */
    private Observer<StatusCode> onlineStatus = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            if (statusCode.wontAutoLoginForever()) {
                //被其他端的登录踢掉和 被同时在线的其他端主动踢掉
                DialogActivity.showDialogActivity(MainActivity.this);
            }
        }
    };

    private void setNosee() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        unreadNum = unreadNum + systemNews;
        if (unreadNum > 0 && ToolUtils.isLogin(this, false)) {
            haveNews_iv.setText("" + unreadNum);
            haveNews_iv.setVisibility(View.VISIBLE);
        } else {
            haveNews_iv.setVisibility(View.GONE);
        }
    }

    //收到的邀请参数,reject 用到
//    private InvitedEvent invitedEvent;
    Observer<ChannelCommonEvent> nimOnlineObserver = new Observer<ChannelCommonEvent>() {
        @Override
        public void onEvent(ChannelCommonEvent channelCommonEvent) {
            SignallingEventType eventType = channelCommonEvent.getEventType();
            switch (eventType) {
                case CLOSE:
                    ChannelCloseEvent channelCloseEvent = (ChannelCloseEvent) channelCommonEvent;
                    break;
                case JOIN:
                    UserJoinEvent userJoinEvent = (UserJoinEvent) channelCommonEvent;
                    break;
                case INVITE://接收到邀请通知
                    InvitedEvent invitedEvent = (InvitedEvent) channelCommonEvent;
                    String lkda = invitedEvent.getChannelBaseInfo().getChannelExt();
                    String customInfo = invitedEvent.getCustomInfo();
                    JSONObject json = JSONObject.parseObject(customInfo);
                    String type = json.getString("type");
                    if (!TextUtils.isEmpty(type) && "1".equals(type)) {//判断是一对一通话邀请
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("LIVE_INFO", invitedEvent);
                        gotoActivity(AnswerPhoneAct.class, false, bundle);
                    }
                    break;
                case CANCEL_INVITE:
                    CanceledInviteEvent canceledInviteEvent = (CanceledInviteEvent) channelCommonEvent;
                    break;
                case REJECT:
                case ACCEPT:
                    InviteAckEvent ackEvent = (InviteAckEvent) channelCommonEvent;
                    String inviCustom = ackEvent.getCustomInfo();
                    CallEvent callEvent = new CallEvent(ackEvent.getAckStatus().getValue());
                    if (!TextUtils.isEmpty(inviCustom) && inviCustom.contains("跳过")) {
                        callEvent.setJump(true);
                    }
                    EventBus.getDefault().post(callEvent);
                    break;
                case LEAVE:
                    UserLeaveEvent userLeaveEvent = (UserLeaveEvent) channelCommonEvent;
                    break;
                case CONTROL:
                    ControlEvent controlEvent = (ControlEvent) channelCommonEvent;
                    break;
            }
        }
    };
    public GiftEntity.ListBean giftBean;
    private boolean isSeenotic;

    /**
     * 处理uiKit传过来的操作
     *
     * @param goSettingEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doTradeListEvent(GoSettingEvent goSettingEvent) {
        if (goSettingEvent == null) {
            return;
        }
        if (goSettingEvent.isGoAnchorInfo()) {
            presenter.checkISAnchor(goSettingEvent.getAccid());
            return;
        }
        if (goSettingEvent.getAblitityNumber() != 0) {//聊天页发送评价
            presenter.sendScoreForAnchor(goSettingEvent.getAccid(), goSettingEvent.getAblitityNumber(), goSettingEvent.getServiceNumber());
            return;
        }
        if (goSettingEvent.isCanScore()) {
            presenter.getMessageCanScore(goSettingEvent.getAccid());
            isSeenotic = goSettingEvent.isSeeNotic();
            return;
        }
        if (goSettingEvent.isScore()) {//获取是否能评价
            presenter.sendMessageForGift(goSettingEvent.getAccid());

            return;
        }
        if (goSettingEvent.isGoRecharge()) {//跳转到充值页面
            gotoActivity(RechargeAct.class);
            return;
        }
        if (goSettingEvent.isGetGiftForMe()) {//获取收到的礼物列表
            presenter.getGiftgiveList(goSettingEvent.getAccid());
            return;
        }
        if (goSettingEvent.isSendGift()) {//私信发送礼物
            giftBean = goSettingEvent.listBean;
            presenter.sendGift(goSettingEvent.getListBean().getId(), goSettingEvent.getAccid());
            return;
        }
        if (goSettingEvent.isGift()) {//获取礼物列表
            presenter.getGiftInfo();
            return;
        }

        if (!TextUtils.isEmpty(goSettingEvent.getLink())) {
            presenter.subMitReCallLive(goSettingEvent.getLink());
            return;
        }
//        if (goSettingEvent.isGoRecharge) {
//            gotoActivity(MessageSettingAct.class);
//            return;
//        }
        Bundle bundle = new Bundle();
        bundle.putString("accID", goSettingEvent.getAccid());
        gotoActivity(MessageSettingAct.class, false, bundle);

    }


    /**
     * 处理uiKit传过来的操作
     *
     * @param goSettingEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasSeeNumberNews(SettingSeeView goSettingEvent) {
        if (goSettingEvent != null) {
            systemNews = goSettingEvent.getHasNewsNumber();
            setNosee();
        }
    }

    private void registerObserve(boolean b) {
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(nimOnlineObserver, b);
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, b);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(onlineStatus, b);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObserve(false);
        EventBus.getDefault().unregister(this);
    }

//    /**
//     * 获取 rtc AppKey
//     */
//    private String getRtcAppKey() {
//        ApplicationInfo appInfo = null;
//        try {
//            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (appInfo == null) {
//            return null;
//        }
//        return appInfo.metaData.getString("com.netease.nim.appKey");
//    }
//
//    /**
//     * 已经登陆过，自动登陆
//     */
//    private boolean canAutoLogin() {
//        LoginInfo loginInfo = Preferences.getLoginInfo();
//        return loginInfo.valid();
//    }

    private void loginUiKit() {
        if (TextUtils.isEmpty(Preferences.getLoginInfo().getAccount())) {
            return;
        }
        StatusCode status = NIMClient.getStatus();
        if (NIMClient.getStatus() != StatusCode.LOGINED) {
            LoginInfo loginInfo = new LoginInfo(Preferences.getLoginInfo().getAccount(), Preferences.getLoginInfo().getToken());
            // 登录
            RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
                    if (!TextUtils.isEmpty(param.getAccount())) {
                        DemoCache.setAccount(param.getAccount());
                        saveLoginInfo(loginInfo);
                    }
                }

                @Override
                public void onFailed(int code) {
                }

                @Override
                public void onException(Throwable exception) {
                }
            };
            NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
        }
    }

    private void initEvent() {
        fragments = new ArrayList<>();
        fragments.add(0, new HomeFrag());
        fragments.add(1, new SmallVideoFrag());
        fragments.add(2, new MessageFrag());
        fragments.add(3, new MyselfFrag());
        rgMainTab.check(resId[position]);
        switchFragment(0);
    }

    private void switchFragment(int pos) {
        position = pos;
        rgMainTab.check(resId[position]);
        ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragments.get(pos).getClass().getName());
        if (fragment == null) {
            fragment = fragments.get(pos);
        }
        currentFragment = fragment;
        if (!fragment.isAdded()) {
            ft.add(R.id.fragment_container, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void doBusiness() {
        presenter.getTeenager();
        if (!ToolUtils.isLogin(MainActivity.this, false)) {
            presenter.getProtocoList("3");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    UserInfoEntity userInfoEntity;
    NoticeDialog noticeDialog;
    TeenagerNoSeeDialog teenagerNoSeeDialog;

    @Override
    public void setUserInfoSuccess(UserInfoEntity data) {
        userInfoEntity = data;
        if (!PackageUtils.getVersionName(MainActivity.this).equalsIgnoreCase(data.getAndroid_version()) || data.getNew_report() == 1) {
            customerNewTv.setVisibility(View.VISIBLE);
        } else {
            customerNewTv.setVisibility(View.GONE);
        }

//        if (data.getIs_read_conceal() == 0) {//没有确认隐私协议

        presenter.getProtocoList("3");
//            return;
//        }
        showNotice(userInfoEntity.getNotice());


        //是否开启青少年模式
        if (data.getIs_teenager() == 1) {
            rbMainTabWealth.setVisibility(View.GONE);
            rbMainTabMessage.setVisibility(View.GONE);
            messageTestLL.setVisibility(View.GONE);
//            boolean b = ToolUtils.isCurrentInTimeScope(22, 0, 6, 0);
//            if (b) {
//                if (teenarNoSeeDialog != null && teenarNoSeeDialog.getDialog() != null && teenarNoSeeDialog.getDialog().isShowing()) {
//
//                } else {
//                    teenarNoSeeDialog = new TeenagerNoSeeDialog();
//                    teenarNoSeeDialog.show(getSupportFragmentManager(), "TeenarNoSeeDialog");
//                    teenarNoSeeDialog.setOnConfirmListener(new TeenagerNoSeeDialog.OnBaseTipsListener() {
//                        @Override
//                        public void comfirm() {
//                            gotoActivity(YouthModeAct.class);
//                        }
//
//                        @Override
//                        public void finishAll() {
//                            finish();
//                            System.exit(0);
//                        }
//                    });
//
//                }
//            }
        } else {
            messageTestLL.setVisibility(View.VISIBLE);
            rbMainTabWealth.setVisibility(View.VISIBLE);
            rbMainTabMessage.setVisibility(View.VISIBLE);
        }
    }

    PrivacyDialog privacyDialog;

    /**
     * 隐私协议弹框
     *
     * @param data
     */
    @Override
    public void setProtocolDetail(ProtocolDetailEntity data) {
        if (privacyDialog != null && privacyDialog.getDialog() != null
                && privacyDialog.getDialog().isShowing()) {
            return;
        }
        if ("1".equalsIgnoreCase(ACache.get(MainActivity.this).getAsString(ACEConstant.YIN_XIEY))) {
            return;
        }
        ACache.get(MainActivity.this).put(ACEConstant.YIN_XIEY, "1");
        privacyDialog = new PrivacyDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PRIVACY_DATA", data.getList());
        privacyDialog.setArguments(bundle);
        privacyDialog.setCancelable(true);
        privacyDialog.show(getSupportFragmentManager(), "PrivacyDialog");
        privacyDialog.setOnConfirmListener(new PrivacyDialog.OnFreezeTipsListener() {
            @Override
            public void gotoAppeal(int type) {
                presenter.RealConceal();
            }
        });

    }

    @Override
    public void setSubRealConceal() {
        showNotice(userInfoEntity.getNotice());
    }

    @Override
    public void setNoticeSuccess() {
        if (userInfoEntity != null && userInfoEntity.getNotice().size() != 0) {
            userInfoEntity.getNotice().remove(0);
            showNotice(userInfoEntity.getNotice());
        }
    }

    @Override
    public void setGiftInfo(GiftEntity data) {
        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setGiftEntity(data);
        EventBus.getDefault().post(giftEvent);
    }

    @Override
    public void sendGiftSuccess() {
        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setGiftEntity(null);
        giftEvent.setGiftBean(giftBean);
        giftEvent.setSendGift(true);
        EventBus.getDefault().post(giftEvent);
    }

    @Override
    public void getForMeGiftListSuccess(GiftForMeEntity data) {
        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setGiftForMeEntity(data);
        EventBus.getDefault().post(giftEvent);
    }

    @Override
    public void reOrderSuccess(String msg) {
        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setReOrder(msg);
        EventBus.getDefault().post(giftEvent);
    }

    /**
     * 获取是否能评价主播
     *
     * @param data
     */
    @Override
    public void setMessageCanScore(MessageCanScoreEntity data) {
        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setMessageCanScoreEntity(data);
        giftEvent.setSeeNotic(isSeenotic);
        EventBus.getDefault().post(giftEvent);
    }

    @Override
    public void setGotoRechargeAct() {

        GiftEvent giftEvent = new GiftEvent();
        giftEvent.setGiftEntity(null);
        giftEvent.setGiftBean(giftBean);
        giftEvent.setSendGift(true);
        giftEvent.setGOtoRecharge(true);
        EventBus.getDefault().post(giftEvent);


    }

    @Override
    public void setYouthSuccess(YouthEntity data) {
        BKGSApplication.youthModeStatus = data.getMember().getIs_teenager();
        if (data.getMember().getIs_teenager() == 1 && data.getMember().getIs_ban() == 1) {
            // 青少年模式禁止登陆
            teenagerNoSeeDialog = new TeenagerNoSeeDialog();
            teenagerNoSeeDialog.show(getSupportFragmentManager(), "TeenagerNoSeeDialog");
            teenagerNoSeeDialog.setOnConfirmListener(new TeenagerNoSeeDialog.OnBaseTipsListener() {
                @Override
                public void comfirm() {
                    gotoActivity(YouthModeAct.class);
                }

                @Override
                public void finishAll() {
                    finish();
                    System.exit(0);
                }
            });
            return;
        }

        if (AcacheUtil.isShowTeenager(MainActivity.this)) {
            return;
        }

        AcacheUtil.saveTeenager(MainActivity.this);
        TeenagerDialog dialog = new TeenagerDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TEENAGER_INFO", data);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "TeenagerDialog");
    }

    /**
     * "is_anchor": 1,是否主播 <number>
     * "system_notice_count": 2,系统消息数量 <number>
     * "anchor_appoint_count": 1,主播预约数量 <number>
     * "appoint_count": 0我的预约数量 <number>
     *
     * @param data
     */
    int systemNews;

    @Override
    public void setMessageSuccess(MessageListEntity data) {
        systemNews = data.getSystem_notice_count() + data.getAnchor_appoint_count() + data.getAppoint_count();
        setNosee();
    }

    @Override
    public void setGoAnchorInfo(String anchorId) {
        Bundle bundle = new Bundle();
        bundle.putString("AnchorID", anchorId);
        gotoActivity(AnchorInformationAct.class, false, bundle);
    }

    private void showNotice(List<UserInfoEntity.NoticeBean> notice) {
        if (notice == null || notice.size() == 0) {
            return;
        }
        if (notice.size() != 0) {
            noticeDialog = new NoticeDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable("NOTIC_ENTITY", notice.get(0));
            noticeDialog.setArguments(bundle);
            noticeDialog.setCancelable(true);
            noticeDialog.show(getSupportFragmentManager(), "NoticeDialog");
            noticeDialog.setOnConfirmListener(new NoticeDialog.OnNoticeListener() {
                @Override
                public void confirm(int type, boolean isFinish) {
                    if (isFinish) {
                        presenter.agreeNotice(type);
                    } else {
                        if (userInfoEntity != null && userInfoEntity.getNotice().size() != 0) {
                            userInfoEntity.getNotice().remove(0);
                            showNotice(userInfoEntity.getNotice());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}