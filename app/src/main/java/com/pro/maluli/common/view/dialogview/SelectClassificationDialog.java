package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class SelectClassificationDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL, oneToMoreLL, oneToOneLL;
    private OnLiveTypeListener onLiveTypeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_classification);
        mDetailDialog.setCancelable(true);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        oneToMoreLL = mDetailDialog.findViewById(R.id.oneToMoreLL);
        oneToOneLL = mDetailDialog.findViewById(R.id.oneToOneLL);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
//        BarUtils.setStatusBarVisibility(mDetailDialog.getWindow(), true);
        dismissLL.setOnClickListener(this);
        oneToMoreLL.setOnClickListener(this);
        oneToOneLL.setOnClickListener(this);

        return mDetailDialog;
    }

    public void setOnTimeListener(OnLiveTypeListener onLiveTypeListener) {
        this.onLiveTypeListener = onLiveTypeListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
                mDetailDialog.dismiss();
                break;
            case R.id.oneToMoreLL:
                if (onLiveTypeListener != null) {
                    onLiveTypeListener.confirmSuccess(1);
                }
                mDetailDialog.dismiss();

                break;
            case R.id.oneToOneLL:
                if (onLiveTypeListener != null) {
                    onLiveTypeListener.confirmSuccess(2);
                }
                mDetailDialog.dismiss();
                break;
            default:

                break;
        }
    }

    public interface OnLiveTypeListener {
        void confirmSuccess(int type);
    }
}
