package com.pro.maluli.module.other.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.WelcomInfoEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.CustomVideoView;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.other.welcome.presenter.IWelcomContraction;
import com.pro.maluli.module.other.welcome.presenter.WelcomPresenter;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class WelcomAct extends BaseMvpActivity<IWelcomContraction.View, WelcomPresenter> implements View.OnClickListener, IWelcomContraction.View {
    @BindView(R.id.videoview)
    CustomVideoView videoview;
    @BindView(R.id.startIv)
    ImageView startIv;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.checkDetailTv)
    TextView checkDetailTv;
    @BindView(R.id.splash_layout)
    RelativeLayout splash_layout;
    private static int TIME = 5;
    private String linkUrl;
    /**
     * 获取Looper并传递
     */
    MyHandler handler = new MyHandler(Looper.myLooper(), this);

    @Override
    public void setWelcomInfo(WelcomInfoEntity welcomInfo) {
        //图片
        if (welcomInfo.getFile_type() == 1) {
            startIv.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            GlideUtils.loadImageNoImage(WelcomAct.this, welcomInfo.getUrl(), startIv);
            TIME = welcomInfo.getStop_time();
            Message message = handler.obtainMessage(1);
            handler.sendMessageDelayed(message, 1000);
        } else if (welcomInfo.getFile_type() == 2) {
            startIv.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            mTime.setText("跳过");
            //视频
            //设置播放加载路径
            videoview.setVideoURI(Uri.parse(welcomInfo.getUrl()));
            //播放
            videoview.start();
            //循环播放
            videoview.setOnCompletionListener(mediaPlayer -> {
                videoview.suspend();//释放所有配置信息和内存.
                gotoActivity(MainActivity.class, true);
            });
        } else {
            gotoActivity(MainActivity.class, true);
        }
        linkUrl = welcomInfo.getLink();
    }


    /**
     * 新的handler类要声明成静态类
     */
    static class MyHandler extends Handler {
        WeakReference<WelcomAct> mactivity;

        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, WelcomAct activity) {
            super(looper);//调用父类的显式指明的构造函数
            mactivity = new WeakReference<WelcomAct>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            WelcomAct nactivity = mactivity.get();
            if (nactivity == null) {
                return;//avtivity都没了还处理个XXX
            }

            switch (msg.what) {
                case 1:
                    TIME--;
                    nactivity.mTime.setText("跳过" + TIME + "s");
                    if (TIME > 0) {
                        Message message = nactivity.handler.obtainMessage(1);
                        nactivity.handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        //跳转到主界面
                        nactivity.gotoActivity(MainActivity.class, true);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public WelcomPresenter initPresenter() {
        return new WelcomPresenter(this);
    }

    @Override
    public void baseInitialization() {
//        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
//        BarUtils.setStatusBarLightMode(this, true);
//        StatusbarUtils.setStatusBarView(this);
        BarUtils.setStatusBarVisibility(this, false);
        Window window = getWindow();
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_welcom;
    }

    @Override
    public void viewInitialization() {

    }

    @OnClick({R.id.mTime, R.id.checkDetailTv})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mTime:
                handler.removeCallbacksAndMessages(null);
                gotoActivity(MainActivity.class, true);

                break;
            case R.id.checkDetailTv:
                if (AntiShake.check(view.getId())) {
                    return;
                }
                if (TextUtils.isEmpty(linkUrl)) {
                    ToastUtils.showShort("链接错误，无法跳转");
                    return;
                }
                try {
                    //方式一：代码实现跳转
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(linkUrl);//此处填链接
                    intent.setData(content_url);
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtils.showShort("链接错误，无法跳转");
                }

                break;
        }
    }

    @Override
    public void doBusiness() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getWelcomInfo();
            }
        },1000);


//        startAnimation();
    }

    private void startAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        splash_layout.setAnimation(alphaAnimation);
        alphaAnimation.setDuration(1000);
        alphaAnimation.start();
    }

    @Override
    public void onClick(View v) {

    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        if (videoview != null) {
            videoview.stopPlayback();
        }
        super.onStop();
    }
}