package com.pro.maluli.module.home.oneToOne.queue;

import static com.netease.nim.uikit.common.media.imagepicker.camera.CaptureActivity.VIDEO_PERMISSIONS;
import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.lava.nertc.sdk.NERtc;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;
import com.netease.nim.uikit.common.media.imagepicker.camera.ConfirmationDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.constant.AppIdConstants;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.OneToOneLiveEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.networkRequest.Url;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.EditGongGaoDialog;
import com.pro.maluli.common.view.dialogview.EditPersonDialog;
import com.pro.maluli.common.view.dialogview.SubReserveDialog;
import com.pro.maluli.common.view.dialogview.checkMsg.CheckMsgDialog;
import com.pro.maluli.common.view.myselfView.QFolderTextView;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.home.oneToOne.queue.adapter.QueueAdapter;
import com.pro.maluli.module.home.oneToOne.queue.adapter.QueueTopVideoFrg;
import com.pro.maluli.module.home.oneToOne.queue.presenter.IOneToOneQueueContraction;
import com.pro.maluli.module.home.oneToOne.queue.presenter.OneToOneQueuePresenter;
import com.pro.maluli.module.home.startLive.StartLiveAct;
import com.pro.maluli.module.socketService.SocketUtils;
import com.pro.maluli.module.socketService.event.OnTwoOneEvent;
import com.pro.maluli.toolkit.ToastExtKt;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class OneToOneQueueAct extends BaseMvpActivity<IOneToOneQueueContraction.View, OneToOneQueuePresenter> implements IOneToOneQueueContraction.View {
    public static final int REQUEST_VIDEO_PERMISSIONS = 1;
    public static final String PERMISSIONS_FRAGMENT_DIALOG = "permission_dialog";
    private static final int UPDATE_VIEWPAGER = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @BindView(R.id.pointLL)
    LinearLayout pointLL;
    @BindView(R.id.anchorAvaterCiv)
    CircleImageView anchorAvaterCiv;
    @BindView(R.id.liveingIv)
    ImageView liveingIv;
    @BindView(R.id.LiveingLL)
    LinearLayout LiveingLL;
    @BindView(R.id.anchorNameTv)
    TextView anchorNameTv;
    @BindView(R.id.liveIdTv)
    TextView liveIdTv;
    @BindView(R.id.leveImg)
    ImageView leveImg;
    @BindView(R.id.ReserveTv)
    TextView ReserveTv;
    @BindView(R.id.ReserveNumberTv)
    TextView ReserveNumberTv;
    @BindView(R.id.editReserveNumberTv)
    TextView editReserveNumberTv;
    @BindView(R.id.editGGTv)
    TextView editGGTv;
    @BindView(R.id.spanna)
    QFolderTextView spanna;
    @BindView(R.id.reserveRlv)
    RecyclerView reserveRlv;
    @BindView(R.id.canReserveTv)
    TextView canReserveTv;
    @BindView(R.id.startLiveTv)
    TextView startLiveTv;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.liveTimeStatusTv)
    TextView liveTimeStatusTv;
    @BindView(R.id.anchorEidtLl)
    LinearLayout anchorEidtLl;
    @BindView(R.id.nodataView)
    LinearLayout nodataView;
    @BindView(R.id.viewPager)
    ViewPager bannerViewPager;
    boolean isStartVideo = true;
    CountDownTimer countDownTimer;
    List<Fragment> topVideoFragment;
    TopVideoAdapter topVideoAdapter;
    private int autoCurrIndex = 0;
    private Timer timer;
    private TimerTask timerTask;
    private ImageView[] tips;
    private long period = 5000;
    private String anchor_id = "";
    private ReserveEntity reserveEntity;
    private QueueAdapter queueAdapter;
    private List<ReserveEntity.InfoBean.AppointListBean> appointListBeans = new ArrayList<>();
    private boolean isLike;//是否关注
    private String reserveNumber;
    private boolean isFristLive;//判断是否是第一次创建直播间
    private boolean mDataServiceBind;
    private String liveBg;
    private String liveTitle;
    private boolean isFirstStart = true;
    private boolean isStartLive;
    private boolean isGranted = true;
    private BaseTipsDialog reserveTipsDialog = null;

    //    /**
//     * socket
//     *
//     * @return
//     */
//    public DataService mDataService;
//    public String socketUrl = "";
//    private ServiceConnection coreServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mDataService = null;
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mDataService = ((DataService.DataServiceBinder) service).getService();
//            String lasdak = AcacheUtil.getToken(OneToOneQueueAct.this, false).substring(7);
//            String url = Url.SOCKET_URL + "?anchor_id=2" + "&token=" + lasdak;
//            mDataService.initSocket(url);
//        }
//    };
    @Override
    public OneToOneQueuePresenter initPresenter() {
        return new OneToOneQueuePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        isFristLive = getIntent().getBooleanExtra("IS_FRIST_LIVE", false);
        isStartLive = getIntent().getBooleanExtra("isStartLive", false);
        liveBg = getIntent().getStringExtra("LIVE_BG");
        liveTitle = getIntent().getStringExtra("LIVE_TITLE");
        presenter.anchor_id = getIntent().getStringExtra("ANCHOR_ID");

        Logger.d("title: " + liveTitle);
        Logger.d("bg: " + liveBg);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(DataService.getIntent(this));
//        } else {
//            startService(DataService.getIntent(this));//启动数据服务
//        }
//        mDataServiceBind = bindService(DataService.getIntent(), coreServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_one_to_one_queue;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lasdak = AcacheUtil.getToken(OneToOneQueueAct.this, true).substring(7);
        String url = Url.SOCKET_URL + "?anchor_id=" + presenter.anchor_id + "&token=" + lasdak;
        Logger.e(url);
        SocketUtils.INSTANCE.onStartCommand(url);
        startTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            mHandler.removeCallbacksAndMessages(null);
            GSYVideoManager.releaseAllVideos();
        }
        SocketUtils.INSTANCE.closeConnect();
    }

    private void startTime() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Message message = new Message();
                message.what = UPDATE_VIEWPAGER;
                if (reserveEntity != null && reserveEntity.getBanner() != null && autoCurrIndex == reserveEntity.getBanner().size() - 1) {
                    autoCurrIndex = -1;
                }
                if (!isStartVideo) {
                    return;
                }
                message.arg1 = autoCurrIndex + 1;
                mHandler.sendMessage(message);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void viewInitialization() {
        setTitleTx("一对一排队");
        setBackPress();
        topVideoFragment = new ArrayList<>();
        reserveRlv.setLayoutManager(new GridLayoutManager(this, 4));
        queueAdapter = new QueueAdapter(appointListBeans, this);
        reserveRlv.setAdapter(queueAdapter);
        queueAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                if (reserveEntity.getInfo().getIs_self() == 0) {
                    return;
                }
                ReserveEntity.InfoBean.AppointListBean appointListBean = appointListBeans.get(position);
                SeeLiveUserEntity.AppointBean appointBean = new SeeLiveUserEntity.AppointBean();
                appointBean.setImages(appointListBean.getImages());
//                appointBean.setAccid(appointListBean.getAccid());
                appointBean.setAvatar(appointListBean.getAvatar());
                appointBean.setContent(appointListBean.getContent());
                appointBean.setNickname(appointListBean.getNickname());
                appointBean.setCreated_at(appointListBean.getCreated_at());
                appointBean.setStatus(appointListBean.getStatus());

                CheckMsgDialog checkMsgDialog = new CheckMsgDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Message", appointBean);
                checkMsgDialog.setArguments(bundle);
                checkMsgDialog.show(getSupportFragmentManager(), "CheckMsgDialog");
            }
        });
        EventBus.getDefault().register(this);
        if (isStartLive) {
            startLiveTv.setVisibility(View.INVISIBLE);
        }
        requestPermissionsIfNeeded();
    }

    private void requestPermissionsIfNeeded() {

        final List<String> missedPermissions = NERtc.checkPermission(this);
        if (missedPermissions.size() > 0) {
            isGranted = false;
            ActivityCompat.requestPermissions(this, missedPermissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        } else {
            isGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {//如果申请权限回调的参数
            boolean hasGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(OneToOneQueueAct.this, permissions[i]);
                    if (showRequestPermission) {
                        showToast("权限未申请,请开启权限");
                    }
                    hasGranted = false;
                }
            }
            isGranted = hasGranted;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doTradeListEvent(OnTwoOneEvent tradeListEvent) {
        Logger.e("一对一排队 doTradeListEvent");
        if (tradeListEvent != null) {
            setQueueSuccess(tradeListEvent.getEntity());
        }
    }

    private void autoBanner() {
        topVideoFragment.clear();
        for (int i = 0; i < reserveEntity.getBanner().size(); i++) {
            QueueTopVideoFrg fragment = (QueueTopVideoFrg) QueueTopVideoFrg.newInstance(reserveEntity.getBanner().get(i));
            topVideoFragment.add(fragment);
            fragment.setBannerListener(new QueueTopVideoFrg.BannerListener() {
                @Override
                public void imgClick(int position) {
                }

                @Override
                public void videoIsStart() {
                    isStartVideo = false;
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        mHandler.removeCallbacksAndMessages(null);
                    }
                }

                @Override
                public void videoStop() {
                    isStartVideo = true;
                    startTime();
                }
            });
        }
//        //new一个适配器
        topVideoAdapter = new TopVideoAdapter(getSupportFragmentManager());
        //设置ViewPager与适配器关联
        bannerViewPager.setAdapter(topVideoAdapter);
        bannerViewPager.setCurrentItem(0);   //
//        bannerViewPager.setOffscreenPageLimit(0);
//        bannerViewAdapter = new QueueBannerAdapter(this, reserveEntity.getBanner());
//        bannerViewPager.setAdapter(bannerViewAdapter);
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ReserveEntity.BannerBean banner = reserveEntity.getBanner().get(position);
                setImageBackground(position);
//                JCVideoPlayer.releaseAllVideos();
                autoCurrIndex = position;
//                GSYVideoManager.releaseAllVideos();
                try {
                    GSYVideoManager.instance().getPlayer().release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (banner.getFile_type() != 1) {
                    isStartVideo = false;
                }
                startTime();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        bannerViewAdapter.setBannerListener(new QueueBannerAdapter.BannerListener() {
//            @Override
//            public void imgClick(int position) {
//                BrowserExtKt.openBrowser(OneToOneQueueAct.this, reserveEntity.getBanner().get(position).getLink());
//            }
//
//            @Override
//            public void videoIsStart() {
//                isStartVideo = true;
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                    mHandler.removeCallbacksAndMessages(null);
//                }
//            }
//
//            @Override
//            public void videoStop() {
//                isStartVideo = false;
//                startTime();
//            }
//        });
    }

    /**
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.shape_yuan_000000);
            } else {
                tips[i].setBackgroundResource(R.drawable.shape_yuan_000000_2);
            }
        }
    }

    @Override
    public void doBusiness() {
        presenter.getReserveInfo();
    }    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_VIEWPAGER) {
                if (msg.arg1 != 0) {
                    bannerViewPager.setCurrentItem(msg.arg1);
                } else {
                    bannerViewPager.setCurrentItem(msg.arg1, false);
                }
                startTime();
            }
        }
    };

    @OnClick({R.id.editGGTv, R.id.canReserveTv, R.id.startLiveTv, R.id.editReserveNumberTv, R.id.attentionTv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.editGGTv://编辑公告
                EditGongGaoDialog editGongGaoDialog = new EditGongGaoDialog();
                editGongGaoDialog.setCancelable(true);
                Bundle bundleEditGongGao = new Bundle();
                bundleEditGongGao.putString("DESC", reserveEntity.getInfo().getAppoint_desc());
                editGongGaoDialog.setArguments(bundleEditGongGao);
                editGongGaoDialog.show(getSupportFragmentManager(), "EditGongGaoDialog");
                editGongGaoDialog.setOnConfirmListener(new EditGongGaoDialog.OnEditPersonListener() {
                    @Override
                    public void subNumber(String desc) {
                        presenter.changeLiveInfo("", desc);
                    }
                });
                break;
            case R.id.canReserveTv://接受预约
                presenter.canReserve();
                break;
            case R.id.startLiveTv://开播
                if (reserveEntity.getInfo().getLive().getType() == 2) {
                    ToastUtils.showShort("对众直播间未关闭，不能开始直播");
                    return;
                }
                if (TextUtils.isEmpty(liveTitle)) {
                    liveTitle = reserveEntity.getInfo().getLive().getTitle();
                }
                if (TextUtils.isEmpty(liveBg)) {
                    liveBg = reserveEntity.getInfo().getLive().getImage();
                }
                if (reserveEntity.getInfo().getLive().getType() == 1) {
                    if (reserveEntity != null && reserveEntity.getInfo().getReport_num() > 0) {
                        presenter.getLiveInfo();
                    } else {
                        ToastUtils.showShort("请先设置预约人数！");
                    }
                } else {
                    presenter.startLive("1", liveTitle, liveBg);
                }
                break;
            case R.id.attentionTv://关注
                presenter.anchorSub();
                break;
            case R.id.editReserveNumberTv://编辑人数
                if (editReserveNumberTv.isSelected()) {
                    if (reserveEntity.getInfo().getIs_appoint() == 1) {//没有预约
                        BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("showContent", "取消当前预约");
                        bundle2.putString("TITLE_DIO", "温馨提示");
                        bundle2.putString("comfirm", "确认");
                        bundle2.putString("cancel", "取消");
                        baseTipsDialog1.setArguments(bundle2);
                        baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                        baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                            @Override
                            public void comfirm() {
                                presenter.cancelReserve();
                            }
                        });
                        return;
                    }
                    if (reserveEntity.getInfo().getIs_report() != 1) {
                        ToastUtils.showShort("主播已暂停预约或预约人数已满，请稍后再预约");
                        return;
                    }
                    SubReserveDialog reserveDialog = new SubReserveDialog();
                    reserveDialog.setCancelable(true);
                    reserveDialog.show(getSupportFragmentManager(), "EditPersonDialog");
                    reserveDialog.setOnConfirmListener(new SubReserveDialog.OnEditPersonListener() {
                        @Override
                        public void subNumber(List<File> files, String content) {
                            if (files.size() == 0) {
                                presenter.subReserve(content, "");
                            } else {
                                presenter.subImg(files, content);
                            }
                        }
                    });
                    return;
                }
                EditPersonDialog dialog = new EditPersonDialog();
                Bundle bundle = new Bundle();
                bundle.putString("Reserve_number", String.valueOf(reserveEntity.getInfo().getAppoint_num()));
                dialog.setArguments(bundle);
                dialog.setCancelable(true);
                dialog.show(getSupportFragmentManager(), "EditPersonDialog");
                dialog.setOnConfirmListener(new EditPersonDialog.OnEditPersonListener() {
                    @Override
                    public void subNumber(String type) {
                        reserveNumber = type;
                        presenter.changeLiveInfo(reserveNumber, "");
                    }
                });
                break;
        }
    }

    @Override
    public void setQueueSuccess(ReserveEntity data) {
        this.reserveEntity = data;
        if (data.getInfo().getIs_live() == 1) {
            LiveingLL.setVisibility(View.VISIBLE);
            GlideUtils.loadGif(this, R.mipmap.ic_liveing, liveingIv);
        } else {
            LiveingLL.setVisibility(View.GONE);
        }
        GlideUtils.loadImage(this, data.getInfo().getAvatar(), anchorAvaterCiv);
        GlideUtils.loadImage(this, data.getInfo().getLevel(), leveImg);
        anchorNameTv.setText(data.getInfo().getNickname());
        liveIdTv.setText("ID:" + data.getInfo().getAnchor_no());
        liveTimeStatusTv.setText(data.getInfo().getProgress_text());
        ReserveNumberTv.setText(String.format("%1$s/%2$s", data.getInfo().getAppoint_num() + "", data.getInfo().getReport_num() + ""));
        // 是自己的话不能编辑
        spanna.setText(TextUtils.isEmpty(data.getInfo().getAppoint_desc()) ? "主播暂未填写公告" : data.getInfo().getAppoint_desc(), null);
        spanna.setForbidFold(false);
        spanna.setFoldLine(2);
        spanna.setEllipsize("..");
        spanna.setUnfoldText(" 点击展开");
        spanna.setFoldColor(Color.parseColor("#3D85FF"));
        if (data.getInfo().getIs_self() == 1) {
            editGGTv.setVisibility(View.VISIBLE);

            anchorEidtLl.setVisibility(View.VISIBLE);
            if (data.getInfo().getIs_report() == 1) {
                canReserveTv.setText("暂停预约");
            } else {
                canReserveTv.setText("接受预约");

            }
            editReserveNumberTv.setText("编辑人数");
            editReserveNumberTv.setSelected(false);
            attentionTv.setVisibility(View.GONE);
        } else {
            editGGTv.setVisibility(View.GONE);

            editReserveNumberTv.setSelected(true);
            anchorEidtLl.setVisibility(View.GONE);
            attentionTv.setVisibility(View.VISIBLE);
            if (data.getInfo().getIs_sub() == 1) {
                isLike = true;
                attentionTv.setSelected(false);
                attentionTv.setText("取消关注");
            } else {
                isLike = false;
                attentionTv.setSelected(true);
                attentionTv.setText("+关注");
            }
            if (data.getInfo().getIs_appoint() == 1) {
                editReserveNumberTv.setText("取消预约");
            } else {
                if (data.getInfo().getIs_report() == 1 && data.getInfo().getAppoint_num() != data.getInfo().getReport_num()) {
                    editReserveNumberTv.setText("预约主播");
                } else {
                    editReserveNumberTv.setText("暂停预约");
                    editReserveNumberTv.setAlpha(0.5f);
                }

            }
        }
        if (isFirstStart) {
            setDianDian(reserveEntity.getBanner().size());
            autoBanner();
        }

        Logger.e("appoint list size: " + data.getInfo().getAppoint_list().size());
        if (data.getInfo().getAppoint_list().size() == 0) {
            nodataView.setVisibility(View.VISIBLE);
            reserveRlv.setVisibility(View.GONE);
        } else {
            nodataView.setVisibility(View.GONE);
            reserveRlv.setVisibility(View.VISIBLE);
            appointListBeans.clear();
            appointListBeans.addAll(data.getInfo().getAppoint_list());
            queueAdapter.notifyDataSetChanged();
        }
        isFirstStart = false;
    }

    private void setDianDian(int a) {
        tips = new ImageView[a];
        pointLL.removeAllViews();
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.shape_yuan_000000);
            } else {
                imageView.setBackgroundResource(R.drawable.shape_yuan_000000_2);
            }
            tips[i] = imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15;
            layoutParams.bottomMargin = 40;
            pointLL.addView(imageView, layoutParams);
        }
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
    public void setStartInfo(OneToOneEntity data) {
        if (reserveEntity != null && reserveEntity.getInfo().getReport_num() > 0) {
            presenter.getLiveInfo();
        } else {
            ToastUtils.showShort("请先设置预约人数！");
        }
    }

    @Override
    public void setDqLiveInfo(OneToOneLiveEntity data) {
        if (NIMClient.getStatus() != StatusCode.LOGINED) {
            //未登录
            LoginInfo loginInfo = new LoginInfo(Preferences.getLoginInfo().getAccount(), Preferences.getLoginInfo().getToken(), AppIdConstants.WY_APP_KEY);
            RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
                    if (!TextUtils.isEmpty(param.getAccount())) {
                        DemoCache.setAccount(Preferences.getLoginInfo().getAccount());
                        saveLoginInfo(loginInfo);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("liveId", String.valueOf(data.getRoom_id()));
                    gotoActivity(StartLiveAct.class, true, bundle);
                }

                @Override
                public void onFailed(int code) {
                    ToastUtils.showShort("进入失败");
                }

                @Override
                public void onException(Throwable exception) {
                    exception.printStackTrace();
                }
            };
            NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
        } else if (NIMClient.getStatus() == StatusCode.LOGINED) {
            Bundle bundle = new Bundle();
            bundle.putString("liveId", String.valueOf(data.getRoom_id()));
            gotoActivity(StartLiveAct.class, true, bundle);
        }
    }

    private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            new ConfirmationDialog().show(getFragmentManager(), PERMISSIONS_FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setReserveSuccess() {
//        if (response.getCode())
        try {

            reserveTipsDialog = new BaseTipsDialog();
            Bundle bundle = new Bundle();
            bundle.putString("showContent", "预约成功，请打开手机声音、视频、通话、通知等相关权限，并尽量保留在本APP页面，避免错过主播来电邀请");
            bundle.putString("comfirm", "知道了");
            bundle.putBoolean("CANCEL_SEE", true);
            reserveTipsDialog.setArguments(bundle);
            reserveTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
            reserveTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                @Override
                public void comfirm() {
                    if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
                        requestVideoPermissions();
                        if (reserveTipsDialog != null) {
                            reserveTipsDialog.dismiss();
                            reserveTipsDialog = null;
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReserveFailed(BaseResponse<Object> response) {
        ToastExtKt.showToast(this, response.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SocketUtils.INSTANCE.closeConnect();
    }

    private class TopVideoAdapter extends FragmentPagerAdapter {
        public TopVideoAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return topVideoFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return reserveEntity.getBanner().get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return topVideoFragment.get(position);
        }
    }




}