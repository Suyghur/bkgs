package com.pro.maluli.module.home.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cy.tablayoutniubility.FragPageAdapterVp;
import com.cy.tablayoutniubility.TabAdapter;
import com.cy.tablayoutniubility.TabLayoutScroll;
import com.cy.tablayoutniubility.TabMediatorVp;
import com.cy.tablayoutniubility.TabViewHolder;
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
import com.pro.maluli.ktx.bus.BusKey;
import com.pro.maluli.ktx.bus.LiveDataBus;
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
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
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
    private static final int UPDATE_VIEWPAGER = 0;
    @BindView(R.id.mine_mian_ll)
    CoordinatorLayout mine_mian_ll;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.homeTbs)
    TabLayoutScroll homeTbs;
    @BindView(R.id.homeViewPager)
    ViewPager homeViewPager;
    @BindView(R.id.viewPager)
    ViewPager bannerViewPager;
    @BindView(R.id.pointLL)
    LinearLayout pointLL;
    @BindView(R.id.smarRef)
    SmartRefreshLayout smarRef;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.gotoLiveIv)
    ImageView gotoLiveIv;
    @BindView(R.id.exitTeenagerTv)
    TextView exitTeenagerTv;
    List<Fragment> fragments;
    List<Fragment> topVideoFragment;
    FragPageAdapterVp<HomeInfoEntity.CategoryBean.ListBean> fragmentPageAdapter;
    TabAdapter<HomeInfoEntity.CategoryBean.ListBean> tabAdapter;
    TopVideoAdapter topVideoAdapter;
    SimpleTextAdapter simpleTextAdapter;
    // 判断视频是否在播放，如果在播放，则不主动滑动viewpager
    boolean isStartVideo = true;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    boolean isHidden;
    boolean isFirstResume = true;
    CountDownTimer countDownTimer;
    private List<HomeInfoEntity.NoticeBean> noticeBeans = new ArrayList<>();
    private int autoCurrIndex = 0;
    private ImageView[] tips;
    private HomeInfoEntity homeInfoEntity;
    private UserInfoEntity userInfoEntity;
    private LastTimeLiveEntity lastTimeLiveEntity;
    //是否下拉刷新了
    private boolean isRefresh = true;
    private int one, two = 0;

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            isStartVideo = false;
            mHandler.removeCallbacksAndMessages(null);
            GSYVideoManager.releaseAllVideos();

        } else {
            isStartVideo = true;
            startTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        autoCurrIndex = 0;
        isHidden = true;
    }

    @Override
    public void baseInitialization() {
        LiveDataBus.get().with(BusKey.EVENT_UPDATE_HOME_DATA, Integer.class).observe(this, flag -> {
            presenter.getHomeInfo();
        });
    }

    public boolean isLogin() {
        return userInfoEntity != null;
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home1;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        fragments = new ArrayList<>();
        topVideoFragment = new ArrayList<>();
        fragmentPageAdapter = new FragPageAdapterVp<HomeInfoEntity.CategoryBean.ListBean>(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment createFragment(HomeInfoEntity.CategoryBean.ListBean bean, int position) {
                return fragments.get(position);
            }

            @Override
            public void bindDataToTab(TabViewHolder holder, int position, HomeInfoEntity.CategoryBean.ListBean bean, boolean isSelected) {
                TextView textView = holder.getView(R.id.tv_label);
                if (isSelected) {
                    textView.setTextColor(Color.parseColor("#8E1D77"));
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    textView.setTextColor(Color.parseColor("#94959B"));
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                textView.setText(bean.getTitle());
            }

            @Override
            public int getTabLayoutID(int position, HomeInfoEntity.CategoryBean.ListBean bean) {
                return R.layout.item_home_label;
            }
        };
        tabAdapter = new TabMediatorVp<HomeInfoEntity.CategoryBean.ListBean>(homeTbs, homeViewPager).setAdapter(fragmentPageAdapter);

        smarRef.setRefreshHeader(header);
        smarRef.setEnableLoadMore(false);
        smarRef.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                presenter.getHomeInfo();
                isRefresh = true;
                smarRef.finishRefresh(1000);
            }
        });
        GSYVideoManager.instance().setNeedMute(true);
        ACache.get(getActivity()).put("MUTE", "1");
        isStartVideo = true;

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
                    HomeChildFrag frag = (HomeChildFrag) fragments.get(one + 1);
                    frag.childVP.setCurrentItem(two);
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
                Intent intent = new Intent(requireActivity(), ClassiftAct.class);
                intent.putExtras(bundle);
                intentActivityResultLauncher.launch(intent);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        userInfoEntity = (UserInfoEntity) ACache.get(requireActivity()).getAsObject(ACEConstant.USERINFO);
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

        isFirstResume = false;
        isStartVideo = true;

        if (PlayerFactory.getPlayManager().getMediaPlayer() == null) {
            return;
        }

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
        if (BKGSApplication.youthModeStatus == 1) {
            gotoLiveIv.setVisibility(View.GONE);
            exitTeenagerTv.setVisibility(View.VISIBLE);
        } else {
            gotoLiveIv.setVisibility(View.VISIBLE);
            exitTeenagerTv.setVisibility(View.GONE);
        }
        if (homeInfoEntity != null && homeInfoEntity.getCategory().getList().size() == data.getCategory().getList().size() + 1 && !isRefresh) {
            // 判断数组一样就不更新数据
            // 为了实现 首页进入直播主页后返回首页需要回来原先的位置
            Logger.e("判断数组一样就不更新数据");
            homeInfoEntity = data;
            return;
        }

        isRefresh = false;
        homeInfoEntity = data;
        setDianDian(homeInfoEntity.getBanner().size());
        autoBanner();
        noticeBeans.clear();
        noticeBeans = data.getNotice();
        if (noticeBeans.size() > 0) {
            simpleTextAdapter = new SimpleTextAdapter(requireActivity(), noticeBeans);
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

        listBean.setTitle("全部");
        childBean.setTitle("全部");
        childBeans.add(childBean);
        listBean.setChildren(childBeans);
        homeInfoEntity.getCategory().getList().add(0, listBean);
        for (int i = 0; i < homeInfoEntity.getCategory().getList().size(); i++) {
            Fragment fragment = HomeChildFrag.newInstance(homeInfoEntity.getCategory().getList().get(i).getChildren());
            fragments.add(fragment);
        }
        fragmentPageAdapter.clear();
        tabAdapter.clear();
        fragmentPageAdapter.add(homeInfoEntity.getCategory().getList());
        tabAdapter.add(homeInfoEntity.getCategory().getList());
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
                message.what = UPDATE_VIEWPAGER;
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
    }

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

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_VIEWPAGER) {
                if (isHidden) {
                    return;
                }
                if (msg.arg1 != 0) {
                    bannerViewPager.setCurrentItem(msg.arg1);
                } else {
                    bannerViewPager.setCurrentItem(msg.arg1, false);
                }
                startTime();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        mHandler.removeCallbacksAndMessages(null);
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