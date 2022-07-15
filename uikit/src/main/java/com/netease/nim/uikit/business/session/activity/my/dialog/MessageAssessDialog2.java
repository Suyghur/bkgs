package com.netease.nim.uikit.business.session.activity.my.dialog;

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

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.view.StarBar;
import com.netease.nim.uikit.common.ToastHelper;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class MessageAssessDialog2 extends Dialog implements View.OnClickListener {
    private StarBar abilityStar;
    private StarBar serviceStar;
    private int abilityNumber, srviceNumber;
    private TextView comfirmTv, abilityTv, serviceTv;
    private LinearLayout dismissLL;
    private String[] ability = new String[]{"非常差", "差", "一般", "强", "非常强"};
    private String[] service = new String[]{"非常差", "差", "一般", "满意", "非常满意"};
    private OnBaseTipsListener onFreezeTipsListener;

    public MessageAssessDialog2(@NonNull Context context) {
//        super(context, R.style.dialog_default_style);
        super(context);
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
        int id = v.getId();
        if (id == R.id.comfirmTv) {
            if (onFreezeTipsListener != null) {
                if (abilityNumber == 0) {
                    ToastHelper.showToast(getContext(), "请为直播专业能力打星");
                    return;
                }
                if (srviceNumber == 0) {
                    ToastHelper.showToast(getContext(), "请为直播服务质量打星");
                    return;
                }
                onFreezeTipsListener.comfirm(abilityNumber, srviceNumber);
            }
            dismiss();
        } else if (id == R.id.dismissLL) {
            dismiss();
        }
    }


    public interface OnBaseTipsListener {
        void comfirm(int abilityNumber, int srviceNumber);//0去申述，2去绑定
    }
}
