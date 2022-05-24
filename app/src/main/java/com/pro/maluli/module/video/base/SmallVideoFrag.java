package com.pro.maluli.module.video.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.TipsNofinishDialog;
import com.pro.maluli.module.other.login.LoginAct;
import com.pro.maluli.module.video.base.adapter.VideoHomeAdapter;
import com.pro.maluli.module.video.base.presenter.SmallVideoPresenter;
import com.pro.maluli.module.video.base.presenter.ISmallVideoContraction;
import com.pro.maluli.module.video.events.CanScrollEvent;
import com.pro.maluli.module.video.events.CanStartEvent;
import com.pro.maluli.module.video.events.ClearPositionEvent;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.collections.CollectionsKt;

/**
 * 首页信息
 *
 * @author 23203
 */
public class SmallVideoFrag extends BaseMvpFragment<ISmallVideoContraction.View, SmallVideoPresenter> implements ISmallVideoContraction.View {
    @BindView(R.id.mine_mian_ll)
    FrameLayout mine_mian_ll;
    @BindView(R.id.viewPager)
    ViewPager2 viewPager;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.tipsVideoTv)
    TextView tipsVideoTv;
    private VideoHomeAdapter mPagerAdapter;
    private List<VideoEntity> videoBeans = new ArrayList<>();
    CountDownTimer countDownTimer;
    private static final int UPTATE_SMALL_VIDEO = 0;

    /**
     * ,
     * "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
     * "https://vd2.bdstatic.com/mda-jhd0td59pep5iaw6/mda-jhd0td59pep5iaw6.mp4",
     * "https://vd4.bdstatic.com/mda-jdaw440gtcpng8r0/sc/mda-jdaw440gtcpng8r0.mp4",
     * "https://vd4.bdstatic.com/mda-mcpq4utc5c447fu0/sc/cae_h264/1616576786/mda-mcpq4utc5c447fu0.mp4"
     *
     * @return
     */
    @Override
    public SmallVideoPresenter initPresenter() {
        return new SmallVideoPresenter(getActivity());
    }

    @Override
    public void onWakeBussiness() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        EventBus.getDefault().post(new CanStartEvent(hidden));
        isHidden = hidden;
        if (hidden) {
            GSYVideoManager.onPause();
//            不可见
//            GSYVideoManager.instance().setNeedMute(false);
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
        } else {
//            可见
            startTime();
        }
    }

    private void startTime() {
        if (isHidden) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Message message = new Message();
                message.what = UPTATE_SMALL_VIDEO;
                mHandler.sendMessage(message);
            }
        };
        countDownTimer.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPTATE_SMALL_VIDEO:
                    if (isHidden) {
                        return;
                    }
                    presenter.videoTime();
//                    startTime();
                    break;
            }
        }
    };

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
//        EventBus.getDefault().register(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_small_video;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        BarUtils.addMarginTopEqualStatusBarHeight(mine_mian_ll);

        this.mPagerAdapter = new VideoHomeAdapter(getChildFragmentManager(), getActivity().getLifecycle(), videoBeans);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EventBus.getDefault().post(new ClearPositionEvent(true));
//                cantSeeVideo("游客限制观看");//游客限制
                if (position == videoBeans.size() - 1) {
//                    urlList.add(urlList.get(0));
//                    mPagerAdapter.notifyDataSetChanged();
                    presenter.getVideo();
                }
            }
        });
        tipsVideoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(LoginAct.class);
            }
        });
    }

    @Override
    public void onError(int code, String msg) {
        showToast(msg);
        nodataView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onCanScrollState(CanScrollEvent event) {
//        if (viewPager != null) {
//            viewPager.setUserInputEnabled(event.isScroll());
//        }
//    }

    @Override
    public void doBusiness() {
        presenter.getVideo();

    }

    @Override
    public void onPause() {
        super.onPause();
//        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.instance().setNeedMute(false);
        presenter.videoTime();
        tipsVideoTv.setVisibility(View.GONE);
//        cantSeeVideo("nihao",true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void setVideoInfo(VideoEntity data) {
        if (data != null) {
            videoBeans.add(data);
        }
        isSeeVideo = false;
//        cantSeeVideo("游客限制观看");
        nodataView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setvideoTime() {
        startTime();
    }

    boolean isSeeVideo;

    @Override
    public void setNoSeeVideo(String msg) {
        isSeeVideo = true;
        cantSeeVideo(msg);
    }

    private void cantSeeVideo(String msg) {
        videoBeans.clear();
        mPagerAdapter.notifyDataSetChanged();
//        TipsNofinishDialog baseTipsDialog = new TipsNofinishDialog();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString("showContent", msg);
//        baseTipsDialog.setArguments(bundle1);
//        baseTipsDialog.show(getChildFragmentManager(), "BaseTipsDialog");
//        baseTipsDialog.setOnConfirmListener(new TipsNofinishDialog.OnBaseTipsListener() {
//            @Override
//            public void comfirm() {
//                gotoActivity(LoginAct.class);
//            }
//        });
//        viewPager.setOverScrollMode();
        if (isSeeVideo) {
            tipsVideoTv.setVisibility(View.VISIBLE);
            tipsVideoTv.setText(msg);
        } else {
            tipsVideoTv.setVisibility(View.GONE);
        }
        CanStartEvent canStartEvent = new CanStartEvent();
        canStartEvent.setNoseet(isSeeVideo);
        EventBus.getDefault().post(canStartEvent);

    }
}