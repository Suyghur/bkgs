package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class LiveMoreDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private RelativeLayout finishRl;
    private LinearLayout fenxiangLL, jvbaoLL, toupingLL, qingpingLL;
    private String imgurl;
    private boolean isAnchor;
    private OnLiveControlListener onLiveControlListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_live_more);
        mDetailDialog.setCancelable(true);
//           //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        finishRl = mDetailDialog.findViewById(R.id.finishRl);
        fenxiangLL = mDetailDialog.findViewById(R.id.fenxiangLL);
        jvbaoLL = mDetailDialog.findViewById(R.id.jvbaoLL);
        toupingLL = mDetailDialog.findViewById(R.id.toupingLL);
        qingpingLL = mDetailDialog.findViewById(R.id.qingpingLL);
        isAnchor = getArguments().getBoolean("ISANCHOR", false);
        if (isAnchor) {
            jvbaoLL.setVisibility(View.GONE);
        } else {
            jvbaoLL.setVisibility(View.VISIBLE);

        }
        finishRl.setOnClickListener(this::onClick);
        fenxiangLL.setOnClickListener(this::onClick);
        jvbaoLL.setOnClickListener(this::onClick);
        toupingLL.setOnClickListener(this::onClick);
        qingpingLL.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    public void setOnLiveControlListener(OnLiveControlListener onLiveControlListener) {
        this.onLiveControlListener = onLiveControlListener;
    }

    @Override
    public void onClick(View v) {
//        if (!ToolUtils.isFastClick()) {
//
//            return;
//        }
        switch (v.getId()) {
            case R.id.finishRl:
                break;
            case R.id.jvbaoLL:
                if (onLiveControlListener != null) {
                    onLiveControlListener.operation(1);
                }
                break;
            case R.id.toupingLL:
                if (onLiveControlListener != null) {
                    onLiveControlListener.operation(2);
                }
                break;
            case R.id.qingpingLL:
                if (onLiveControlListener != null) {
                    onLiveControlListener.operation(3);
                }
                break;
            case R.id.fenxiangLL:
                if (onLiveControlListener != null) {
                    onLiveControlListener.operation(4);
                }
                break;
        }
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }

    public interface OnLiveControlListener {
        void operation(int type);//0去申述，2去绑定
    }
}
