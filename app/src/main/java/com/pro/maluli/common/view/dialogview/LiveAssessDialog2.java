package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.business.session.activity.my.view.StarBar;
import com.pro.maluli.R;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class LiveAssessDialog2 extends Dialog implements View.OnClickListener {
    private StarBar abilityStar;
    private StarBar serviceStar;
    private int abilityNumber, srviceNumber;
    private TextView comfirmTv, abilityTv, serviceTv;
    private LinearLayout dismissLL;
    private String[] ability = new String[]{"非常差", "差", "一般", "强", "非常强"};
    private String[] service = new String[]{"非常差", "差", "一般", "满意", "非常满意"};
    private OnBaseTipsListener onFreezeTipsListener;

    public LiveAssessDialog2(@NonNull Context context) {
        super(context, R.style.dialog_bottom);
        initView();
    }


    private void initView() {
        //设置背景为透明
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_assess_message);
        abilityStar = findViewById(R.id.abilityStar);
        serviceStar = findViewById(R.id.serviceStar);
//        int dialogHeight = DensityExtKt.getScreenHeight(getContext());
//        //设置弹窗大小为会屏
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
//        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        getWindow().getDecorView().setSystemUiVisibility(flag);

        comfirmTv = findViewById(R.id.comfirmTv);
        dismissLL = findViewById(R.id.dismissLL);
//        cancelTv = findViewById(R.id.cancelTv);
        abilityTv = findViewById(R.id.abilityTv);
        serviceTv = findViewById(R.id.serviceTv);
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
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        comfirmTv.setOnClickListener(this);
        dismissLL.setOnClickListener(this);
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

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
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
                dismiss();
                break;
            case R.id.dismissLL:
                dismiss();
                break;
        }
    }


    public interface OnBaseTipsListener {
        void comfirm(int abilityNumber, int srviceNumber);//0去申述，2去绑定
    }
}
