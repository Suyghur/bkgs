package com.pro.maluli.module.video.videoact;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.WelcomInfoEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.CustomVideoView;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.video.base.adapter.VideoHomeAdapter;
import com.pro.maluli.module.video.events.ClearPositionEvent;
import com.pro.maluli.module.video.videoact.adapter.VideoAnchorAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class VideoAct extends BaseMvpActivity<IVideoActContraction.View, VideoActPresenter> implements View.OnClickListener, IVideoActContraction.View {
    @BindView(R.id.viewPager)
    ViewPager2 viewPager;

    String anchorID;
    private VideoAnchorAdapter mPagerAdapter;
    private int position;

    @Override
    public VideoActPresenter initPresenter() {
        return new VideoActPresenter(this);
    }

    @Override
    public void baseInitialization() {
//        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
//        BarUtils.setStatusBarLightMode(this, true);
//        StatusbarUtils.setStatusBarView(this);
        BarUtils.setStatusBarVisibility(this, false);
        anchorID = getIntent().getStringExtra("AnchorID");
        position = getIntent().getIntExtra("Position", 0);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_video_anchor;
    }

    @Override
    public void viewInitialization() {

    }

    @OnClick({R.id.finishIv})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.finishIv:
                finish();
                break;
        }
    }

    @Override
    public void doBusiness() {
        presenter.getAnchorInfo(anchorID);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        List<AnchorInfoEntity.VideoBean> video = data.getVideo();
        this.mPagerAdapter = new VideoAnchorAdapter(getSupportFragmentManager(), this.getLifecycle(), video, anchorID);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EventBus.getDefault().post(new ClearPositionEvent(true));
//                if (position == videoBeans.size() - 1) {
////                    urlList.add(urlList.get(0));
////                    mPagerAdapter.notifyDataSetChanged();
//                    presenter.getVideo();
//                }
            }
        });
    }
}