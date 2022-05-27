package com.pro.maluli.common.view.myselfView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class LikeLayout extends FrameLayout {
    public LikeLayoutHandler handler = new LikeLayoutHandler(this);
    private int mClickCount = 0;
    private LikeLayoutListener likeLayoutListener;

    public LikeLayout(@NonNull Context context) {
        super(context);
    }

    public LikeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LikeLayoutListener getLikeLayoutListener() {
        return likeLayoutListener;
    }

    public void setLikeLayoutListener(LikeLayoutListener likeLayoutListener) {
        this.likeLayoutListener = likeLayoutListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == 0) {
                float x = event.getX();
                float y = event.getY();
                this.mClickCount++;
                this.handler.removeCallbacksAndMessages((Object) null);
                if (this.mClickCount >= 2) {
                    this.handler.sendEmptyMessageDelayed(1, 500L);
                } else {
                    this.handler.sendEmptyMessageDelayed(0, 500L);
                }
            }
        }

        return false;
    }

    public void onPause() {
        mClickCount = 0;
        handler.removeCallbacksAndMessages(null);
    }

    private void pauseClick() {
        if (mClickCount == 1) {
            if (likeLayoutListener != null) {
                likeLayoutListener.setOnPause();
            }
        }
        mClickCount = 0;

    }

    public interface LikeLayoutListener {
        void setOnlike();

        void setOnPause();
    }

    private static final class LikeLayoutHandler extends Handler {
        private final WeakReference<LikeLayout> mView;

        public LikeLayoutHandler(@NotNull LikeLayout view) {
            super();
            this.mView = new WeakReference(view);
        }

        public void handleMessage(@org.jetbrains.annotations.Nullable Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mView != null) {
                        mView.get().pauseClick();
                    }
                    break;
                case 1:
                    if (mView != null) {
                        mView.get().onPause();
                    }
                    break;
            }

        }


    }
}
