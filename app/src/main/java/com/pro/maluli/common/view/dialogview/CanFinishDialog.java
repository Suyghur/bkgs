package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
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
 * 禁播通知
 */

public class CanFinishDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView cancelTv, comfirmTv, contentTv, titleTv;
    private String showContent;
    private OnBaseTipsListener onFreezeTipsListener;

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
        dismissLL.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);
        showContent = getArguments().getString("showContent");
        if (!TextUtils.isEmpty(showContent)) {
            contentTv.setText(showContent);
        }
        mDetailDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }

            }
        });

        comfirmTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfirmTv:
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.comfirm();
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
}
