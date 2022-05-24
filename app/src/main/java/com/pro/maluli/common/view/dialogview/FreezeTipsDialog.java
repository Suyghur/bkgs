package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.myselfView.WheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class FreezeTipsDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView comfirmTv, toAppealTv, contentTv;
    private String showTips;
    private boolean isgoAppeal=true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_freeze_tips);
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
        toAppealTv = mDetailDialog.findViewById(R.id.toAppealTv);

        contentTv = mDetailDialog.findViewById(R.id.contentTv);
        showTips = getArguments().getString("showTips");
        isgoAppeal = getArguments().getBoolean("goAppeal",true);

        if (!isgoAppeal) {
            toAppealTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(showTips)) {
            contentTv.setText(showTips);
        }
        dismissLL.setOnClickListener(this::onClick);
        comfirmTv.setOnClickListener(this::onClick);
        toAppealTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    private OnFreezeTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnFreezeTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnFreezeTipsListener {
        void gotoAppeal(int type);//0去申述，2去绑定
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.comfirmTv:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.toAppealTv:
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.gotoAppeal(1);
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;

            default:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }
}
