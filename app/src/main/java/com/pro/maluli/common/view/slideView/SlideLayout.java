package com.pro.maluli.common.view.slideView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import java.lang.reflect.Field;

/**
 * name: SlideLayout
 * desc: 侧滑菜单
 * author:
 * date: 2019-09-04 15:10
 * remark:
 */
public class SlideLayout extends FrameLayout implements View.OnTouchListener {

    /**
     * 表示当前滑动闲置
     */
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;
    private static final String TAG = SlideLayout.class.getName();
    /**
     * 滑动速率，每秒dip
     */
    private static final int SLIDING_VELOCITY = 600;
    boolean isCanListener;
    /**
     * debug
     */
    private boolean isDebug = true;

    /**
     * 菜单视图
     */
//    private View mMenuView;
    /**
     * 最底层视图
     */
    private View mBaseView;
    /**
     * 清屏视图
     */
    private View mDrawerView;
    /**
     *
     */
    private Context mContext;
    /**
     * 抽屉视图是否可见
     */
    private boolean flag = true;
    /**
     * 页面状态 1:侧边栏可见，清屏可见 2:侧边栏不可见 清屏可见 3:侧边栏和清屏界面不可见，初始状态是都可见
     */
    private int stateCoutorm = 1;

    /**
     * ViewDragHelper
     */
    private ViewDragHelper mHelper;

    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScrren;

    /**
     * slideview 移动百分比
     */
    private float mSlideViewOnScreen;

    /**
     * 控制的view
     */
    private View controlView;

    /**
     * 是否计算完成
     */
    private boolean isCalculate = false;

    /**
     * 当前State
     */
    private int mDragState;

    /**
     * 侧边栏不随手势滑动
     */
    private int newLeft;


    /**
     * 进度监听
     */
    private ISlideListener slideListener;
    private int openStatus;
    private DragListener mListeners;
    /**
     * ViewDragHelper回调接口
     */
    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            log("clampViewPositionHorizontal");
            int drawerLeft = Math.min(Math.max(getWidth() - child.getWidth(), left), getWidth());
//            if(!isMenuGesture && child == mMenuView) {
//                log("clampViewPositionHorizontal newLeft: " + newLeft);
//                return newLeft;
//            }
            return drawerLeft;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            log("tryCaptureView");
            return false;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            log("onEdgeDragStarted");
            if (mDragState == ViewDragHelper.STATE_IDLE)
                mHelper.captureChildView(controlView, pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            final int childWidth = changedView.getWidth();
//            float offset = (left - (getWidth() - childWidth))* 1.0f / childWidth;
            float offset = (float) left / childWidth;
//            if (slideListener != null) {
            if (offset == 1.0 || offset == 0.0) {
//                    slideListener.onPositionChanged(offset);
                openStatus = (int) offset;
            }
//            }
            if (changedView == mDrawerView) {
                mLeftMenuOnScrren = offset;
            }
            log("onViewPositionChanged" + offset);
//            else if(changedView == mMenuView){
//                mSlideViewOnScreen = offset;
//            }
            changedView.setVisibility(offset == 1 ? View.INVISIBLE : View.VISIBLE);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            log("onViewReleased");
            final int childWidth = releasedChild.getWidth();
            float offset = (float) (releasedChild.getLeft() - (getWidth() - childWidth)) / (float) childWidth;
            if (releasedChild == mDrawerView) {
                mLeftMenuOnScrren = offset;
            }
            isCanListener = false;
//            else if(releasedChild == mMenuView){
//                mSlideViewOnScreen = offset;
//            }
            if (xvel < 0 || xvel == 0 && offset < 0.5f) { // 目标向左移动
                if (releasedChild == mDrawerView) {
                    stateCoutorm = 2;
                }
//                else if(releasedChild == mMenuView){
////                    state = 1;
//                    newLeft = getWidth() - childWidth;
//                }
                flag = true;
                mHelper.settleCapturedViewAt(getWidth() - childWidth, releasedChild.getTop());
            } else { // 目标向右移动
                if (releasedChild == mDrawerView) {
                    stateCoutorm = 3;
                }
//                else if(releasedChild == mMenuView){
//                    newLeft = getWidth();
////                    state = 2;
//                }
                mHelper.settleCapturedViewAt(getWidth(), releasedChild.getTop());
            }
//            mHelper.settleCapturedViewAt(flag ? getWidth() - childWidth : getWidth(), releasedChild.getTop());
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            log("getViewHorizontalDragRange");
            return controlView == child ? child.getWidth() : 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            log("onViewDragStateChanged state----: " + state);
            Log.e("linshijin", state + "==onViewDragStateChanged==" + stateCoutorm);
            mDragState = state;
            updateDragState(state, mHelper.getCapturedView());
        }
    };
    /**
     * 按下点X坐标
     */
    private float startX;
    /**
     * 以下4个参数用来记录按下点的初始坐标和结束坐标
     */
    private float firstDownX = 0;
    private float lastDownX = 0;
    private float firstDownY = 0;
    private float lastDownY = 0;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;


        final float density = getResources().getDisplayMetrics().density;
        final float minVel = SLIDING_VELOCITY * density;

        mHelper = ViewDragHelper.create(this, 1.0f, cb);
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mHelper.setMinVelocity(minVel);

        setDrawerLeftEdgeSize(context, mHelper, 1.0f);
        setOnTouchListener(this);

    }

    public int getStateCoutorm() {
        return stateCoutorm;
    }

    public void setStateCoutorm(int stateCoutorm) {
        this.stateCoutorm = stateCoutorm;
    }

    /**
     * 设置进度监听
     *
     * @param
     * @return void
     */
    public void setSlideListener(ISlideListener slideListener) {
        this.slideListener = slideListener;
    }

    public DragListener getmListeners() {
        return mListeners;
    }

    public void setmListeners(DragListener mListeners) {
        this.mListeners = mListeners;
    }

    public void updateDragState(int activeState, View activeView) {
//        if (isCanListener){
//            return;
//        }
        activeView.post(new Runnable() {
            @Override
            public void run() {
                if (activeView != null && activeState == STATE_IDLE) {
                    if (stateCoutorm == 2 && openStatus == 0) {
                        dispatchDragToOutState(activeView);
                    } else if (stateCoutorm == 3 && openStatus == 1) {
                        dispatchDragToInState(activeView);
                    }
                }
            }
        });


    }

    void dispatchDragToOutState(View dragView) {
        if (mListeners != null) {
            // 发出通知。从列表的末尾开始，这样，如果监听器由于被调用而将自己删除，则不会干扰我们的迭代
            mListeners.onDragToOut(dragView);
        }
    }

    void dispatchDragToInState(View dragView) {
        if (mListeners != null) {
            // 发出通知。从列表的末尾开始，这样，如果监听器由于被调用而将自己删除，则不会干扰我们的迭代
            mListeners.onDragToIn(dragView);
        }
    }

    /**
     * 设置滑动范围
     *
     * @param: activity
     * @param: dragHelper 设置范围的ViewDragHelper
     * @param: displayWidthPercentage 滑动因子，为 1 全屏滑动
     * @return: void
     */
    public void setDrawerLeftEdgeSize(Context activity, ViewDragHelper dragHelper, float displayWidthPercentage) {
        Field edgeSizeField;
        try {
            edgeSizeField = dragHelper.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(dragHelper, (int) (dm.widthPixels * displayWidthPercentage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        MarginLayoutParams lp = (MarginLayoutParams) mDrawerView.getLayoutParams();
        final int menuWidth = mDrawerView.getMeasuredWidth();
        int childLeft = (getWidth() - menuWidth) + (int) (menuWidth * mLeftMenuOnScrren);
        mDrawerView.layout(childLeft, lp.topMargin, childLeft + menuWidth, lp.topMargin + mDrawerView.getMeasuredHeight());

//        MarginLayoutParams lp1 = (MarginLayoutParams) mMenuView.getLayoutParams();
//        final int menuWidth1 = mMenuView.getMeasuredWidth();
//        int childLeft1 = (getWidth() - menuWidth1) + (int)(menuWidth1 * mSlideViewOnScreen);
//        mMenuView.layout(childLeft1, lp1.topMargin, childLeft1 + menuWidth1, lp1.topMargin + mMenuView.getMeasuredHeight());

        newLeft = mDrawerView.getLeft();
    }

    /**
     * 获取容器内的控件引用
     *
     * @param:
     * @return:
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        log("count: " + count);
//        if(count != 3){
//            throw new IllegalArgumentException("child view count must equal 3");
//        }

//        if (count == 2) {
        mBaseView = getChildAt(0);
        mDrawerView = getChildAt(1);
//            mMenuView = getChildAt(2);
//        }
        controlView = mDrawerView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                mHelper.shouldInterceptTouchEvent(ev);
                return false;
            case MotionEvent.ACTION_MOVE:
//                float endX = ev.getX();
//                float distanceX = endX - startX;
//                if(mMenuView.getVisibility() == View.VISIBLE) {
//                    return false;
//                } else if(mMenuView.getVisibility() == View.GONE) {
//                    if ((flag && distanceX > 0 && isGreaterThanMinSize(endX, startX)) || (!flag && distanceX < 0 && isGreaterThanMinSize(endX, startX))) {
//                        return mHelper.shouldInterceptTouchEvent(ev);
//                    } else if ((flag && distanceX < 0) || (!flag && distanceX > 0)) {
//                        return false;
//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                return mHelper.shouldInterceptTouchEvent(ev);
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 滑动范围阈值，可以修改布局上存在滑动控件时的移动范围
     *
     * @param:
     * @return:
     */
    public boolean isGreaterThanMinSize(float x1, float x2) {
        return Math.abs((int) (x1 - x2)) > 66;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                mHelper.processTouchEvent(ev);
                isCalculate = false;//计算开始
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float distanceX = endX - startX;
                if (!isCalculate) {
                    if ((mDrawerView.getLeft() < getWidth() - mDrawerView.getWidth() / 2)) { // 侧边栏隐藏，清屏界面显示
                        if (distanceX > 0) { // 向右滑动，移动drawerview
                            controlView = mDrawerView;
                            isCalculate = true;
                        }
                    } else if (mDrawerView.getLeft() >= getWidth() - mDrawerView.getWidth() / 2) { // 清屏界面隐藏
                        if (distanceX < 0) { // 移动移动drawerview
                            controlView = mDrawerView;
                            isCalculate = true;
                        } else { // 不管
                        }
                    }
                }
                if (isCalculate) {
                    mHelper.processTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_UP:
                mHelper.processTouchEvent(ev);
                break;
            default:
                mHelper.processTouchEvent(ev);
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

//    /**
//     * 关闭drawer，预留
//     * @param:
//     * @return: void
//     */
//    public void closeSlideMenu() {
//        View menuView = mMenuView;
//        mSlideViewOnScreen = 1.0f;
//        newLeft = getWidth();
//        mHelper.smoothSlideViewTo(menuView, getWidth(), menuView.getTop());
//        invalidate();
//    }

//    /**
//     * 打开drawer，预留
//     * @param:
//     * @return: void
//     */
//    public void openSlideMenu() {
//        View menuView = mMenuView;
//        mSlideViewOnScreen = 0.0f;
//        newLeft = getWidth() - mMenuView.getWidth();
//        mHelper.smoothSlideViewTo(menuView, getWidth() - mMenuView.getWidth(), menuView.getTop());
//        invalidate();
//    }

    /**
     * 判断侧滑菜单是否打开
     * @param:
     * @return:
     */
//    public boolean isSlideMenuOpen() {
//        return mMenuView.getLeft() >= getWidth() - mMenuView.getWidth() && mMenuView.getLeft() <  getWidth() - mMenuView.getWidth() / 2;
//    }

    /**
     * 恢复清屏内容
     *
     * @param
     * @return
     */
    public void restoreContent() {
//        isCanListener=true;
//        stateCoutorm = 2;
        if (mDrawerView != null) {
            mLeftMenuOnScrren = 0.0f;
            flag = true;
            View menuView = mDrawerView;
            mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
            invalidate();
        }
    }

    /**
     * 关闭drawer，预留
     *
     * @param:
     * @return: void
     */
    public void closeDrawer() {
//        isCanListener=true;
        mLeftMenuOnScrren = 0.0f;
        View menuView = mDrawerView;
        mHelper.smoothSlideViewTo(menuView, menuView.getWidth(), menuView.getTop());
        stateCoutorm = 3;
        invalidate();
    }

    /**
     * 打开drawer，预留
     *
     * @param:
     * @return: void
     */
    public void openDrawer() {
        mLeftMenuOnScrren = 1.0f;
//        stateCoutorm = 2;
        View menuView = mDrawerView;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = event.getX();
                firstDownY = event.getY();
                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                lastDownX = event.getX();
                lastDownY = event.getY();
                float deltaX = Math.abs(lastDownX - firstDownX);
                float deltaY = Math.abs(lastDownY - firstDownY);
//                if (deltaX < 10 && deltaY < 10) {
//                    if(isTouchCloseSlideArea(lastDownX, lastDownY)) {
//                        if(isSlideMenuOpen()){
//                            closeSlideMenu();
//                            return true;
//                        }
//                    }
//                }
                break;
        }
        return false;
    }

    /**
     * 判断是否点击在了关闭侧边栏的区域
     *
     * @param: x 按下点x坐标
     * @param: y 按下点y坐标
     * @return: true：关闭 false：不关闭
     */
//    private boolean isTouchCloseSlideArea(float x, float y) {
//        int left = 0;
//        int top = 0;
//        int right = getWidth() - mMenuView.getWidth();
//        int bottom = mMenuView.getHeight();
//        if (x >= left && x <= right && y >= top && y <= bottom) {
//            return true;
//        }
//        return false;
//    }
    private void log(String msg) {
        if (isDebug)
            Log.e(TAG, "===================================> " + msg);
    }

    public interface DragListener {

        /**
         * 当遮罩层被拖动至屏幕外时调用
         *
         * @param dragView 被拖动的View
         */
        void onDragToOut(@NonNull View dragView);

        /**
         * 当遮罩层被拖动至屏幕内时调用
         *
         * @param dragView 被拖动的View
         */
        void onDragToIn(@NonNull View dragView);

    }
}