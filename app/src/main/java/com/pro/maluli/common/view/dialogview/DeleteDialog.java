package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class DeleteDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private RelativeLayout finishRl;
    private TextView deleteCommentTv, cancelTv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_delete);
        mDetailDialog.setCancelable(true);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        finishRl = mDetailDialog.findViewById(R.id.finishRl);
        deleteCommentTv = mDetailDialog.findViewById(R.id.deleteCommentTv);
        cancelTv = mDetailDialog.findViewById(R.id.cancelTv);

        finishRl.setOnClickListener(this::onClick);
        deleteCommentTv.setOnClickListener(this::onClick);
        cancelTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    private OnBaseTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnBaseTipsListener {
        void comfirm();//0去申述，2去绑定
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteCommentTv:
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.comfirm();
                }
            case R.id.finishRl:
            case R.id.cancelTv:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }
}
