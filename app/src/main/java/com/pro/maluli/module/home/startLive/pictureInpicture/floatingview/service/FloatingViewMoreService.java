package com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.netease.neliveplayer.playerkit.sdk.PlayerManager;
import com.netease.neliveplayer.playerkit.sdk.VodPlayer;
import com.netease.neliveplayer.playerkit.sdk.model.SDKOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoBufferStrategy;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;
import com.netease.neliveplayer.playerkit.sdk.view.AdvanceTextureView;
import com.netease.neliveplayer.proxy.config.NEPlayerConfig;
import com.pro.maluli.R;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.StartOneToMoreLiveAct;
import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.FloatingView;
import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.FloatingViewManager;


/**
 * @author zwl
 * @describe 悬浮窗Service
 * @date on 2018/11/26
 */
public class FloatingViewMoreService extends Service implements FloatingView.IFloatingViewCallback {

    protected VodPlayer player;
    private FloatingViewManager mFloatingViewManager;
    private AdvanceTextureView textureView;
    /**
     * 拉流播放器
     */
    private SDKOptions config;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

        Logger.d("悬浮窗Service已启动");
        View floatView = LayoutInflater.from(this).inflate(R.layout.floating_more, null, false);
        textureView = (AdvanceTextureView) floatView.findViewById(R.id.textureView);
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "点击了悬浮窗", Toast.LENGTH_SHORT).show();
                //这样启动activity是解决启动延迟的问题
                Intent intent = new Intent(FloatingViewMoreService.this, StartOneToMoreLiveAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        this.mFloatingViewManager = new FloatingViewManager(this, this);

        FloatingViewManager.Configs configs = new FloatingViewManager.Configs();
        configs.floatingViewX = dm.widthPixels / 2;
        configs.floatingViewY = dm.heightPixels / 4;
        configs.overMargin = -(int) (8 * dm.density);
        configs.animateInitialMove = false;
        this.mFloatingViewManager.addFloatingView(floatView, configs);

//        return START_REDELIVER_INTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        Logger.d("onDestroy");

        destroyFloatingView();

        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    //初始化拉流播放器

    private void initPlaye2r(String rtmp_pull_url) {
        config = new SDKOptions();
        config.privateConfig = new NEPlayerConfig();
        PlayerManager.init(this, config);
        VideoOptions options = new VideoOptions();
        options.hardwareDecode = false;
        options.isPlayLongTimeBackground = true;
        options.bufferStrategy = VideoBufferStrategy.ANTI_JITTER;
        player = PlayerManager.buildVodPlayer(this, rtmp_pull_url, options);
        //NONE：无缩放(原始大小)
        //FIT：等比例缩放至长边充满容器，短边可能填充黑边
        //FILL：拉伸至充满容器，画面可能会变形
        //FULL：等比例缩放至短边充满容器，长边可能会被裁剪
        player.setVideoScaleMode(VideoScaleMode.FIT);
        start();
        if (textureView != null) {
            textureView.setVisibility(View.VISIBLE);
            player.setupRenderView(textureView, VideoScaleMode.FIT);
        }
    }

    private void start() {
//        player.registerPlayerObserver(playerObserver, true);
        player.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }

    /**
     * 销毁悬浮窗
     */
    private void destroyFloatingView() {
        if (player == null) {
            return;
        }
//        player.registerPlayerObserver(playerObserver, false);
        player.setupRenderView(null, VideoScaleMode.NONE);
        textureView.releaseSurface();
//        textureView = null;
        player.stop();
        player = null;
        if (this.mFloatingViewManager != null) {
            this.mFloatingViewManager.removeAllFloatingView();
            this.mFloatingViewManager = null;
        }

        Logger.d("悬浮窗已销毁");
    }

//    public void setCallback(CallBack callback) {
//        this.callback = callback;
//    }
//
//    public static interface CallBack {
//        void onDataChanged(String data);
//    }

    public class MyBinder extends Binder {
        public FloatingViewMoreService getService() {
            return FloatingViewMoreService.this;
        }


        public void setData(String url) {//写一个公共方法，用来对data数据赋值。
            initPlaye2r(url);
        }
    }
}
