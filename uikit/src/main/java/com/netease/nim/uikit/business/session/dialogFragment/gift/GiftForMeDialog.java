package com.netease.nim.uikit.business.session.dialogFragment.gift;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class GiftForMeDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private RelativeLayout giftMainRl;
    private GiftForMeEntity giftForMeEntity;
    private RecyclerView giftRlv;
    private GiftForMeListAdapter adapter;
    private TextView allMoneyTv, nodataTv,yigognTv;
    private boolean isfromSX;//true 私信弹框

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_gift_for_me);
        mDetailDialog.setCancelable(true);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        giftRlv = mDetailDialog.findViewById(R.id.giftRlv);
        allMoneyTv = mDetailDialog.findViewById(R.id.allMoneyTv);
        nodataTv = mDetailDialog.findViewById(R.id.nodataTv);
        giftMainRl = mDetailDialog.findViewById(R.id.giftMainRl);
        yigognTv = mDetailDialog.findViewById(R.id.yigognTv);
        giftRlv.setLayoutManager(new LinearLayoutManager(getActivity()));

        giftForMeEntity = (GiftForMeEntity) getArguments().getSerializable("GIFT_FOR_ME");
        isfromSX = getArguments().getBoolean("SHI_XIN", false);
        allMoneyTv.setText(giftForMeEntity.getMoney() + "");
        adapter = new GiftForMeListAdapter(getActivity(), giftForMeEntity);
        giftRlv.setAdapter(adapter);
        if (giftForMeEntity.getList().size() == 0) {
            nodataTv.setVisibility(View.VISIBLE);
            giftRlv.setVisibility(View.GONE);
        } else {
            nodataTv.setVisibility(View.GONE);
            giftRlv.setVisibility(View.VISIBLE);

        }
        if (isfromSX) {
            yigognTv.setText("与该用户私信累计获得：");
        } else {
            yigognTv.setText("本场直播累计获得:");
        }
        dismissLL.setOnClickListener(this);
        giftMainRl.setOnClickListener(this);
        return mDetailDialog;
    }

    private OnBaseTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public interface OnBaseTipsListener {
        void comfirm();//0去申述，2去绑定
    }

    public interface OnTwoBaseTipsListener {
        void comfirm();//0去申述，2去绑定

        void cancel();
    }

    private OnTwoBaseTipsListener onTwoBaseTipsListener;

    public OnTwoBaseTipsListener getOnTwoBaseTipsListener() {
        return onTwoBaseTipsListener;
    }

    public void setOnTwoBaseTipsListener(OnTwoBaseTipsListener onTwoBaseTipsListener) {
        this.onTwoBaseTipsListener = onTwoBaseTipsListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.comfirmTv) {
            if (onFreezeTipsListener != null) {
                onFreezeTipsListener.comfirm();
            }
            if (onTwoBaseTipsListener != null) {
                onTwoBaseTipsListener.comfirm();
            }
            dismissDialog();
        } else if (id == R.id.dismissLL) {
            dismissDialog();
        } else if (id == R.id.cancelTv) {
            if (onTwoBaseTipsListener != null) {
                onTwoBaseTipsListener.cancel();
            }
            dismissDialog();
        } else if (id == R.id.giftMainRl) {
            dismissDialog();
        }
    }

    private void dismissDialog() {
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }
}
