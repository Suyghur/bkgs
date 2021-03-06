package com.pro.maluli.module.myself.anchorInformation.base;

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.netease.nim.uikit.api.NimUIKit;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.ShareVideoDialog;
import com.pro.maluli.common.view.myselfView.CustomViewpager;
import com.pro.maluli.common.view.myselfView.StarBar;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.home.oneToOne.base.oneToMore.OneToOneAct;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.home.previewLive.PreviewLiveAct;
import com.pro.maluli.module.myself.anchorInformation.anchorMore.AnchorMoreAct;
import com.pro.maluli.module.myself.anchorInformation.base.presenter.AnchorInformationPresenter;
import com.pro.maluli.module.myself.anchorInformation.base.presenter.IAnchorInformationContraction;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.AnchorImageFrag;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.AnchorInfoFrag;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base.AnchorVideoFrag;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;

import java.util.ArrayList;
import java.util.List;

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
public class AnchorInformationAct extends BaseMvpActivity<IAnchorInformationContraction.View, AnchorInformationPresenter> implements IAnchorInformationContraction.View {

    @BindView(R.id.appbarLayout)
    AppBarLayout appbarLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.view_toolbar)
    Toolbar view_toolbar;
    @BindView(R.id.anchorNameTv)
    TextView anchorNameTv;
    @BindView(R.id.oneClassTv)
    TextView oneClassTv;
    @BindView(R.id.twoClassTv)
    TextView twoClassTv;
    @BindView(R.id.jsyyTv)
    TextView jsyyTv;
    @BindView(R.id.serviceNumberTv)
    TextView serviceNumberTv;
    @BindView(R.id.liveIdTv)
    TextView liveIdTv;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.fansNumberTv)
    TextView fansNumberTv;
    @BindView(R.id.LiveingLL)
    LinearLayout LiveingLL;
    @BindView(R.id.oneTomoreLl)
    LinearLayout oneTomoreLl;
    @BindView(R.id.liveingIv)
    ImageView liveingIv;
    @BindView(R.id.leveImg)
    ImageView leveImg;
    @BindView(R.id.genderImg)
    ImageView genderImg;
    LinearLayout.LayoutParams linearParams;
    List<Fragment> fragments;
    @BindView(R.id.homeTpStl)
    SlidingTabLayout homeTpStl;
    @BindView(R.id.anchorAvaterCiv)
    CircleImageView anchorAvaterCiv;
    @BindView(R.id.homeViewPager)
    CustomViewpager homeViewPager;
    @BindView(R.id.abilityStar)
    StarBar abilityStar;
    @BindView(R.id.zhuanyeStar)
    StarBar zhuanyeStar;
    @BindView(R.id.nestedSv)
    NestedScrollView nestedSv;
    @BindView(R.id.videoPlayer)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.bottomControl)
    LinearLayout bottomControl;
    @BindView(R.id.topControlRl)
    RelativeLayout topControlRl;
    @BindView(R.id.attentionImg)
    ImageView attentionImg;
    @BindView(R.id.liveTypeTv)
    TextView liveTypeTv;
    @BindView(R.id.titlebar_ly)
    RelativeLayout titlebar_ly;
    @BindView(R.id.topMoreIv)
    ImageView topMoreIv;
    @BindView(R.id.moreIv)
    ImageView moreIv;
    MyPagerAdapter mAdapter;
    AnchorInfoEntity anchorInfoEntity;
    UserInfoEntity userInfoEntity;
    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        int height=huadogn.getHeight();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                offset=offset-(int)event.getY();
//                if (offset>200){
//                    linearParams.height=400;
//                    huadogn.setLayoutParams(linearParams);
//                }else if (offset<-200){
//                    linearParams.height=800;
//                    huadogn.setLayoutParams(linearParams);
//                }
//
//                break;
//            case MotionEvent.ACTION_DOWN:
//                offset=(int) event.getY();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//??????Handler?????????????????????UI??????????????????
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ??????
                case 0:
                    ToastUtils.showShort("????????????");
                    break;
                //????????????
                case 1:
                    ToastUtils.showShort("????????????");
                    break;
            }
        }
    };
    private boolean isLike;//????????????
    private List<String> title = new ArrayList<>();
    private String anchorID;
    private boolean isFrist;
    private String webUrl, accidAnchor, new_video;
    private int selectPosition = 0;
    private int offset;

    @Override
    public AnchorInformationPresenter initPresenter() {
        return new AnchorInformationPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        userInfoEntity = (UserInfoEntity) ACache.get(this).getAsObject(ACEConstant.USERINFO);
        anchorID = getIntent().getStringExtra("AnchorID");
        presenter.anchorID = anchorID;
    }

    @Override
    public void setLikeAnNoLike(String msg) {
        if (isLike) {
            attentionImg.setSelected(true);
            attentionTv.setSelected(true);
            attentionTv.setText("+??????");
        } else {
            attentionImg.setSelected(false);
            attentionTv.setSelected(false);
            attentionTv.setText("????????????");
        }
        isLike = !isLike;
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_anchor_information;
    }

    @Override
    public void viewInitialization() {
        linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        appbarLayout.setExpanded(false);
//        nestedSv.scrollTo(0, 300);
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (!videoPlayer.isInPlayingState()) {
                        if (isFrist) {
                            videoPlayer.setUpLazy(new_video, false, null, null, "");
                            videoPlayer.startPlayLogic();
                            Logger.e("??????");
                        }

                    }
                    if (isFrist) {
                        return;
                    }
                    CoordinatorLayout.Behavior behavior1 = ((CoordinatorLayout.LayoutParams) appbarLayout.getLayoutParams()).getBehavior();
                    if (behavior1 != null) {
                        behavior1.onNestedPreScroll(coordinator, appbarLayout, nestedSv, 0,
                                appbarLayout.getHeight() - view_toolbar.getHeight(), new int[]{0, 0}, TYPE_NON_TOUCH);
                    }
                    isFrist = true;
                } else if (verticalOffset == appBarLayout.getTotalScrollRange()) {
                    String sdal = "";
                } else if (Math.abs(verticalOffset) + Math.abs(view_toolbar.getHeight()) >= appbarLayout.getHeight()) {
                    topControlRl.setVisibility(View.VISIBLE);
                    if (videoPlayer.isInPlayingState() && videoPlayer.getCurrentState() != GSYVideoView.CURRENT_STATE_NORMAL) {
                        Logger.e("??????");
                        Logger.e("sdfas" + verticalOffset);
                        videoPlayer.onVideoPause();
                        videoPlayer.onVideoReset();
                    }
                }
                if (Math.abs(verticalOffset) >= view_toolbar.getHeight()) {
                    topControlRl.setVisibility(View.GONE);
                }
            }
        });
        fragments = new ArrayList<>();
        title.add("????????????");
        title.add("????????????");
        title.add("?????????");
    }

    @Override
    public void doBusiness() {

//        presenter.getAboutInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getAnchorInfo(anchorID);
        isFrist = false;
        videoPlayer.getCurrentPlayer().onVideoResume(false);
    }

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        anchorInfoEntity = data;
        fragments.clear();
        accidAnchor = data.getAnchor_accid();
        webUrl = data.getShare_url();

        if (data.getIs_live() == 1) {//????????????
            LiveingLL.setVisibility(View.VISIBLE);
            GlideUtils.loadGif(this, R.mipmap.ic_liveing, liveingIv);
        } else {
            LiveingLL.setVisibility(View.GONE);
        }
        if (data.getLive() != null) {
            if (data.getLive().getType() == 1) {//???????????? (1:?????????, 2:?????????,3:????????????)
                liveTypeTv.setText("??????????????????");
            } else if (data.getLive().getType() == 2) {
                liveTypeTv.setText("???????????????");
            } else {
                liveTypeTv.setVisibility(View.GONE);
            }
        }

        //?????????????????????
        if (data.getIs_edit() == 1) {
            moreIv.setVisibility(View.GONE);
            topMoreIv.setVisibility(View.GONE);
            bottomControl.setVisibility(View.GONE);
        } else {
            moreIv.setVisibility(View.VISIBLE);
            topMoreIv.setVisibility(View.VISIBLE);
            bottomControl.setVisibility(View.VISIBLE);
            if (data.getIs_sub() == 1) {
                isLike = true;
                attentionImg.setSelected(false);
                attentionTv.setSelected(false);
                attentionTv.setText("????????????");
            } else {
                attentionTv.setText("+??????");
                attentionImg.setSelected(true);
                attentionTv.setSelected(true);

                isLike = false;
            }

        }

        GlideUtils.loadImage(this, data.getAvatar(), anchorAvaterCiv);
        GlideUtils.loadImage(this, data.getLevel(), leveImg);
        anchorNameTv.setText(data.getNickname());
        liveIdTv.setText("ID:" + data.getAnchor_no());
        if ("???".equalsIgnoreCase(data.getSex())) {//????????????
            genderImg.setSelected(false);
        } else {
            genderImg.setSelected(true);
        }
        if (data.getIs_report() == 1) {//??????????????????
            jsyyTv.setVisibility(View.VISIBLE);
        } else {
            jsyyTv.setVisibility(View.GONE);
        }
        oneClassTv.setText(data.getCate_one());
        twoClassTv.setText(data.getCate());
        serviceNumberTv.setText(data.getService_num() + "");
        fansNumberTv.setText(data.getFans() + "");
//        abilityStar.setStarMark(data.getService());
        abilityStar.setStarCount(data.getService() == 0 ? 1 : data.getService());
        zhuanyeStar.setStarCount(data.getProfessional() == 0 ? 1 : data.getProfessional());
        fragments.add(AnchorInfoFrag.newInstance(anchorID));
//        fragments.add(AnchorImageFrag.newInstance(data.getPicture()));
        fragments.add(AnchorImageFrag.newInstance(anchorID));
        fragments.add(AnchorVideoFrag.newInstance(anchorID));
        //new???????????????
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        //??????ViewPager??????????????????
        homeViewPager.setAdapter(mAdapter);
        //??????Tab???ViewPager??????
        homeTpStl.setViewPager(homeViewPager);
        homeTpStl.setCurrentTab(selectPosition);
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition = position;
                for (int i = 0; i < homeTpStl.getTabCount(); i++) {
                    if (position == i) {
                        homeTpStl.getTitleView(position).setTextSize(18f);
                    } else {
                        homeTpStl.getTitleView(i).setTextSize(16f);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        new_video = data.getNew_video();
        if (TextUtils.isEmpty(new_video)) {
            titlebar_ly.setVisibility(View.VISIBLE);
            appbarLayout.setVisibility(View.GONE);
            topControlRl.setVisibility(View.GONE);
            return;
        } else {
            titlebar_ly.setVisibility(View.GONE);
            appbarLayout.setVisibility(View.VISIBLE);
            topControlRl.setVisibility(View.VISIBLE);
        }
        videoPlayer.onVideoPause();
        videoPlayer.onVideoReset();
//        nestedSv.setNestedScrollingEnabled(false);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.loadImage(AnchorInformationAct.this, data.getNew_video_cover(), imageView);
        videoPlayer.setThumbImageView(imageView);
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
//        videoPlayer.getStartButton().setVisibility(View.GONE);
        videoPlayer.setNeedShowWifiTip(false);
        videoPlayer.setLooping(false);
        videoPlayer.setDismissControlTime(0);
        videoPlayer.setUpLazy(data.getNew_video(), false, null, null, "");
    }

    @Override
    public void setAboutMeInfo(AboutMeEntity data) {
//        imgUrl = data.getShare().getImage();
    }

    @OnClick({R.id.oneTomoreLl, R.id.likeAnchorLL, R.id.oneToOneLL, R.id.moreIv, R.id.topMoreIv, R.id.leftImg_ly, R.id.shareAppLL, R.id.finishIv, R.id.messageLL, R.id.anchorAvaterCiv})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.finishIv:
            case R.id.leftImg_ly:
                finish();
                break;
            case R.id.anchorAvaterCiv://??????????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
//                ArrayList<String> urls = new ArrayList<>();
//                urls.add(anchorInfoEntity.getAvatar());
//                CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
//                Bundle bundlePic = new Bundle();
//                bundlePic.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
//                bundlePic.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, 0);
//                bigPictureDialog.setArguments(bundlePic);
//                bigPictureDialog.show(getSupportFragmentManager(), "CheckBigPictureDialog");
                Intent intent = new Intent(AnchorInformationAct.this, SeeBigPictuerAct.class);
                intent.putExtra("imgUrl", anchorInfoEntity.getAvatar());
                startActivity(intent);
                break;
            case R.id.moreIv://?????????????????????
            case R.id.topMoreIv://?????????????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                Bundle bundleMore = new Bundle();
                bundleMore.putString("IS_BLACK", String.valueOf(anchorInfoEntity.getIs_back()));
                bundleMore.putString("anchorID", String.valueOf(anchorInfoEntity.getAnchor_id()));
                bundleMore.putString("anchorAccuid", String.valueOf(anchorInfoEntity.getAnchor_accid()));
                gotoActivity(AnchorMoreAct.class, false, bundleMore);
                break;
            case R.id.oneTomoreLl://????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                Logger.d(anchorInfoEntity.toString());
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                    ToastUtils.showShort("?????????????????????????????????");
                    return;
                }

                if (anchorInfoEntity.getIs_ban_live() == 1) {
                    ToastUtils.showShort("??????????????????????????????");
                    return;
                }

                if (anchorInfoEntity.getIs_edit() == 1 && anchorInfoEntity.getLive().getType() == 0) {
                    if (anchorInfoEntity.getIs_ban_live() == 1) {
                        ToastUtils.showShort("??????????????????????????????");
                        return;
                    }
                    ToastUtils.showShort("????????????????????????????????????????????????");
//                    gotoActivity(OneToMoreAct.class);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("ANCHOR_ID", anchorID);
                    bundle1.putString("ROOM_ID", String.valueOf(anchorInfoEntity.getLive().getId()));
                    gotoActivity(PreviewLiveAct.class, false, bundle1);
                }


                break;
            case R.id.oneToOneLL://???????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                    ToastUtils.showShort("?????????????????????????????????");
                    return;
                }
                if (anchorInfoEntity.getIs_ban_live() == 1) {
                    ToastUtils.showShort("??????????????????????????????");
                    return;
                }
                //,???????????? (1:?????????, 2:?????????,3:????????????)
                if (anchorInfoEntity.getIs_edit() == 1 && anchorInfoEntity.getIs_live() == 0) {
                    gotoActivity(OneToOneAct.class);
//                    ToastUtils.showShort("????????????????????????????????????????????????");
                    return;
                }
                Bundle bundleReserve = new Bundle();
                bundleReserve.putString("ANCHOR_ID", presenter.anchorID);
                gotoActivity(OneToOneQueueAct.class, false, bundleReserve);

                break;
            case R.id.likeAnchorLL://????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                presenter.anchorSub();
                break;
            case R.id.messageLL://??????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                    ToastUtils.showShort("????????????????????????????????????????????????");
                    return;
                }
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                NimUIKit.startP2PSession(this, accidAnchor);
                break;
            case R.id.shareAppLL://????????????
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (!ToolUtils.isLoginTips(AnchorInformationAct.this, getSupportFragmentManager())) {
                    return;
                }
                ShareVideoDialog dialogFragment = new ShareVideoDialog();
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_SHARE_VIDEO", false);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "ForgetPwdDialog");
                dialogFragment.setOnShareAppListener(new ShareVideoDialog.OnShareAppListener() {
                    @Override
                    public void gotoShare(int type) {
                        // 1 QQ 2 ?????? 3????????????4 QQ??????
                        switch (type) {
                            case 1:
                                ToolUtils.shareURL(handler, QQ.NAME, webUrl,
                                        anchorInfoEntity.getNickname(), anchorInfoEntity.getAvatar());
                                break;
                            case 2:
                                ToolUtils.shareURL(handler, Wechat.NAME, webUrl,
                                        anchorInfoEntity.getNickname(), anchorInfoEntity.getAvatar());
                                break;
                            case 3:
                                ToolUtils.shareURL(handler, WechatMoments.NAME, webUrl,
                                        anchorInfoEntity.getNickname(), anchorInfoEntity.getAvatar());
                                break;
                            case 4:
                                ToolUtils.shareURL(handler, QZone.NAME, webUrl,
                                        anchorInfoEntity.getNickname(), anchorInfoEntity.getAvatar());
                                break;
                        }

                    }

                    @Override
                    public void downloadVideo() {

                    }
                });
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.getCurrentPlayer().onVideoPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.getCurrentPlayer().release();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}