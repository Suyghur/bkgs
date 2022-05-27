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

public class ShareVideoDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL, qqLL, wechatLL, wechatPyqLL, QQZoneLL, downImagLL;
    private String imgurl;
    private View videoline;
    private boolean isShareVideo = true;
    private OnShareAppListener onShareAppListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_share_video);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);


        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        qqLL = mDetailDialog.findViewById(R.id.qqLL);
        wechatLL = mDetailDialog.findViewById(R.id.wechatLL);
        wechatPyqLL = mDetailDialog.findViewById(R.id.wechatPyqLL);
        QQZoneLL = mDetailDialog.findViewById(R.id.QQZoneLL);
        downImagLL = mDetailDialog.findViewById(R.id.downImagLL);
        videoline = (View) mDetailDialog.findViewById(R.id.videoline);

        isShareVideo = getArguments().getBoolean("IS_SHARE_VIDEO", true);
        if (!isShareVideo) {
            downImagLL.setVisibility(View.GONE);
            videoline.setVisibility(View.GONE);
        } else {
            downImagLL.setVisibility(View.VISIBLE);
            videoline.setVisibility(View.VISIBLE);
        }

        dismissLL.setOnClickListener(this::onClick);
        qqLL.setOnClickListener(this::onClick);
        wechatLL.setOnClickListener(this::onClick);
        wechatPyqLL.setOnClickListener(this::onClick);
        QQZoneLL.setOnClickListener(this::onClick);
        downImagLL.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    public void setOnShareAppListener(OnShareAppListener onShareAppListener) {
        this.onShareAppListener = onShareAppListener;
    }

    @Override
    public void onClick(View v) {
//        if (!ToolUtils.isFastClick()) {
//
//            return;
//        }
        switch (v.getId()) {
            case R.id.dismissLL:
                break;
            case R.id.downImagLL:
                if (onShareAppListener != null) {
                    onShareAppListener.downloadVideo();
                }

                break;
            case R.id.qqLL:
                if (onShareAppListener != null) {
                    onShareAppListener.gotoShare(1);
                }
                break;
            case R.id.wechatLL:
                if (onShareAppListener != null) {
                    onShareAppListener.gotoShare(2);
                }
                break;
            case R.id.wechatPyqLL:
                if (onShareAppListener != null) {
                    onShareAppListener.gotoShare(3);
                }
                break;
            case R.id.QQZoneLL:
                if (onShareAppListener != null) {
                    onShareAppListener.gotoShare(4);
                }
                break;
        }
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }

    public interface OnShareAppListener {
        void gotoShare(int type);//0去申述，2去绑定

        void downloadVideo();
    }
}
