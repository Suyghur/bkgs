package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.DonwloadSaveImg;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.sharesdk.tencent.qq.QQ;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */
public class ShareAppDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL, qqLL, wechatLL, wechatPyqLL, QQZoneLL, downImagLL;
    private String imgurl;
    private RoundedImageView APPriv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_share_app);
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
        APPriv = mDetailDialog.findViewById(R.id.APPriv);
        downImagLL = mDetailDialog.findViewById(R.id.downImagLL);
        imgurl = getArguments().getString("IMG");

        dismissLL.setOnClickListener(this::onClick);
        qqLL.setOnClickListener(this::onClick);
        wechatLL.setOnClickListener(this::onClick);
        wechatPyqLL.setOnClickListener(this::onClick);
        QQZoneLL.setOnClickListener(this::onClick);
        downImagLL.setOnClickListener(this::onClick);
        GlideUtils.loadImage(getActivity(), imgurl, APPriv);
        return mDetailDialog;
    }

    private OnShareAppListener onShareAppListener;

    public void setOnShareAppListener(OnShareAppListener onShareAppListener) {
        this.onShareAppListener = onShareAppListener;
    }

    public interface OnShareAppListener {
        void gotoShare(int type);//0去申述，2去绑定
    }
    Bitmap bitmap;
    public Bitmap returnBitMap(final String url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
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
                DonwloadSaveImg.donwloadImg(getActivity(),imgurl);//iPath
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
}
