package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.myselfView.ProgressWebView;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class PrivacyDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private ProtocolDetailEntity.ListBean listBean;
    private ProgressWebView tipsWv;
    private ImageView successImg;
    private TextView submitTv, xieyiTv;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_privacy);
        mDetailDialog.setCancelable(true);
//        设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        xieyiTv = mDetailDialog.findViewById(R.id.xieyiTv);
        tipsWv = mDetailDialog.findViewById(R.id.tipsWv);
        successImg = mDetailDialog.findViewById(R.id.successImg);
        submitTv = mDetailDialog.findViewById(R.id.submitTv);
        listBean = (ProtocolDetailEntity.ListBean) getArguments().getSerializable("PRIVACY_DATA");
        tipsWv.setMaxHeight((int) getActivity().getResources().getDimension(R.dimen.dp_200));
        tipsWv.loadDataWithBaseURL(null, listBean.getContent(), "text/html", "utf-8", null);
//        dismissLL.setOnClickListener(this::onClick);
        successImg.setOnClickListener(this::onClick);
        submitTv.setOnClickListener(this::onClick);

        String str = "已阅读并同意《隐私协议》";

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《") + 1;//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (AntiShake.check(widget.getId())) {
                    return;
                }
                Intent intent = new Intent(getActivity(), ProtocolDetailAct.class);
                intent.putExtra("PROTOCOL_ID", "3");
                startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString("PROTOCOL_ID", "2");
//                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.c_8e1d77));
                ds.setUnderlineText(false);
            }
        }, start, start + 4, 0);

        xieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
        xieyiTv.setText(ssb, TextView.BufferType.SPANNABLE);
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
            case R.id.successImg:
                if (successImg.isSelected()) {
                    successImg.setSelected(false);
                    submitTv.setSelected(false);
                } else {
                    successImg.setSelected(true);
                    submitTv.setSelected(true);
                }
                break;
            case R.id.submitTv:
                if (!successImg.isSelected()) {
                    ToastUtils.showShort("请阅读并同意隐私协议");
                    return;
                }
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.gotoAppeal(1);
                }
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }
}
