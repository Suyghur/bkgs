package com.pro.maluli.module.home.base;

import android.app.Activity;
import android.content.Intent;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.SelectClassificationDialog;
import com.pro.maluli.common.view.myselfView.CustomViewpager;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.app.BKGSApplication;
import com.pro.maluli.module.home.announcement.AnnouncementAct;
import com.pro.maluli.module.home.base.adapter.HomeTopVideoFrg;
import com.pro.maluli.module.home.base.adapter.SimpleTextAdapter;
import com.pro.maluli.module.home.base.applyForAnchor.ApplyForAnchorAct;
import com.pro.maluli.module.home.base.classify.base.ClassiftAct;
import com.pro.maluli.module.home.base.fragment.child.HomeChildFrag;
import com.pro.maluli.module.home.base.presenter.HomePresenter;
import com.pro.maluli.module.home.base.presenter.IHomeContraction;
import com.pro.maluli.module.home.homeSearch.SearchHomeAct;
import com.pro.maluli.module.home.oneToMore.base.oneToMore.OneToMoreAct;
import com.pro.maluli.module.home.oneToOne.base.oneToMore.OneToOneAct;
import com.pro.maluli.module.home.previewLive.PreviewLiveAct;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;
import com.pro.maluli.module.myself.setting.youthMode.base.YouthModeAct;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.MultiItemTypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页信息
 *
 * @author 23203
 */
public class HomeFrag extends BaseMvpFragment<IHomeContraction.View, HomePresenter> implements IHomeContraction.View {
    private static final int UPTATE_VIEWPAGER = 0;
    @BindView(R.id.mine_mian_ll)
    CoordinatorLayout mine_mian_ll;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.homeTpStl)
    SlidingTabLayout homeTpStl;
    @BindView(R.id.homeViewPager)
    CustomViewpager homeViewPager;
    @BindView(R.id.viewPager)
    ViewPager bannerViewPager;
    @BindView(R.id.pointLL)
    LinearLayout pointLL;
    @BindView(R.id.smarRef)
    SmartRefreshLayout smarRef;
    @BindView(R.id.gotoLiveIv)
    ImageView gotoLiveIv;
    @BindView(R.id.exitTeenagerTv)
    TextView exitTeenagerTv;
    List<Fragment> fragments;
    List<Fragment> topVideoFragment;
    MyPagerAdapter mAdapter;
    TopVideoAdapter topVideoAdapter;
    SimpleTextAdapter simpleTextAdapter;
    // 判断视频是否在播放，如果在播放，则不主动滑动viewpager
    boolean isStartVideo = true;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    boolean isHidden;
    boolean isFirstResume = true;
    CountDownTimer countDownTimer;
    private List<HomeInfoEntity.NoticeBean> noticeBeans = new ArrayList<>();
    //    private BannerViewAdapter bannerViewAdapter;
    private int autoCurrIndex = 0;
    //    private Timer timer;
//    private TimerTask timerTask;
    private ImageView[] tips;
    private long period = 5000;
    private HomeInfoEntity homeInfoEntity;
    private UserInfoEntity userInfoEntity;
    private LastTimeLiveEntity lastTimeLiveEntity;
    private boolean isRefresh = true;//是否下拉刷新了
    private int positionLive = 0;
    private int one, two;

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {
//        presenter.getUserInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                userInfoEntity = (UserInfoEntity) ACache.get(mContext).getAsObject(ACEConstant.USERINFO);
//                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                if (BKGSApplication.youthModeStatus == 1) {
                    gotoLiveIv.setVisibility(View.GONE);
                    exitTeenagerTv.setVisibility(View.VISIBLE);
                    if (isFirstResume) {
                        presenter.getHomeInfo();
                    }
                } else {
                    gotoLiveIv.setVisibility(View.VISIBLE);
                    exitTeenagerTv.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            if (mHandler != null) {
                isStartVideo = false;
                mHandler.removeCallbacksAndMessages(null);
            }
            GSYVideoManager.releaseAllVideos();

        } else {
//            可见
            isStartVideo = true;
            startTime();
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void onPause() {
        super.onPause();
        autoCurrIndex = 0;
        isHidden = true;
    }

    @Override
    public void baseInitialization() {
//        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
//        BarUtils.setStatusBarLightMode(getActivity(), true);

    }

    public boolean isLogin() {
        return userInfoEntity != null;
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home1;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 110 && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                one = data.getIntExtra("ClassiftOne", 0);
//                two = data.getIntExtra("ClassiftTwo", 0);
//                isFristResume = false;
//            }
//            homeViewPager.setCurrentItem(one + 1);   //һ��ʼ��ѡ��
//            HomeChildFrag frag = (HomeChildFrag) fragments.get(homeViewPager.getCurrentItem());
//            frag.searchDuoBaoGoods(two);
//        }
//    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
//        BarUtils.addMarginTopEqualStatusBarHeight(mine_mian_ll);
//        marqueeView.setAdapter(simpleTextAdapter);
        fragments = new ArrayList<>();
        topVideoFragment = new ArrayList<>();


        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                positionLive = position;
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

        smarRef.setRefreshHeader(new ClassicsHeader(requireActivity()));
        smarRef.setRefreshFooter(new ClassicsFooter(requireActivity()));
        smarRef.setEnableLoadMore(false);
        /*
          加载更多
         */
        smarRef.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smarRef.finishLoadMore(1000);
                ToastHelper.showToast(getContext(), "sdfasdf");
            }
        });
        /*
          下拉刷新
         */
        smarRef.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.getHomeInfo();
                isRefresh = true;
                smarRef.finishRefresh(1000);
            }
        });

        GSYVideoManager.instance().setNeedMute(true);
        ACache.get(getActivity()).put("MUTE", "1");
        isStartVideo = true;

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);

        intentActivityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        one = data.getIntExtra("ClassiftOne", 0);
                        two = data.getIntExtra("ClassiftTwo", 0);
                        isFirstResume = false;
                    }
                    homeViewPager.setCurrentItem(one + 1);
                    HomeChildFrag frag = (HomeChildFrag) fragments.get(homeViewPager.getCurrentItem());
                    frag.searchDuoBaoGoods(two);

                }
            }
        });
    }

    @OnClick({R.id.gotoLiveIv, R.id.classificationIv, R.id.searchLL, R.id.exitTeenagerTv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.gotoLiveIv:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                presenter.getLiveInfo();
                break;
            case R.id.searchLL:
                gotoActivity(SearchHomeAct.class);
                break;
            case R.id.exitTeenagerTv://跳到青少年模式
                gotoActivity(YouthModeAct.class);
                break;
            case R.id.classificationIv://跳转到分类
                Bundle bundle = new Bundle();
                bundle.putSerializable("Home_data", homeInfoEntity);
//                gotoActivity(ClassiftAct.class, false, bundle);
                Intent intent = new Intent(requireActivity(), ClassiftAct.class);
                intent.putExtras(bundle);
                intentActivityResultLauncher.launch(intent);
//                startActivityForResult(intent, 110);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfoEntity = (UserInfoEntity) ACache.get(mContext).getAsObject(ACEConstant.USERINFO);
        if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
            gotoLiveIv.setVisibility(View.GONE);
            exitTeenagerTv.setVisibility(View.VISIBLE);
        } else {
            gotoLiveIv.setVisibility(View.VISIBLE);
            exitTeenagerTv.setVisibility(View.GONE);
        }
        if (isFirstResume) {
            presenter.getHomeInfo();
        }
        isFirstResume = true;
        isStartVideo = true;

        if (ACache.get(getActivity()).getAsString("MUTE").equalsIgnoreCase("1")) {
            GSYVideoManager.instance().setNeedMute(true);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GSYVideoManager.instance().setNeedMute(false);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0.5f, 0.5f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void setHomeInfo(HomeInfoEntity data) {
        if (homeInfoEntity != null) {
            if (homeInfoEntity.getCategory().getList().size() == data.getCategory().getList().size() + 1 && !isRefresh) {
                // 判断数组一样就不更新数据
                // 为了实现 首页进入直播主页后返回首页需要回来原先的位置
                Logger.e("判断数组一样就不更新数据");
                homeInfoEntity = data;
                return;
            }
        }

        isRefresh = false;
        homeInfoEntity = data;
        setDianDian(homeInfoEntity.getBanner().size());
        autoBanner();
        noticeBeans.clear();
        noticeBeans = data.getNotice();
        if (noticeBeans.size() > 0) {
            simpleTextAdapter = new SimpleTextAdapter(mContext, noticeBeans);
            simpleTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    gotoActivity(AnnouncementAct.class);
                    if (marqueeView.isStart()) {
                        marqueeView.stopFilp();
                    } else {
                        marqueeView.startFlip();
                    }
                }
            });
            marqueeView.setAdapter(simpleTextAdapter);
        }

        if (fragments != null) {
            fragments.clear();
        }

        HomeInfoEntity.CategoryBean.ListBean listBean = new HomeInfoEntity.CategoryBean.ListBean();
        HomeInfoEntity.CategoryBean.ListBean.ChildBean childBean = new HomeInfoEntity.CategoryBean.ListBean.ChildBean();
        List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> childBeans = new ArrayList<>();
        childBeans.add(childBean);
        listBean.setTitle("全部");
        childBean.setTitle("全部");
        listBean.setChildren(childBeans);
//        Fragment fragment1 = HomeChildFrag.newInstance(listBean.getChildren());
//        fragments.add(0, fragment1);
//        homeInfoEntity.getCategory().getList().;
        homeInfoEntity.getCategory().getList().add(0, listBean);
        for (int i = 0; i < homeInfoEntity.getCategory().getList().size(); i++) {
            Fragment fragment = HomeChildFrag.newInstance(homeInfoEntity.getCategory().getList().get(i).getChildren());
            fragments.add(fragment);
        }
        //new一个适配器
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        //设置ViewPager与适配器关联
        homeViewPager.setAdapter(mAdapter);
        //设置Tab与ViewPager关联
        homeTpStl.setViewPager(homeViewPager);
        homeViewPager.setCurrentItem(positionLive);
    }

    /**
     * 1002 审核未通过
     * <p>
     * 1005 您还不是主播,请先申请为主播
     * <p>
     * 1006 主播申请审核中...请耐心等待审核
     * <p>
     * 1007 您已被禁播
     */
    @Override
    public void setLastLive(LastTimeLiveEntity data) {
        lastTimeLiveEntity = data;
//        gotoActivity(OneToMoreAct.class);
        if (data.getIs_live() == 1) {//主播已经有了直播间
            //       type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
            Bundle bundle = new Bundle();
            bundle.putString("ANCHOR_ID", "");
            bundle.putString("ROOM_ID", String.valueOf(data.getRoom_id()));
            gotoActivity(PreviewLiveAct.class, false, bundle);
            return;
        }
        if (TextUtils.isEmpty(data.getStatus_code())) {
            SelectClassificationDialog selectClassificationDialog = new SelectClassificationDialog();
            selectClassificationDialog.show(getChildFragmentManager(), "SelectClassificationDialog");
            selectClassificationDialog.setOnTimeListener(new SelectClassificationDialog.OnLiveTypeListener() {
                @Override
                public void confirmSuccess(int type) {
                    if (type == 1) {//对众直播
                        gotoActivity(OneToMoreAct.class);
                    } else {//一对一直播
                        gotoActivity(OneToOneAct.class);
                    }
                }
            });
        } else {
            showDialog(data);
        }
    }

    private void showDialog(LastTimeLiveEntity data) {
        BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
        Bundle bundle1 = new Bundle();
        switch (data.getStatus_code()) {
            case "1002":
                bundle1.putString("showContent", "您的申请未能通过审核");
                bundle1.putString("comfirm", "再次提交");
                bundle1.putBoolean("CANCEL_SEE", true);
                break;
            case "1005":
                bundle1.putString("showContent", "您尚未成为主播，是否申请成为主播");
                bundle1.putString("comfirm", "去申请");
                break;
            case "1006":
                bundle1.putString("showContent", "您的主播申请已提交，请耐心等待平台审核");
                bundle1.putString("comfirm", "知道了");
                bundle1.putBoolean("CANCEL_SEE", true);
                break;
            case "1007":
                if (data.getCan_report() == 1) {
                    bundle1.putString("TITLE_DIO", "禁播通知");
                    bundle1.putString("showContent", lastTimeLiveEntity.getReason());
                    bundle1.putString("comfirm", "去申诉");
                } else {
                    bundle1.putString("TITLE_DIO", "禁播通知");
                    bundle1.putString("showContent", "您的申诉已提交，请等待后台反馈");
                    bundle1.putString("comfirm", "知道了");
                    bundle1.putBoolean("CANCEL_SEE", true);
                }
                break;
        }
        baseTipsDialog.setArguments(bundle1);
        baseTipsDialog.show(getChildFragmentManager(), "BaseTipsDialog");
        baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                Bundle bundle = new Bundle();
                switch (data.getStatus_code()) {
                    case "1005":
                        bundle.putSerializable("classification", (Serializable) homeInfoEntity.getCategory().getList());
                        gotoActivity(ApplyForAnchorAct.class, false, bundle);
                        break;
                    case "1002":
                        bundle.putString("REASON", lastTimeLiveEntity.getReason());
                        bundle.putSerializable("classification", (Serializable) homeInfoEntity.getCategory().getList());
                        gotoActivity(ApplyForAnchorAct.class, false, bundle);
                        break;
                    case "1006":
                        break;
                    case "1007":
                        if (data.getCan_report() == 1) {
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("SUB_TYPE", "1");
                            gotoActivity(AppealAct.class, false, bundle2);
                        }
                        break;
                }

            }
        });
    }

    private void autoBanner() {
        topVideoFragment.clear();
        for (int i = 0; i < homeInfoEntity.getBanner().size(); i++) {
            HomeTopVideoFrg fragment = (HomeTopVideoFrg) HomeTopVideoFrg.newInstance(homeInfoEntity.getBanner().get(i));
            topVideoFragment.add(fragment);
            fragment.setBannerListener(new HomeTopVideoFrg.BannerListener() {
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
                    if (isHidden) {
                        return;
                    }
                    isStartVideo = true;
                    startTime();
                }
            });
        }
//        //new一个适配器
        topVideoAdapter = new TopVideoAdapter(getChildFragmentManager());
        //设置ViewPager与适配器关联
        bannerViewPager.setAdapter(topVideoAdapter);
        bannerViewPager.setCurrentItem(0);   //
//
//        bannerViewPager.setOffscreenPageLimit(0);
//        bannerViewAdapter = new BannerViewAdapter(getActivity(), homeInfoEntity.getBanner());
//        bannerViewPager.setAdapter(bannerViewAdapter);
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HomeInfoEntity.BannerBean banner = homeInfoEntity.getBanner().get(position);
                setImageBackground(position);
                autoCurrIndex = position;
                if (banner.getFile_type() != 1) {
                    if (!isHidden) {
                        isStartVideo = false;
                    }
                }

                if (isHidden) {
                    return;
                }
                startTime();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        isHidden = false;
        startTime();
    }

    private void startTime() {
        if (isHidden) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Message message = new Message();
                message.what = UPTATE_VIEWPAGER;
                if (homeInfoEntity == null) {
                    return;
                }
                if (!isStartVideo) {
                    return;
                }
                if (autoCurrIndex == homeInfoEntity.getBanner().size() - 1) {
                    autoCurrIndex = -1;
                }
                message.arg1 = autoCurrIndex + 1;
                mHandler.sendMessage(message);
            }
        };
        countDownTimer.start();
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
    }    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == UPTATE_VIEWPAGER) {
                if (isHidden) {
                    return;
                }
                Logger.e("setCurrentItem");
                if (msg.arg1 != 0) {
                    bannerViewPager.setCurrentItem(msg.arg1);
                } else {
                    bannerViewPager.setCurrentItem(msg.arg1, false);
                }
                startTime();
            }
        }
    };

    private void setDianDian(int a) {
        tips = new ImageView[a];
        pointLL.removeAllViews();
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(getActivity());
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
    public void onDestroy() {
        super.onDestroy();
//        GSYVideoManager.releaseAllVideos();
        try {
            GSYVideoManager.instance().getPlayer().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
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
            return homeInfoEntity.getCategory().getList().get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public long getItemId(int position) {
            //修改getItemId   不与位置对应  返回List<Fragment>中Fragment的hashCode
            return fragments.get(position).hashCode();
        }
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
            return homeInfoEntity.getBanner().get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return topVideoFragment.get(position);
        }
    }


}