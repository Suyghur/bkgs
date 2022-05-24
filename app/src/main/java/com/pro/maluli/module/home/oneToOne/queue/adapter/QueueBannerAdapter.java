package com.pro.maluli.module.home.oneToOne.queue.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.home.base.adapter.CoustormGsyVideoPlayer;
import com.pro.maluli.module.home.base.adapter.RoundVideoView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;

import java.util.ArrayList;
import java.util.List;


public class QueueBannerAdapter extends PagerAdapter {
    CoustormGsyVideoPlayer jcVideoPlayer;
    private Context context;
    private List<ReserveEntity.BannerBean> listBean;
    private String url;

    public QueueBannerAdapter(Activity context, List<ReserveEntity.BannerBean> list) {
//        this.context = context.getApplicationContext();
        this.context = context;
        if (list == null || list.size() == 0) {
            this.listBean = new ArrayList<>();
        } else {
            this.listBean = list;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ReserveEntity.BannerBean bannerBean = listBean.get(position);
        if (bannerBean.getFile_type() == 1) {//ͼƬ
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AntiShake.check(view.getId())) {
                        return;
                    }
                    if (bannerListener != null) {
                        bannerListener.imgClick(position);
                    }
                }
            });
            Glide.with(context).load(bannerBean.getUrl())
//                    .skipMemoryCache(true)
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        } else {//
             View view = LayoutInflater.from(context).inflate(R.layout.main_top_video, container, false);
            jcVideoPlayer = view.findViewById(R.id.jcVideoPlayer);
            RelativeLayout topOnclickRl =view.findViewById(R.id.topOnclickRl);
            topOnclickRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AntiShake.check(view.getId())) {
                        return;
                    }
                    if (bannerListener != null) {
                        bannerListener.imgClick(position);
                    }
                }
            });
            jcVideoPlayer.onVideoPause();
            jcVideoPlayer.onVideoReset();
//            StandardGSYVideoPlayer jcVideoPlayer = new StandardGSYVideoPlayer(context);
            jcVideoPlayer.getBackButton().setVisibility(View.GONE);
            jcVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            jcVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
//            jcVideoPlayer.getStartButton().setVisibility(View.GONE);
            jcVideoPlayer.setNeedShowWifiTip(true);
            jcVideoPlayer.setIsTouchWigetFull(true);
            jcVideoPlayer.setLooping(false);
            ImageView view1 = (ImageView) LayoutInflater.from(context).inflate(R.layout.main_video_fm, container, false);
            GlideUtils.loadImage(context, bannerBean.getUrl() + "?x-oss-process=video/snapshot,t_10000,m_fast", view1);
            jcVideoPlayer.setThumbImageView(view1);
            jcVideoPlayer.setDismissControlTime(2000);
            jcVideoPlayer.setUpLazy(bannerBean.getUrl(), false, null, null, "");
            jcVideoPlayer.startPlayLogic();
            jcVideoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
                @Override
                public void onStartPrepared(String url, Object... objects) {
                    if (bannerListener != null) {
                        bannerListener.videoIsStart();
                    }
                }

                @Override
                public void onPrepared(String url, Object... objects) {

                }

                @Override
                public void onClickStartIcon(String url, Object... objects) {

                }

                @Override
                public void onClickStartError(String url, Object... objects) {

                }

                @Override
                public void onClickStop(String url, Object... objects) {
                    if (bannerListener != null) {
                        bannerListener.videoStop();
                    }
                }

                @Override
                public void onClickStopFullscreen(String url, Object... objects) {

                }

                @Override
                public void onClickResume(String url, Object... objects) {

                }

                @Override
                public void onClickResumeFullscreen(String url, Object... objects) {

                }

                @Override
                public void onClickSeekbar(String url, Object... objects) {

                }

                @Override
                public void onClickSeekbarFullscreen(String url, Object... objects) {

                }

                @Override
                public void onAutoComplete(String url, Object... objects) {
                    if (bannerListener != null) {
                        bannerListener.videoStop();
                    }
                }

                @Override
                public void onComplete(String url, Object... objects) {

                }

                @Override
                public void onEnterFullscreen(String url, Object... objects) {

                }

                @Override
                public void onQuitFullscreen(String url, Object... objects) {

                }

                @Override
                public void onQuitSmallWidget(String url, Object... objects) {

                }

                @Override
                public void onEnterSmallWidget(String url, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekVolume(String url, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekPosition(String url, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekLight(String url, Object... objects) {

                }

                @Override
                public void onPlayError(String url, Object... objects) {

                }

                @Override
                public void onClickStartThumb(String url, Object... objects) {

                }

                @Override
                public void onClickBlank(String url, Object... objects) {
                    if (bannerListener != null) {
                        bannerListener.imgClick(position);
                    }
                }

                @Override
                public void onClickBlankFullscreen(String url, Object... objects) {

                }
            });
            container.addView(view);
            return view;
        }

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    public interface BannerListener {
        void imgClick(int position);

        void videoIsStart();

        void videoStop();
    }

    public BannerListener bannerListener;

    public BannerListener getBannerListener() {
        return bannerListener;
    }

    public void setBannerListener(BannerListener bannerListener) {
        this.bannerListener = bannerListener;
    }

    public void startVideo() {
        if (jcVideoPlayer == null) {
            return;
        }

        jcVideoPlayer.startPlayLogic();
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMuteStatus();
                }
            }, 300);

        } catch (Exception e) {

        }
    }

    private void setMuteStatus() {
        if (jcVideoPlayer == null) {
            return;
        }
        if (ACache.get(context).getAsString("MUTE").equalsIgnoreCase("1")) {
            GSYVideoManager.instance().setNeedMute(true);
            jcVideoPlayer.setMute(true);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0, 0);
            } catch (Exception e) {

            }
        } else {
            jcVideoPlayer.setMute(false);
            GSYVideoManager.instance().setNeedMute(false);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0.5f, 0.5f);
            } catch (Exception e) {

            }
        }
    }
}
