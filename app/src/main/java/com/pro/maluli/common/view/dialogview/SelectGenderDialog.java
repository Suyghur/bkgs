package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.myselfView.WheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class SelectGenderDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private WheelView sexWlv;
    private TextView submitTv;
    private ImageView dismissImg;
    private int genderType = 1;
    private OnSelectGenderListener onSelectGenderListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_gender);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        sexWlv = mDetailDialog.findViewById(R.id.sexWlv);
        submitTv = mDetailDialog.findViewById(R.id.submitTv);
        dismissImg = mDetailDialog.findViewById(R.id.dismissImg);
        dismissImg.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        sexWlv.setOffset(1);
        List<String> genderList = new ArrayList<>();
        genderList.add("男");
        genderList.add("女");
        sexWlv.setItems(genderList);
        sexWlv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                genderType = selectedIndex;
            }
        });
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnSelectGenderListener onSelectGenderListener) {
        this.onSelectGenderListener = onSelectGenderListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissImg:
                mDetailDialog.dismiss();
                break;
            case R.id.submitTv:
                mDetailDialog.dismiss();
                if (onSelectGenderListener != null) {
                    onSelectGenderListener.confirmSuccess(genderType);
                }
                break;

            default:

                break;
        }
    }

    public interface OnSelectGenderListener {
        void confirmSuccess(int genderType);
    }
}
