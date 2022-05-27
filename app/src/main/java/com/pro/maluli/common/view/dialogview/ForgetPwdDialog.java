package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class ForgetPwdDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private String findPwdTips;
    private WebView tipsWv;
    private OnFreezeTipsListener onFreezeTipsListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_forget_pwd_tips);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        tipsWv = mDetailDialog.findViewById(R.id.tipsWv);
        findPwdTips = getArguments().getString("Youth_find_password_tips");

        tipsWv.loadDataWithBaseURL(null, findPwdTips, "text/html", "utf-8", null);
        dismissLL.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnFreezeTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }

    public interface OnFreezeTipsListener {
        void gotoAppeal(int type);//0去申述，2去绑定
    }
}
