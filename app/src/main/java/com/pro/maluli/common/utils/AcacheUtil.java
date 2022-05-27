package com.pro.maluli.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.preferences.Preferences;
import com.pro.maluli.module.other.login.LoginAct;

import java.text.SimpleDateFormat;

public class AcacheUtil {

    /**
     * 保存登录令牌
     *
     * @param context
     * @param token
     */
    public static boolean saveToken(Context context, String token) {
        if (token == null) {
            return false;
        }
        try {
            ACache.get(context).put(ACEConstant.USER_TOKEN, token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 保存青少年模式
     *
     * @param context
     */
    public static boolean saveTeenager(Context context) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = TimeUtils.getNowString(format);
            ACache.get(context).put(ACEConstant.TEENAGER, time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param context
     * @return false 需要弹框
     */
    public static boolean isShowTeenager(Context context) {
        try {
            String time = ACache.get(context).getAsString(ACEConstant.TEENAGER);
            if (TextUtils.isEmpty(time)) {
                return false;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return TimeUtils.isToday(time, format);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取登录令牌
     *
     * @param context
     * @param flag
     */
    public static String getToken(Context context, boolean flag) {
        String fToken = (String) ACache.get(context).getAsString(ACEConstant.USER_TOKEN);
        if (TextUtils.isEmpty(fToken)) {
            if (flag) {
                context.startActivity(new Intent(context, LoginAct.class));
            }
        }
        return fToken;
    }

    /**
     * 清除数据
     *
     * @param context
     */
    public static boolean loginOut(Context context) {
        try {
            NIMClient.getService(AuthService.class).logout();
            NimUIKit.logout();
            ACache.get(context).remove(ACEConstant.USER_TOKEN);
            ACache.get(context).remove(ACEConstant.USERINFO);
            Preferences.clearUserToken();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
