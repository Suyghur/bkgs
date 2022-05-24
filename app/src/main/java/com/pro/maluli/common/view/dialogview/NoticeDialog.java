package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.myselfView.ProgressWebView;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class NoticeDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private ProgressWebView tipsWv;
    private TextView submitTv;
    private UserInfoEntity.NoticeBean bean;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_notice);
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
        tipsWv.setMaxHeight((int) getActivity().getResources().getDimension(R.dimen.dp_150));
        submitTv = mDetailDialog.findViewById(R.id.submitTv);
        bean = (UserInfoEntity.NoticeBean) getArguments().getSerializable("NOTIC_ENTITY");

        tipsWv.loadDataWithBaseURL(null, bean.getContent(), "text/html", "utf-8", null);
        mDetailDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
        {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (onNoticeListener != null) {
                        onNoticeListener.confirm(bean.getId(),false);
                    }
                    dismiss();
                    return true;
                }else if(keyCode == KeyEvent.KEYCODE_MENU) {
                    return true;
                }
                return false;
            }
        });
        dismissLL.setOnClickListener(this::onClick);
        submitTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    private OnNoticeListener onNoticeListener;

    public void setOnConfirmListener(OnNoticeListener onNoticeListener) {
        this.onNoticeListener = onNoticeListener;
    }

    public interface OnNoticeListener {
        void confirm(int type,boolean isFinish);//0去申述，2去绑定
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
                if (onNoticeListener != null) {
                    onNoticeListener.confirm(bean.getId(),false);
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.submitTv:
                if (onNoticeListener != null) {
                    onNoticeListener.confirm(bean.getId(),true);
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }
}
