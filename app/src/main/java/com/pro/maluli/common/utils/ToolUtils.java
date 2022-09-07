package com.pro.maluli.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.app.BKGSApplication;
import com.pro.maluli.module.other.login.LoginAct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ToolUtils {
    /**
     * 防止多次点击
     */
    // 两次点击按钮接口之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    public static int WRITE_PERMISSION_REQ_CODE = 100;
    private static long lastClickTime;

    public static String getMacAddress() {

        String macAddress = null;
        WifiManager wifiManager =
                (WifiManager) BKGSApplication.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());

        if (!wifiManager.isWifiEnabled()) {
            //必须先打开，才能获取到MAC地址
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        }
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 判断Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service 类全名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            String className = info.service.getClassName();
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    //获取内容区域
    public static int getContextRect(Activity activity) {
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }

    public static boolean isLogin(Context context, boolean isGoLogin) {
        return !TextUtils.isEmpty(AcacheUtil.getToken(context, isGoLogin));
    }

    public static boolean isLoginTips(Context context, FragmentManager manager) {
        if (!TextUtils.isEmpty(AcacheUtil.getToken(context, false))) {

            return true;
        }
        BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
        Bundle bundle2 = new Bundle();
        bundle2.putString("showContent", "您未登录");
        bundle2.putString("TITLE_DIO", "温馨提示");
        bundle2.putString("comfirm", "去登录");
        bundle2.putString("cancel", "取消");
        baseTipsDialog1.setArguments(bundle2);
        baseTipsDialog1.show(manager, "BaseTipsDialog");
        baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
            @Override
            public void comfirm() {
                Intent intent = new Intent(context, LoginAct.class);
                context.startActivity(intent);
            }
        });
        return false;
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();

            PackageInfo p1 = pm.getPackageInfo(context.getPackageName(), 0);

            versionName = p1.versionName;

            if (TextUtils.isEmpty(versionName) || versionName.length() <= 0) {
                return "";
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //在你需要登陆的时候调用此方法既可
    public static void loginQQ(Handler handler, Activity context) {
        //qq登陆传值QQ.NAME
        final Platform weChat = ShareSDK.getPlatform(QQ.NAME);
        ShareSDK.setActivity(context);//抖音登录适配安卓9.0
        if (weChat.isAuthValid()) {
            weChat.removeAccount(true);
        }
        weChat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                Logger.d("onComplete: ");
                if (platform.getDb().exportData() != null) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = platform.getDb().exportData();
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Logger.d("onError: ");
                String asda = platform.getDb().exportData();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Logger.d("onCancel: ");
            }
        });
        weChat.SSOSetting(false);
        weChat.showUser(null);
    }

    //在你需要登陆的时候调用此方法既可
    public static void loginWeChat(Handler handler, Activity context) {
        //qq登陆传值QQ.NAME
        Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
        ShareSDK.setActivity(context);//抖音登录适配安卓9.0
        if (weChat.isAuthValid()) {
            weChat.removeAccount(true);
        }
        weChat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                if (platform.getDb().exportData() != null) {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = platform.getDb().exportData();
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Logger.e("onError: " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Logger.d("onCancel: ");
            }
        });
        weChat.SSOSetting(false);
        weChat.showUser(null);
    }

    /**
     * 分享图片
     */
    public static void shareImg(Handler handler, String type, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImageUrl(imgUrl);
        sp.setTitle("百科高手");
        sp.setTitleUrl(imgUrl); // 标题的超链接
//        sp.setText("测试分享的文本");

//        sp.setSite("发布分享的网站名称");
//        sp.setSiteUrl("发布分享网站的地址");
        Platform platform = ShareSDK.getPlatform(type);
        Logger.e("type: " + type);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable throwable) {
                Logger.e("onError: " + throwable.getMessage());
                // 失败的回调，arg:平台对象，arg1:表示当前的动作(9表示分享)，arg2:异常信息
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                // 分享成功的回调
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            public void onCancel(Platform arg0, int arg1) {
                // 取消分享的回调
            }
        });
// 执行图文分享
        platform.share(sp);
    }

    /**
     * 分享网页
     */
    public static void shareURL(Handler handler, String type, String imgurl, String title, String avatar) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText("快来看看我的主页吧");
        sp.setImageUrl(avatar);
        sp.setUrl(imgurl);
        sp.setTitle(title);
        sp.setTitleUrl(imgurl);
//        sp.setSite("发布分享的网站名称");
//        sp.setSiteUrl("发布分享网站的地址");
        Platform platform = ShareSDK.getPlatform(type);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable throwable) {
                // 失败的回调，arg:平台对象，arg1:表示当前的动作(9表示分享)，arg2:异常信息
                Logger.e("onError: " + throwable.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                // 分享成功的回调
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            public void onCancel(Platform arg0, int arg1) {
                // 取消分享的回调
            }
        });
// 执行图文分享
        platform.share(sp);
    }

    public static void shareVideoUrl(Handler handler, String type, String url, String desc, String imgUrl) {
        Platform platform = ShareSDK.getPlatform(type);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(desc);
        sp.setImageUrl(imgUrl);
        sp.setUrl(url);
        sp.setTitle("百科高手");
        sp.setTitleUrl(url);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform p, int code, Throwable throwable) {
                // 失败的回调，arg:平台对象，arg1:表示当前的动作(9表示分享)，arg2:异常信息
                Logger.e("onError: " + throwable.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            public void onComplete(Platform platform, int code, HashMap arg2) {
                // 分享成功的回调
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            public void onCancel(Platform arg0, int arg1) {
                // 取消分享的回调
            }
        });
        // 执行图文分享
        platform.share(sp);
    }

    /**
     * 分享视频
     */
    public static void shareVideo(Handler handler, String type, String videoUrl, String text, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("百科高手");
        sp.setText(text);
        sp.setTitleUrl(videoUrl);
        sp.setImageUrl(imgUrl);
        sp.setShareType(Platform.SHARE_VIDEO);
        sp.setUrl(videoUrl);
//        sp.setSiteUrl(videoUrl);
        Platform platform = ShareSDK.getPlatform(type);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable throwable) {
                // 失败的回调，arg:平台对象，arg1:表示当前的动作(9表示分享)，arg2:异常信息
                Logger.e("onError: " + throwable.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                // 分享成功的回调
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            public void onCancel(Platform arg0, int arg1) {
                // 取消分享的回调
            }
        });
// 执行图文分享
        platform.share(sp);
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 是否安装微信
     */
    public static boolean isWeixinAvilible(Context context) {

        boolean bHas = true;
        try {
            context.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_GIDS);
        } catch (PackageManager.NameNotFoundException e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            bHas = false;
        }
        return bHas;
    }

    public static boolean checkPublishPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions((Activity) context, (String[]) permissions.toArray(new String[0]),
                        WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * 手机号用****号隐藏中间数字
     */
    public static String settingphone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }


    /**
     * 判断当前系统时间是否在指定时间的范围内
     * <p>
     * beginHour 开始小时,例如22
     * beginMin  开始小时的分钟数,例如30
     * endHour   结束小时,例如 8
     * endMin    结束小时的分钟数,例如0
     * true表示在范围内, 否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        // 跨天的特殊情况(比如22:00-8:00)
        if (!startTime.before(endTime)) {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            //普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }
}
