//package com.pro.maluli.module.home.startLive.windowView;
//
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.netease.lava.nertc.sdk.video.NERtcVideoView;
//import com.pro.maluli.R;
//import com.pro.maluli.module.home.startLive.StartLiveAct;
//import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.FloatingViewManager;
//import com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service.FloatingViewService;
//
//public class WindowUtils {
//    private static final String TAG = "FloatingViewService";
//
//    private FloatingViewManager mFloatingViewManager;
//
//    private NERtcVideoView vv_local_user;
//    private Long uid;
//    public void initLive(final Context context) {
//
//        View floatView = LayoutInflater.from(context).inflate(R.layout.call_float_view, null, false);
//        vv_local_user = (NERtcVideoView) floatView.findViewById(R.id.vv_local_user);
//        floatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //这样启动activity是解决启动延迟的问题
//                Intent intent = new Intent(context, StartLiveAct.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                try {
//                    pendingIntent.send();
//                } catch (PendingIntent.CanceledException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        windowManager.getDefaultDisplay().getMetrics(dm);
//
//        this.mFloatingViewManager = new FloatingViewManager(context, this);
//
//        FloatingViewManager.Configs configs = new FloatingViewManager.Configs();
//        configs.floatingViewX = dm.widthPixels / 2;
//        configs.floatingViewY = dm.heightPixels / 4;
//        configs.overMargin = -(int) (8 * dm.density);
//        configs.animateInitialMove = false;
//        this.mFloatingViewManager.addFloatingView(floatView, configs);
//
//    }
//}