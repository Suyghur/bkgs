package com.pro.maluli.module.home.startLive.pictureInpicture.floatingview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬浮窗管理器
 *
 * @author PengZhenjin
 * @date 2017-6-5
 */
public class FloatingViewManager {
    /**
     * WindowManager
     */
    private final WindowManager mWindowManager;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 悬浮窗监听器
     */
    private FloatingView.IFloatingViewCallback callback;
    /**
     * 悬浮窗集合
     */
    private List<FloatingView> mFloatingViewList;

    /**
     * 构造方法
     */
    public FloatingViewManager(Context context, FloatingView.IFloatingViewCallback callback) {
        this.mContext = context;
        this.callback = callback;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mFloatingViewList = new ArrayList<>();
    }

    /**
     * 添加悬浮窗
     *
     * @param view    悬浮窗视图组件
     * @param configs 悬浮窗的配置信息
     */
    public void addFloatingView(View view, Configs configs) {
        // 创建悬浮窗
        FloatingView floatingView = new FloatingView(this.mContext, configs.floatingViewX, configs.floatingViewY);
        floatingView.setOverMargin(configs.overMargin);
        floatingView.setMoveDirection(configs.moveDirection);
        floatingView.setAnimateInitialMove(configs.animateInitialMove);
        // 设置悬浮窗的大小
        FrameLayout.LayoutParams targetParams = new FrameLayout.LayoutParams(configs.floatingViewWidth, configs.floatingViewHeight);
        view.setLayoutParams(targetParams);
        floatingView.addView(view);
        WindowManager.LayoutParams windowManager = floatingView.getWindowLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowManager.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowManager.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 添加悬浮窗到集合
        this.mFloatingViewList.add(floatingView);

        // 添加悬浮窗
        this.mWindowManager.addView(floatingView, windowManager);
    }

    /**
     * 移除悬浮窗
     *
     * @param floatingView
     */
    private void removeFloatingView(FloatingView floatingView) {
        int matchIndex = this.mFloatingViewList.indexOf(floatingView);
        if (matchIndex != -1) {
            this.mWindowManager.removeViewImmediate(floatingView);
            this.mFloatingViewList.remove(matchIndex);
        }
        if (this.mFloatingViewList.isEmpty()) {
            if (this.callback != null) {
                this.callback.onFinishFloatingView();
            }
        }
    }

    /**
     * 移除所有的悬浮窗
     */
    public void removeAllFloatingView() {
        if (this.mFloatingViewList != null) {
            for (FloatingView floatingView : mFloatingViewList) {
                this.mWindowManager.removeViewImmediate(floatingView);
            }
            this.mFloatingViewList.clear();
        }
    }

    /**
     * 悬浮窗的配置信息
     */
    public static class Configs {
        /**
         * 悬浮窗的x坐标
         */
        public int floatingViewX;

        /**
         * 悬浮窗的y坐标
         */
        public int floatingViewY;

        /**
         * 悬浮窗的宽度（单位：px）
         */
        public int floatingViewWidth;

        /**
         * 悬浮窗的高度（单位：px）
         */
        public int floatingViewHeight;

        /**
         * 悬浮窗边缘的外边距
         */
        public int overMargin;

        /**
         * 悬浮窗移动方向
         */
        @FloatingView.MoveDirection
        public int moveDirection;

        /**
         * 悬浮窗移动时是否带动画
         */
        public boolean animateInitialMove;

        public Configs() {
            this.floatingViewX = FloatingView.DEFAULT_X;
            this.floatingViewY = FloatingView.DEFAULT_Y;
            this.floatingViewWidth = FloatingView.DEFAULT_WIDTH;
            this.floatingViewHeight = FloatingView.DEFAULT_HEIGHT;
            this.overMargin = 0;
            this.moveDirection = FloatingView.MOVE_DIRECTION_DEFAULT;
            this.animateInitialMove = true;
        }
    }
}
