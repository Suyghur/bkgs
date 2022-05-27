package com.pro.maluli.module.home.previewLive;

import static com.netease.nim.uikit.common.media.imagepicker.camera.CaptureActivity.PERMISSIONS_FRAGMENT_DIALOG;
import static com.netease.nim.uikit.common.media.imagepicker.camera.CaptureActivity.VIDEO_PERMISSIONS;
import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;
import com.netease.nim.uikit.common.media.imagepicker.camera.ConfirmationDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.AppIdConstants;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.LivePayInfoEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.PayInLiveDialog;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.common.view.myselfView.StarBar;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.StartOneToMoreLiveAct;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.home.previewLive.adapter.PreviewLiveAdapter;
import com.pro.maluli.module.home.previewLive.presenter.IPreviewLiveContraction;
import com.pro.maluli.module.home.previewLive.presenter.PreviewLivePresenter;
import com.pro.maluli.module.myself.anchorInformation.base.AnchorInformationAct;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;
import com.pro.maluli.module.video.videoact.VideoAct;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class PreviewLiveAct extends BaseMvpActivity<IPreviewLiveContraction.View, PreviewLivePresenter> implements IPreviewLiveContraction.View {

    public static final int REQUEST_VIDEO_PERMISSIONS = 1;
    PreviewLiveAdapter previewLiveAdapter;
    @BindView(R.id.anchorAvaterCiv)
    CircleImageView anchorAvaterCiv;
    @BindView(R.id.liveingIv)
    ImageView liveingIv;
    @BindView(R.id.liveBgIv)
    ImageView liveBgIv;
    @BindView(R.id.LiveingLL)
    LinearLayout LiveingLL;
    @BindView(R.id.anchorNameTv)
    TextView anchorNameTv;
    @BindView(R.id.liveIdTv)
    TextView liveIdTv;
    @BindView(R.id.leveImg)
    ImageView leveImg;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.abilityStar)
    StarBar abilityStar;
    @BindView(R.id.serviceQualitySb)
    StarBar serviceQualitySb;
    @BindView(R.id.topInfoLL)
    LinearLayout topInfoLL;
    @BindView(R.id.serviceTv)
    TextView serviceTv;
    @BindView(R.id.ReserveTv)
    TextView ReserveTv;
    @BindView(R.id.anchorImgRv)
    RecyclerView anchorImgRv;
    @BindView(R.id.checkImgIV)
    ImageView checkImgIV;
    @BindView(R.id.chekcVideoIv)
    ImageView chekcVideoIv;
    @BindView(R.id.StartLiveTv)
    TextView StartLiveTv;
    @BindView(R.id.closeIv)
    ImageView closeIv;
    @BindView(R.id.bottomLL)
    LinearLayout bottomLL;
    //    List<List<AnchorInfoEntity.PictureBean>> pictureBeanList = new ArrayList<>();
    List<AnchorInfoEntity.PictureBean> pictureBeanList = new ArrayList<>();
    private String anchorId = "";
    private AnchorInfoEntity anchorInfoEntity;
    private boolean isSHowImg = true;
    private String roomId;
    private boolean isSubAnchor;

    @Override
    public PreviewLivePresenter initPresenter() {
        return new PreviewLivePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.transparentStatusBar(this);
        anchorId = getIntent().getStringExtra("ANCHOR_ID");
        roomId = getIntent().getStringExtra("ROOM_ID");
        presenter.anchorId = anchorId;

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_preview_live;
    }

    @Override
    public void viewInitialization() {

        anchorImgRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        ToolUtils.checkPublishPermission(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
//            return;
        }
        presenter.getAnchorInfo(anchorId);
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

    private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            new ConfirmationDialog().show(getFragmentManager(), PERMISSIONS_FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    @OnClick({R.id.attentionTv, R.id.checkImgIV, R.id.chekcVideoIv,
            R.id.ReserveTv, R.id.StartLiveTv, R.id.closeIv,
            R.id.AnchorInfoRl})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.attentionTv://关注
                if (!ToolUtils.isLoginTips(PreviewLiveAct.this, getSupportFragmentManager())) {
                    return;
                }
                presenter.anchorSub();
                break;
            case R.id.checkImgIV://是否展示图片
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
            case R.id.chekcVideoIv:

                if (anchorInfoEntity != null && anchorInfoEntity.getVideo().size() > 0) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("AnchorID", anchorId);
//                    bundle.putBoolean("isFormVideo", true);
//                    gotoActivity(AnchorInformationAct.class, false, bundle);
                    Bundle bundle = new Bundle();
                    bundle.putString("AnchorID", presenter.anchorId);
                    bundle.putInt("Position", 0);
                    gotoActivity(VideoAct.class, false, bundle);
                } else {
                    ToastUtils.showShort("没有视频展示哦");
                }
                break;
            case R.id.AnchorInfoRl:
                Bundle bundle = new Bundle();
                bundle.putString("AnchorID", anchorId);
                gotoActivity(AnchorInformationAct.class, false, bundle);
                break;
            case R.id.StartLiveTv:
                if (!ToolUtils.isLoginTips(PreviewLiveAct.this, getSupportFragmentManager())) {
                    return;
                }

                if (anchorInfoEntity.getLive().getType() == 1 || anchorInfoEntity.getIs_edit() != 1 && anchorInfoEntity.getIs_live() == 0) {
                    Bundle bundleReserve = new Bundle();
                    bundleReserve.putString("ANCHOR_ID", anchorId);
                    gotoActivity(OneToOneQueueAct.class, false, bundleReserve);
                    return;
                }
                presenter.joinLive(roomId);
                break;
            case R.id.ReserveTv:
                if (!ToolUtils.isLoginTips(PreviewLiveAct.this, getSupportFragmentManager())) {
                    return;
                }
                Bundle bundleReserve = new Bundle();
                bundleReserve.putString("LIVE_BG", anchorInfoEntity.getLive().getImage());
                bundleReserve.putString("LIVE_TITLE", anchorInfoEntity.getLive().getTitle());
                bundleReserve.putString("ANCHOR_ID", anchorId);
                gotoActivity(OneToOneQueueAct.class, false, bundleReserve);
                break;
            case R.id.closeIv:
                finish();
                break;
        }
    }

    private void gotoStartLive(JoinLiveEntity data) {
        if (NIMClient.getStatus() != StatusCode.LOGINED) {
            //未登录
            LoginInfo loginInfo = new LoginInfo(Preferences.getLoginInfo().getAccount(), Preferences.getLoginInfo().getToken(), AppIdConstants.WY_APP_KEY);
            RequestCallback<LoginInfo> callback =
                    new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            if (!TextUtils.isEmpty(param.getAccount())) {
                                DemoCache.setAccount(Preferences.getLoginInfo().getAccount());
                                saveLoginInfo(loginInfo);
                            }
                            gotoLiveStar(data);
                        }

                        @Override
                        public void onFailed(int code) {
                            ToastUtils.showShort("进入失败");

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    };
            NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
        } else if (NIMClient.getStatus() == StatusCode.LOGINED) {
            gotoLiveStar(data);
        }

    }

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        this.anchorInfoEntity = data;
        anchorId = String.valueOf(data.getAnchor_id());
//        abilityStar.setStarMark(data.getService()==0?1:data.getService());
        abilityStar.setStarCount(data.getProfessional() == 0 ? 1 : data.getProfessional());
//        serviceQualitySb.setStarMark(data.getProfessional()==0?1:data.getProfessional());
        serviceQualitySb.setStarCount(data.getService() == 0 ? 1 : data.getService());
        ReserveTv.setText(String.format("已预约：%1$s/%2$s", data.getAppoint_num(), data.getReport_num()));
        GlideUtils.loadImage(this, data.getAvatar(), anchorAvaterCiv);
        GlideUtils.loadImage(this, data.getLevel(), leveImg);
        GlideUtils.loadImage(this, data.getLive().getImage(), liveBgIv);
        anchorNameTv.setText(data.getNickname());
        liveIdTv.setText("ID:" + data.getAnchor_no());
        serviceTv.setText("已服务：" + data.getService_num() + "");
//        ReserveTv.setText("已预约：" + data.getService_num() + "");
        if (anchorInfoEntity.getIs_appoint() == 1) {
            ReserveTv.setSelected(true);
        } else {
            ReserveTv.setSelected(false);
        }

        if (data.getIs_edit() == 1) {//判断是否是主播
            attentionTv.setVisibility(View.GONE);
        } else {
            attentionTv.setVisibility(View.VISIBLE);
            if (data.getIs_sub() == 1) {
                attentionTv.setText("取消关注");
                isSubAnchor = true;
            } else {
                isSubAnchor = false;
                attentionTv.setText("+关注");

            }
            if (data.getLive().getType() == 1) {
                StartLiveTv.setText("预约一对一");
            } else {
                StartLiveTv.setText("进入直播间");
            }

        }

        if (data.getIs_live() == 0) {
            LiveingLL.setVisibility(View.GONE);
            if (data.getIs_edit() != 1) {

                StartLiveTv.setText("预约一对一");
            } else {

                StartLiveTv.setText("未开播");
                StartLiveTv.setAlpha(0.5f);
                StartLiveTv.setEnabled(false);
            }


        } else {
            LiveingLL.setVisibility(View.VISIBLE);
            GlideUtils.loadGif(this, R.mipmap.ic_liveing, liveingIv);
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
    public void setJoinLiveSuccess(JoinLiveEntity data) {
        //       type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
        if (data.getStatus_code().equalsIgnoreCase("302")) {
            ToastUtils.showShort("您已把主播拉黑，无法进入该主播直播间");
            return;
        }
        if ("301".equalsIgnoreCase(data.getStatus_code())) {
            ToastUtils.showShort("为合理控制青少年的使用时间，每日22:00至次日凌晨6:00无法使用百科高手");
            return;
        }
        if (anchorInfoEntity.getIs_live() == 0 && anchorInfoEntity.getIs_edit() != 1) {
            return;
        }
        gotoStartLive(data);


    }

    /**
     * 进入直播间需要付费
     *
     * @param entity
     */
    @Override
    public void setGotoPay(LivePayInfoEntity entity) {
//        if (entity != null && TextUtils.isEmpty(entity.getStatus_code())) {
        PayInLiveDialog dialog = new PayInLiveDialog();
        Bundle bundle = new Bundle();
        bundle.putString("Need_PAY", String.valueOf(entity.getMoney()));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "PayInLiveDialog");
        dialog.setOnTimeListener(new PayInLiveDialog.OnLiveTypeListener() {
            @Override
            public void confirmSuccess() {
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "是否确认支付进入直播间费用");
                bundle2.putString("TITLE_DIO", "温馨提示");
                bundle2.putString("comfirm", "确认支付");
                bundle2.putString("cancel", "取消支付");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.payInLive(anchorInfoEntity.getLive().getId());
                    }
                });
            }
        });
//        }
    }

    @Override
    public void setNoMoney() {
        BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
        Bundle bundle2 = new Bundle();
        bundle2.putString("showContent", "您剩余的高手币不足，请先充值");
        bundle2.putString("TITLE_DIO", "温馨提示");
        bundle2.putString("comfirm", "去充值");
        bundle2.putString("cancel", "取消");
        baseTipsDialog1.setArguments(bundle2);
        baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
        baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                gotoActivity(RechargeAct.class);
            }
        });
    }

    @Override
    public void setLikeAnNoLike(String msg) {
        ToastUtils.showShort(msg);
        if (isSubAnchor) {
            attentionTv.setSelected(true);
            attentionTv.setText("+关注");
        } else {
            attentionTv.setSelected(false);
            attentionTv.setText("取消关注");
        }
        isSubAnchor = !isSubAnchor;
    }

    private void gotoLiveStar(JoinLiveEntity data) {
        if (anchorInfoEntity.getLive().getType() == 1) {
//            if (entity.getIs_edit() == 1) {
//                gotoActivity(StartLiveAct.class, false);
//            } else {
            Bundle bundle = new Bundle();
            bundle.putString("ANCHOR_ID", anchorId);
            gotoActivity(OneToOneQueueAct.class, false, bundle);
//            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("LIVE_BG", anchorInfoEntity.getLive().getImage());
            bundle.putString("LIVE_TITLE", anchorInfoEntity.getLive().getTitle());
            bundle.putInt("UID", data.getInfo().getUid());
            bundle.putString("ANCHOR_ID", anchorId);
            bundle.putString("LIVE_ID", String.valueOf(data.getInfo().getRoom_id()));
            //对众直播
            if (anchorInfoEntity.getIs_edit() == 1) {
                bundle.putBoolean("IS_AVATER", true);
            } else {
                bundle.putBoolean("IS_AVATER", false);
            }
            gotoActivity(StartOneToMoreLiveAct.class, false, bundle);
        }
    }
}