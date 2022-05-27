package com.pro.maluli.module.home.base.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ACache;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class CoustormGsyVideoPlayer extends StandardGSYVideoPlayer {
    ImageView jiugnying;

    public CoustormGsyVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        initMy();
    }


    public CoustormGsyVideoPlayer(Context context) {
        super(context);
        initMy();
    }

    public CoustormGsyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMy();
    }


    @Override
    public int getLayoutId() {
        return R.layout.video_main_top;
    }


    private void initMy() {
        boolean isMute = GSYVideoManager.instance().isNeedMute();
        jiugnying = findViewById(R.id.jiugnying);
        jiugnying.setSelected(isMute);
        jiugnying.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMute = GSYVideoManager.instance().isNeedMute();
                if (isMute) {
                    silentSwitchOff();
                    jiugnying.setSelected(false);
                } else {
                    jiugnying.setSelected(true);
                    silentSwitchOn();
                }
            }
        });
    }

    public AudioManager getAudioManger() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager;
    }

    private void silentSwitchOn() {
        GSYVideoManager.instance().setNeedMute(true);
        ACache.get(mContext).put("MUTE", "1");
//        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//            Log.d("Silent:", "RINGING 已被静音");
    }

    private void silentSwitchOff() {
        GSYVideoManager.instance().setNeedMute(false);
        ACache.get(mContext).put("MUTE", "0");
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
//            Log.d("SilentListenerService", "RINGING 取消静音");
    }

    public void setMute(boolean isMute) {
        jiugnying.setSelected(isMute);
        if (isMute) {
            ACache.get(mContext).put("MUTE", "1");
        } else {

            ACache.get(mContext).put("MUTE", "0");
        }
    }
}