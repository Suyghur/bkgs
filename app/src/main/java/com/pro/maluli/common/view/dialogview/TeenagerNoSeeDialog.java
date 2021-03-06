package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.app.BKGSApplication;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class TeenagerNoSeeDialog extends DialogFragment implements View.OnClickListener {

    private Dialog mDetailDialog;
    private TextView comfirmTv;
    private long exitTime = 0;
    private OnBaseTipsListener onFreezeTipsListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_teenager_no_see);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        comfirmTv = mDetailDialog.findViewById(R.id.comfirmTv);

        comfirmTv.setOnClickListener(this::onClick);
        mDetailDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    exit();
                }
                return true;
            }
        });
        return mDetailDialog;
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            if (onFreezeTipsListener != null) {
                onFreezeTipsListener.finishAll();
            }
        }
    }

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comfirmTv) {
            if (onFreezeTipsListener != null) {
                onFreezeTipsListener.comfirm();
            }
//            if (mDetailDialog != null) {
//                mDetailDialog.dismiss();
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e(BKGSApplication.youthModeStatus);
        if (BKGSApplication.youthModeStatus == 0) {
            mDetailDialog.dismiss();
        }
    }

    public interface OnBaseTipsListener {
        void comfirm();

        void finishAll();
    }
}
