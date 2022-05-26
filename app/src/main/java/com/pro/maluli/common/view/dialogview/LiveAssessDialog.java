package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.business.session.activity.my.view.StarBar;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class LiveAssessDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private StarBar abilityStar;
    private StarBar serviceStar;
    private int abilityNumber, srviceNumber;
    private TextView comfirmTv, abilityTv, serviceTv;
    private LinearLayout dismissLL;
    private RelativeLayout cancelTv;
    private String[] ability = new String[]{"非常差", "差", "一般", "强", "非常强"};
    private String[] service = new String[]{"非常差", "差", "一般", "满意", "非常满意"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_assess_message);
        mDetailDialog.setCanceledOnTouchOutside(false);
        abilityStar = mDetailDialog.findViewById(R.id.abilityStar);
        serviceStar = mDetailDialog.findViewById(R.id.serviceStar);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        comfirmTv = (TextView) mDetailDialog.findViewById(R.id.comfirmTv);
        dismissLL = (LinearLayout) mDetailDialog.findViewById(R.id.dismissLL);
        cancelTv = (RelativeLayout) mDetailDialog.findViewById(R.id.cancelTv);
        abilityTv = mDetailDialog.findViewById(R.id.abilityTv);
        serviceTv = mDetailDialog.findViewById(R.id.serviceTv);
        abilityStar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(int mark) {
                abilityNumber = mark;
                abilityTv.setText(ability[abilityNumber - 1]);
                canOnclick();
            }
        });
        serviceStar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(int mark) {
                srviceNumber = mark;
                serviceTv.setText(service[srviceNumber - 1]);
                canOnclick();
            }
        });

        mDetailDialog.setCancelable(true);
        comfirmTv.setOnClickListener(this);
        dismissLL.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        return mDetailDialog;
    }

    private void canOnclick() {
        if (srviceNumber == 0 && abilityNumber == 0) {
            comfirmTv.setBackgroundResource(R.drawable.shape_20000000_32);
            comfirmTv.setClickable(false);
        } else {
            comfirmTv.setBackgroundResource(R.drawable.shape_b23299_d23db3_34);
            comfirmTv.setClickable(true);
        }
    }

    private OnBaseTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnBaseTipsListener {
        void comfirm(int abilityNumber, int srviceNumber);//0去申述，2去绑定
    }

    private void dismissView() {
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfirmTv:
                if (onFreezeTipsListener != null) {
                    if (abilityNumber == 0) {
                        ToastUtils.showShort("请为直播专业能力打星");
                        return;
                    }
                    if (srviceNumber == 0) {
                        ToastUtils.showShort("请为直播服务质量打星");
                        return;
                    }
                    onFreezeTipsListener.comfirm(abilityNumber, srviceNumber);
                }
                dismissView();
                break;
            case R.id.dismissLL:
            case R.id.cancelTv:
                dismissView();
                break;
        }
    }
}
