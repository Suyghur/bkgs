package com.pro.maluli.common.view.catloading;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.pro.maluli.R;


public class CatLoadingView extends DialogFragment {

    public static CatLoadingView catLoadingView;

    public CatLoadingView() {
    }

    public static CatLoadingView createInstance() {
        if (catLoadingView == null) {
            catLoadingView = new CatLoadingView();
            catLoadingView.dismissDelayTime = 100;
            catLoadingView.timeOutTime = 25000;
        } else {
            if (catLoadingView.mDialog != null) {
                catLoadingView.mDialog.dismiss();
                catLoadingView.mDialog = null;
            }
        }
        return catLoadingView;
    }

    /**
     * 消失延迟时间 ms
     */
    public long dismissDelayTime = 100;
    /**
     * 最长等待时间
     */
    public long timeOutTime = -1;

    private Animation operatingAnim, eye_left_Anim, eye_right_Anim;

    private Dialog mDialog;

    private View mouse, eye_left, eye_right;

    private EyelidView eyelid_left, eyelid_right;

    private GraduallyTextView mGraduallyTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.catloading_main);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            operatingAnim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            operatingAnim.setRepeatCount(Animation.INFINITE);
            operatingAnim.setDuration(2000);

            eye_left_Anim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            eye_left_Anim.setRepeatCount(Animation.INFINITE);
            eye_left_Anim.setDuration(2000);

            eye_right_Anim = new RotateAnimation(360f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            eye_right_Anim.setRepeatCount(Animation.INFINITE);
            eye_right_Anim.setDuration(2000);

            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            eye_left_Anim.setInterpolator(lin);
            eye_right_Anim.setInterpolator(lin);

            View view = mDialog.getWindow().getDecorView();

            mouse = view.findViewById(R.id.mouse);

            eye_left = view.findViewById(R.id.eye_left);

            eye_right = view.findViewById(R.id.eye_right);

            eyelid_left = (EyelidView) view.findViewById(R.id.eyelid_left);

            eyelid_left.setColor(Color.parseColor("#d0ced1"));

            eyelid_left.setFromFull(true);

            eyelid_right = (EyelidView) view.findViewById(R.id.eyelid_right);

            eyelid_right.setColor(Color.parseColor("#d0ced1"));

            eyelid_right.setFromFull(true);

            mGraduallyTextView = (GraduallyTextView) view
                    .findViewById(R.id.graduallyTextView);

            operatingAnim
                    .setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            eyelid_left.resetAnimator();
                            eyelid_right.resetAnimator();
                        }
                    });
        }
        mDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
        mGraduallyTextView.startLoading();
    }

    @Override
    public void onPause() {
        super.onPause();

        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
        mGraduallyTextView.stopLoading();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        mDialog = null;
//        System.gc();
    }

    public void showView(final Context context) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(this).commit();
            super.show(((FragmentActivity) context).getSupportFragmentManager(), "");
        } catch (Exception e) {

        }
    }

    public void hideView(final Context context) {
        dismiss();
    }
}