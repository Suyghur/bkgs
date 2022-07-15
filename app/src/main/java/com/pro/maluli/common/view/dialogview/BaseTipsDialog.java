package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class BaseTipsDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView cancelTv, comfirmTv, contentTv, titleTv;
    private String showContent, comfirm, title, cancel;
    private boolean closeEnable = true;
    private boolean hideCancel;//是否显示取消按钮
    private boolean isCanfinish = true;//是否显示取消按钮
    private OnBaseTipsListener onFreezeTipsListener;
    private OnTwoBaseTipsListener onTwoBaseTipsListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_base_tips);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        comfirmTv = mDetailDialog.findViewById(R.id.comfirmTv);
        cancelTv = mDetailDialog.findViewById(R.id.cancelTv);
        contentTv = mDetailDialog.findViewById(R.id.contentTv);
        titleTv = mDetailDialog.findViewById(R.id.titleTv);
        showContent = getArguments().getString("showContent");
        comfirm = getArguments().getString("comfirm");
        title = getArguments().getString("TITLE_DIO");
        cancel = getArguments().getString("cancel");
        hideCancel = getArguments().getBoolean("CANCEL_SEE", false);
        isCanfinish = getArguments().getBoolean("isCanfinish", false);
        closeEnable = getArguments().getBoolean("closeEnable", true);
        if (closeEnable) {
            dismissLL.setVisibility(View.VISIBLE);
        } else {
            dismissLL.setVisibility(View.GONE);
        }
        //是否显示取消按钮
        if (hideCancel) {
            cancelTv.setVisibility(View.GONE);
        } else {
            cancelTv.setVisibility(View.VISIBLE);
        }
        //更改标题
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        //更改提交按钮文字
        if (!TextUtils.isEmpty(comfirm)) {
            comfirmTv.setText(comfirm);
        }
        if (isCanfinish) {
            comfirmTv.setVisibility(View.GONE);
        } else {
            comfirmTv.setVisibility(View.VISIBLE);
        }
        //更改取消按钮文字
        if (!TextUtils.isEmpty(cancel)) {
            cancelTv.setText(cancel);
        }
        dismissLL.setOnClickListener(this::onClick);
        contentTv.setText(showContent);

        cancelTv.setOnClickListener(this::onClick);

        comfirmTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public OnTwoBaseTipsListener getOnTwoBaseTipsListener() {
        return onTwoBaseTipsListener;
    }

    public void setOnTwoBaseTipsListener(OnTwoBaseTipsListener onTwoBaseTipsListener) {
        this.onTwoBaseTipsListener = onTwoBaseTipsListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfirmTv:
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.comfirm();
                }
                if (onTwoBaseTipsListener != null) {
                    onTwoBaseTipsListener.comfirm();
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.dismissLL:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.cancelTv:
                if (onTwoBaseTipsListener != null) {
                    onTwoBaseTipsListener.cancel();
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }

    public interface OnBaseTipsListener {
        void comfirm();//0去申述，2去绑定
    }

    public interface OnTwoBaseTipsListener {
        void comfirm();//0去申述，2去绑定

        void cancel();
    }
}
