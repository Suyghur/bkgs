package com.pro.maluli.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class StatusbarUtils {
    /**
     * 添加View到状态栏，在沉浸式状态下不侵入状态栏
     */
    public static void setStatusBarView(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
// 生成一个状态栏大小的矩形
            View StatusView = createStatusView(activity);
// 添加statusView到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(StatusView);
// 设置根布局的参数
            ViewGroup rootView = (ViewGroup) (((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
            rootView.setFitsSystemWindows(true);
        }
    }

    private static View createStatusView(Activity activity) {
        int statusBarHeight = getStatusBarHeight(activity);
        View view = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }
    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
