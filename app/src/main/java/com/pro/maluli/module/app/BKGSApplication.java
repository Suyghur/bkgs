package com.pro.maluli.module.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.mob.MobSDK;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;
import com.netease.nim.uikit.business.session.myCustom.extension.SessionHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.module.chatRoom.ChatRoomSessionHelper;
import com.pro.maluli.toolkit.Logger;
import com.tencent.bugly.Bugly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;


public class BKGSApplication extends Application {

    /**
     * MD5:  7A:B1:84:CB:0E:E1:D0:1E:AA:1F:14:27:C1:56:F1:91
     * SHA1: 04:DB:7B:28:32:B4:C0:24:82:8F:19:04:DE:2E:38:20:20:C3:EB:46
     * SHA256: CC:DD:8D:6D:72:33:C6:61:F2:FA:F9:89:4B:7E:AB:F5:D9:1F:69:9F:9E:9B:A0:00:4D:B1:42:95:B6:A6:BF:37
     */
    public static final int UPLOAD_STEP = 100;//多少步上传一次；

    private static BKGSApplication mApplication;
    /**
     * 缓存拍照图片路径
     */
    public File takePhotoCacheDir = null;

    /**
     * 获取Application
     */
    public static BKGSApplication getApp() {
        return mApplication;
    }

    public boolean socketOnline = false;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        Logger.initZap(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        DemoCache.setContext(this);
        baseInit();
        //初始化shareSdk
        MobSDK.submitPolicyGrantResult(true, null);
//        MobSDK.init(this);
//        initLoadImageView();
        initBugly();


    }

    //初始化bug上传
    private void initBugly() {
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        strategy.setAppChannel("bug");//设置渠道
//        Context context = getApplicationContext();
//// 获取当前包名
//        String packageName = context.getPackageName();
//// 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//// 设置是否为上报进程
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        strategy.setAppVersion(PackageUtils.getVersionName(this));//app的版本
//        strategy.setAppPackageName("com.pro.maluli");//包名
//        strategy.setUserInfoActivity(LoginActivity.class);
//        CrashReport.initCrashReport(getApplicationContext(), "dd30375eac", false, strategy);

        Bugly.init(getApplicationContext(), "dd30375eac", false);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
//    private void initLoadImageView() {
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
//                .showImageForEmptyUri(R.mipmap.ic_add_img) //
//                .showImageOnFail(R.mipmap.ic_add_img) //
//                .cacheInMemory(true) //
//                .cacheOnDisk(true) //
//                .build();//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
//                .Builder(getApplicationContext())//
//                .defaultDisplayImageOptions(defaultOptions).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)// 缓存一百张图片
//                .writeDebugLogs()//
//                .build();//
//        ImageLoader.getInstance().init(config);
//    }


    private void baseInit() {
        Glide.with(this);
        SDKOptions sdkOptions = NimSDKOptionConfig.getSDKOptions(this);
        NIMClient.init(this, getLoginInfo(), sdkOptions);
        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            NimUIKit.init(this);
            JPushInterface.init(this);
            // 初始化消息提醒
//            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            NIMClient.toggleNotification(true);
            // IM 会话窗口的定制初始化。
            SessionHelper.init();
            // 聊天室聊天窗口的定制初始化。
            ChatRoomSessionHelper.init();
        }
    }


    /**
     * Handling Configuration Changes
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // 根据authType提供完整信息
    private LoginInfo getLoginInfo() {
        LoginInfo loginInfo = Preferences.getLoginInfo();
        if (!TextUtils.isEmpty(loginInfo.getAccount())) {
            DemoCache.setAccount(loginInfo.getAccount().toLowerCase());
        }
        return loginInfo;
    }


}
