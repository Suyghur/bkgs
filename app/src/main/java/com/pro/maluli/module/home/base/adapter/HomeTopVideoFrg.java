package com.pro.maluli.module.home.base.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.ktx.utils.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;

public class HomeTopVideoFrg extends Fragment {
    public BannerListener bannerListener;
    protected boolean isLazyLoaded = false;
    HomeInfoEntity.BannerBean bannerBean;
    CoustormGsyVideoPlayer jcVideoPlayer;
    View mainView;

    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isPrepared = false;

    public static Fragment newInstance(HomeInfoEntity.BannerBean listBean) {
        HomeTopVideoFrg treasureGameFrag = new HomeTopVideoFrg();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bannerTopVideo", listBean);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    public BannerListener getBannerListener() {
        return bannerListener;
    }

    public void setBannerListener(BannerListener bannerListener) {
        this.bannerListener = bannerListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
        baseInitialization();
    }

    public void baseInitialization() {
        bannerBean = (HomeInfoEntity.BannerBean) getArguments().getSerializable("bannerTopVideo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (!isVideo()) {
            if (mainView == null) {
                mainView = LayoutInflater.from(getActivity()).inflate(R.layout.main_top_img, container, false);
                RoundedImageView mainTopRiv = mainView.findViewById(R.id.mainTopRiv);
                mainTopRiv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AntiShake.check(view.getId())) {
                            return;
                        }
                        //方式一：代码实现跳转
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(bannerBean.getLink());//此处填链接
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                });
                Glide.with(requireActivity()).load(bannerBean.getUrl()).into(mainTopRiv);
            }
        } else {//
            if (mainView == null) {
                mainView = LayoutInflater.from(getActivity()).inflate(R.layout.main_top_video, container, false);
                jcVideoPlayer = mainView.findViewById(R.id.jcVideoPlayer);
                if (ACache.get(getActivity()).getAsString("MUTE").equalsIgnoreCase("1")) {
                    GSYVideoManager.instance().setNeedMute(true);
                    jcVideoPlayer.setMute(true);
                } else {
                    jcVideoPlayer.setMute(false);
                    GSYVideoManager.instance().setNeedMute(false);
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
                ImageView view1 = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.main_video_fm, container, false);
                GlideUtils.loadImage(getActivity(), bannerBean.getUrl() + "?x-oss-process=video/snapshot,t_10000,m_fast", view1);
                jcVideoPlayer.setThumbImageView(view1);
                jcVideoPlayer.setDismissControlTime(2000);
                jcVideoPlayer.setUpLazy(bannerBean.getUrl(), false, null, null, "");
                jcVideoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
                    @Override
                    public void onStartPrepared(String url, Object... objects) {

                    }

                    @Override
                    public void onPrepared(String url, Object... objects) {
                        if (bannerListener != null) {
                            bannerListener.videoIsStart();
                        }
                        if ("1".equalsIgnoreCase(ACache.get(getActivity()).getAsString("MUTE"))) {
                            GSYVideoManager.instance().setNeedMute(true);
                            jcVideoPlayer.setMute(true);
                        } else {
                            jcVideoPlayer.setMute(false);
                            GSYVideoManager.instance().setNeedMute(false);
                        }
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
                        //方式一：代码实现跳转
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(bannerBean.getLink());//此处填链接
                        intent.setData(content_url);
                        startActivity(intent);

                    }

                    @Override
                    public void onClickBlankFullscreen(String url, Object... objects) {

                    }
                });
                lazyLoad();
            }
        }
        return mainView;
    }

    public boolean isVideo() {
        return bannerBean == null || bannerBean.getFile_type() != 1;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    /**
     * 调用懒加载
     * 第三步:在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
     */
    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared) {
            startVideo();
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setMuteStatus();
                    }
                }, 300);

            } catch (Exception e) {
                e.printStackTrace();
            }
            isLazyLoaded = true;
        } else {
            //当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
            if (isLazyLoaded) {
                if (isVideo()) {
                    GSYVideoManager.releaseAllVideos();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isVideo()) {
            GSYVideoManager.releaseAllVideos();
        }
    }

    @Override
    public void onDestroy() {
        isLazyLoaded = false;
        isPrepared = false;
        if (jcVideoPlayer != null) {
            jcVideoPlayer.getCurrentPlayer().setVideoAllCallBack(null);
            jcVideoPlayer.release();
        }
        super.onDestroy();
    }

    public void startVideo() {
        if (jcVideoPlayer != null) {
            if (jcVideoPlayer.getGSYVideoManager().isPlaying()) {
                return;
            }
            Logger.e("startPlayLogic");
            GSYVideoManager.releaseAllVideos();
            jcVideoPlayer.setUpLazy(bannerBean.getUrl(), false, null, null, "");
            jcVideoPlayer.startPlayLogic();
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMuteStatus();
                }
            }, 300);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMuteStatus() {
        if (jcVideoPlayer == null || PlayerFactory.getPlayManager().getMediaPlayer() == null) {
            return;
        }
        if (ACache.get(getActivity()).getAsString("MUTE").equalsIgnoreCase("1")) {
            GSYVideoManager.instance().setNeedMute(true);
            jcVideoPlayer.setMute(true);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            jcVideoPlayer.setMute(false);
            GSYVideoManager.instance().setNeedMute(false);
            try {
                PlayerFactory.getPlayManager().getMediaPlayer().setVolume(0.5f, 0.5f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface BannerListener {
        void imgClick(int position);

        void videoIsStart();

        void videoStop();
    }
}