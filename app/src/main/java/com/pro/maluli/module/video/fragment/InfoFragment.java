package com.pro.maluli.module.video.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.video.events.CanStartEvent;
import com.pro.maluli.module.video.events.ClearPositionEvent;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfoFragment extends Fragment {
    private View infoMainView;
    private StandardGSYVideoPlayer videoPlayer;
    private ImageView adverImg;
    private RoundedImageView detailRiv;
    private LinearLayout checkDetailLL;
    private TextView adverNameTv, videoContentTv;
    private int mCurrentPosition;

    private VideoEntity.AdvertBean advertBean;
    private boolean isCanStart;

    public static InfoFragment getNewInstance(@NotNull VideoEntity.AdvertBean advertBean) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ADVERT", advertBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mCurrentPosition = 0;
        advertBean = (VideoEntity.AdvertBean) getArguments().getSerializable("ADVERT");
    }

    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (infoMainView == null) {
            infoMainView = inflater.inflate(R.layout.frag_advert_info, container, false);
        }
        videoPlayer = infoMainView.findViewById(R.id.videoPlayer);
        adverImg = infoMainView.findViewById(R.id.adverImg);
        detailRiv = infoMainView.findViewById(R.id.detailRiv);
        adverNameTv = infoMainView.findViewById(R.id.adverNameTv);
        videoContentTv = infoMainView.findViewById(R.id.videoContentTv);
        checkDetailLL = infoMainView.findViewById(R.id.checkDetailLL);

        GlideUtils.loadImageNoImage(getActivity(), advertBean.getIcon(), detailRiv);
        adverNameTv.setText(advertBean.getTitle());

        videoContentTv.setMovementMethod(new ScrollingMovementMethod());//设置textview可以滑动
        videoContentTv.setScrollbarFadingEnabled(false);//设置scrollbar一直显示
        videoContentTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                videoContentTv.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        videoContentTv.setText(advertBean.getContent());

        checkDetailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AntiShake.check(v.getId())) {
                    return;
                }
                //方式一：代码实现跳转
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(advertBean.getLink());//此处填链接
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        if (advertBean != null && advertBean.getFile_type() == 1) {//1图片，2视屏
            videoPlayer.setVisibility(View.GONE);
            adverImg.setVisibility(View.VISIBLE);
            GlideUtils.loadImageNoImage(getActivity(), advertBean.getUrl(), adverImg);
        } else {
            videoPlayer.setVisibility(View.VISIBLE);
            adverImg.setVisibility(View.GONE);
            videoPlayer.release();
            videoPlayer.onVideoReset();

            videoPlayer.getBackButton().setVisibility(View.GONE);
            videoPlayer.getTitleTextView().setVisibility(View.GONE);
            videoPlayer.getFullscreenButton().setVisibility(View.GONE);
            videoPlayer.setNeedShowWifiTip(false);
            videoPlayer.setLooping(true);
            videoPlayer.setDismissControlTime(3000);
            videoPlayer.setIsTouchWiget(true);
            videoPlayer.setThumbPlay(true);
//            videoPlayer.setThumbImageView(imageView);
            videoPlayer.setUpLazy(advertBean.getUrl(), false, null, null, "");

        }

        return infoMainView;
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClearPosition(ClearPositionEvent clearPositionEvent) {
        if (clearPositionEvent.isClear()) {
            mCurrentPosition = 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClearPosition(CanStartEvent canStartEvent) {
        isCanStart = canStartEvent.isCanStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCanStart) {
            return;
        }
        if (advertBean.getFile_type() == 1) {
            return;
        }
        GSYVideoManager.releaseAllVideos();
        GSYVideoManager.instance().setNeedMute(false);
        if (mCurrentPosition > 0) {
            videoPlayer.onVideoResume(false);
        } else {
            videoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoPlayer.startPlayLogic();
                }
            }, 200);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        videoPlayer.onVideoPause();
        if (advertBean.getFile_type() == 1) {
            return;
        }
        GSYVideoManager.onPause();
        mCurrentPosition = (int) videoPlayer.getGSYVideoManager().getCurrentPosition();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
