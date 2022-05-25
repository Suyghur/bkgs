package com.netease.nim.uikit.business.session.activity.my.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.view.StarBar;
import com.netease.nim.uikit.common.ToastHelper;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class MessageAssessDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private StarBar abilityStar;
    private StarBar serviceStar;
    private TextView comfirmTv,abilityTv,serviceTv;
    private LinearLayout dismissLL;
    private RelativeLayout cancelTv;
    private int abilityNumber, srviceNumber;
    private String[] ability = new String[]{"非常差", "差", "一般", "强", "非常强"};
    private String[] service = new String[]{"非常差", "差", "一般", "满意", "非常满意"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_assess_message);
        abilityStar = mDetailDialog.findViewById(R.id.abilityStar);
        serviceStar = mDetailDialog.findViewById(R.id.serviceStar);
        comfirmTv = mDetailDialog.findViewById(R.id.comfirmTv);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        cancelTv = mDetailDialog.findViewById(R.id.cancelTv);
        abilityTv = mDetailDialog.findViewById(R.id.abilityTv);
        serviceTv = mDetailDialog.findViewById(R.id.serviceTv);
        abilityStar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(int mark) {
                abilityNumber = mark;
                abilityTv.setText(ability[abilityNumber-1]);
            }
        });
        serviceStar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(int mark) {
                srviceNumber = mark;
                serviceTv.setText(service[srviceNumber-1]);
            }
        });

        mDetailDialog.setCancelable(true);
        comfirmTv.setOnClickListener(this);
        dismissLL.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        return mDetailDialog;
    }

    private OnBaseTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnBaseTipsListener {
        void comfirm(int abilityNumber, int srviceNumber);//0去申述，2去绑定
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.comfirmTv) {
            if (onFreezeTipsListener != null) {
                if (abilityNumber == 0) {
                    ToastHelper.showToast(getActivity(), "请为直播专业能力打星");
                    return;
                }
                if (srviceNumber == 0) {
                    ToastHelper.showToast(getActivity(), "请为直播服务质量打星");
                    return;
                }
                onFreezeTipsListener.comfirm(abilityNumber, srviceNumber);
            }


            if (mDetailDialog != null) {
                mDetailDialog.dismiss();
            }
        } else if (id == R.id.dismissLL || id == R.id.cancelTv) {
            if (mDetailDialog != null) {
                mDetailDialog.dismiss();
            }
        }
    }
}
