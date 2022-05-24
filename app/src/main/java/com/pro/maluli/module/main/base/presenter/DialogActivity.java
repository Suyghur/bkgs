package com.pro.maluli.module.main.base.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.module.myself.setting.base.SettingAct;
import com.pro.maluli.module.other.login.LoginAct;


public class DialogActivity extends Activity {
    private LinearLayout dismissLL;
    private TextView cancelTv, comfirmTv, contentTv, titleTv;

    // 接听来电
    public static void showDialogActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);
        dismissLL = findViewById(R.id.dismissLL);
        comfirmTv = findViewById(R.id.comfirmTv);
        cancelTv = findViewById(R.id.cancelTv);
        contentTv = findViewById(R.id.contentTv);
        titleTv = findViewById(R.id.titleTv);
        AcacheUtil.loginOut(this);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dismissLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity.this, LoginAct.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
