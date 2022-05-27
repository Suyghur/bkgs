package com.pro.maluli.module.home.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import java.util.ArrayList;
import java.util.List;


public class BannerViewAdapter extends PagerAdapter {

    public BannerListener bannerListener;
    CoustormGsyVideoPlayer jcVideoPlayer;
    private Context context;
    private List<HomeInfoEntity.BannerBean> listBean;
    private String url;
    private boolean isFristVideo;
    private boolean isMute;

    public BannerViewAdapter(Activity context, List<HomeInfoEntity.BannerBean> list) {
//        this.context = context.getApplicationContext();
        this.context = context;
        if (list == null || list.size() == 0) {
            this.listBean = new ArrayList<>();
        } else {
            this.listBean = list;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        HomeInfoEntity.BannerBean bannerBean = listBean.get(position);
        if (bannerBean.getFile_type() == 1) {//ͼƬ

            View view = LayoutInflater.from(context).inflate(R.layout.main_top_img, container, false);
            RoundedImageView mainTopRiv = view.findViewById(R.id.mainTopRiv);
            mainTopRiv.setOnClickListener(new View.OnClickListener() {
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
            Glide.with(context).load(bannerBean.getUrl()).into(mainTopRiv);
            container.addView(mainTopRiv);
            return mainTopRiv;
        } else {//
            View view = LayoutInflater.from(context).inflate(R.layout.main_top_video, container, false);
            jcVideoPlayer = view.findViewById(R.id.jcVideoPlayer);
            jcVideoPlayer.onVideoPause();
            jcVideoPlayer.onVideoReset();
            int adf = jcVideoPlayer.getAudioManger().getStreamVolume(AudioManager.STREAM_MUSIC);
            if (adf == 0) {
                jcVideoPlayer.setMute(true);
            } else {
                jcVideoPlayer.setMute(false);

            }

//            StandardGSYVideoPlayer jcVideoPlayer = new StandardGSYVideoPlayer(context);
            jcVideoPlayer.getBackButton().setVisibility(View.GONE);
            jcVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            jcVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
//            jcVideoPlayer.getStartButton().setVisibility(View.GONE);
            jcVideoPlayer.setNeedShowWifiTip(true);
//            jcVideoPlayer.setIsTouchWigetFull(true);
//            jcVideoPlayer.setIsTouchWiget(false);
//            jcVideoPlayer.setThumbPlay(true);
            jcVideoPlayer.setLooping(false);
            ImageView view1 = (ImageView) LayoutInflater.from(context).inflate(R.layout.main_video_fm, container, false);
            GlideUtils.loadImage(context, bannerBean.getUrl() + "?x-oss-process=video/snapshot,t_10000,m_fast", view1);
            jcVideoPlayer.setThumbImageView(view1);
            jcVideoPlayer.setDismissControlTime(2000);
            jcVideoPlayer.setUpLazy(bannerBean.getUrl(), false, null, null, "");
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

                }

                @Override
                public void onClickBlankFullscreen(String url, Object... objects) {

                }
            });
            container.addView(view);
            return view;
        }

    }

    public void startVideo(int position) {
        if (jcVideoPlayer == null) {
            return;
        }
//        jcVideoPlayer.setUpLazy(listBean.get(position).getUrl(), false, null, null, "");
        jcVideoPlayer.startPlayLogic();
    }

    private void silentSwitchOn() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.d("Silent:", "RINGING 已被静音");
        }
    }

    private void silentSwitchOff() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.d("SilentListenerService", "RINGING 取消静音");
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

    public BannerListener getBannerListener() {
        return bannerListener;
    }

    public void setBannerListener(BannerListener bannerListener) {
        this.bannerListener = bannerListener;
    }

    public interface BannerListener {
        void imgClick(int position);

        void videoIsStart();

        void videoStop();
    }
}
