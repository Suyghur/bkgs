package com.pro.maluli.common.view.dialogview.bigPicture;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.luck.picture.lib.tools.ScreenUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.SendCommentDialogFragment;
import com.pro.maluli.common.view.myselfView.HackyViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class CheckBigPictureDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private FrameLayout ousideDismissFl;
    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private View mContentView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = super.onCreateDialog(savedInstanceState);
//        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
//        mDetailDialog.setContentView(R.layout.dialog_big_picture);
        mDetailDialog.setCancelable(true);
        mDetailDialog.setCanceledOnTouchOutside(true);
//        //设置背景为透明
//        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
//        int dialogHeight = ToolUtils.getContextRect(getActivity());
//        //设置弹窗大小为会屏
//        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
//        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);


        return mDetailDialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContentView = inflater.inflate(R.layout.dialog_big_picture, container, false);



        return mContentView;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mPager = (HackyViewPager) view.findViewById(R.id.pager);
        ousideDismissFl = view.findViewById(R.id.ousideDismissFl);
        indicator = (TextView) view.findViewById(R.id.indicator);
        ousideDismissFl.setOnClickListener(this);
        pagerPosition = getArguments().getInt(EXTRA_IMAGE_INDEX, 0);
        ArrayList<String> urls = getArguments().getStringArrayList(
                EXTRA_IMAGE_URLS);

//        mPager = (HackyViewPager) mDetailDialog.findViewById(R.id.pager);
//        ousideDismissFl = mDetailDialog.findViewById(R.id.ousideDismissFl);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getChildFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
//        indicator = (TextView) mDetailDialog.findViewById(R.id.indicator);
//        ousideDismissFl.setOnClickListener(this);
        @SuppressLint("StringFormatMatches") CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                @SuppressLint("StringFormatMatches") CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);

    }
    @Override
    public void onResume() {
        int screenWidth = ScreenUtils.getScreenWidth(getActivity())-ScreenUtils.dip2px(getContext(),30);
        int screenHidth =screenWidth*3/2;
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.CENTER;
//        params.windowAnimations = R.style.dialog_bottom;
        getDialog().getWindow().setAttributes(params);
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.dialog_bottom);

        params.width = screenWidth;
        params.height = screenHidth;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
    private OnFreezeTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnFreezeTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnFreezeTipsListener {
        void gotoAppeal(int type);//0去申述，2去绑定
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ousideDismissFl:
                dissmiss();
                break;
        }
    }

    public void dissmiss() {
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
