package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.ProgressWebView;
import com.pro.maluli.module.myself.setting.youthMode.base.YouthModeAct;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class CloseTeenagerDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private ProgressWebView contentTv;
    private TextView submitTv, titleBottomTv, contentBottomTv;
    private LinearLayout gotoTeenagerLL;
    private YouthEntity youthEntity;
    private RoundedImageView bottomImg;
    private OnFreezeTipsListener onFreezeTipsListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_teenager);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        youthEntity = (YouthEntity) getArguments().getSerializable("TEENAGER_INFO");
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        contentTv = mDetailDialog.findViewById(R.id.contentTv);
        submitTv = mDetailDialog.findViewById(R.id.submitTv);
        titleBottomTv = mDetailDialog.findViewById(R.id.titleBottomTv);
        contentBottomTv = mDetailDialog.findViewById(R.id.contentBottomTv);
        gotoTeenagerLL = mDetailDialog.findViewById(R.id.gotoTeenagerLL);
        bottomImg = mDetailDialog.findViewById(R.id.bottomImg);
        contentTv.setMaxHeight((int) getActivity().getResources().getDimension(R.dimen.dp_150));
        contentTv.loadDataWithBaseURL(null, youthEntity.getTop().getDesc(), "text/html", "utf-8", null);

        GlideUtils.loadImageNoImage(getActivity(), youthEntity.getBottom().getImage(), bottomImg);
        titleBottomTv.setText(youthEntity.getBottom().getTitle());
        contentBottomTv.setText(youthEntity.getBottom().getContent());
        dismissLL.setOnClickListener(this::onClick);
        submitTv.setOnClickListener(this::onClick);
        gotoTeenagerLL.setOnClickListener(this::onClick);

        return mDetailDialog;
    }

    public void setOnConfirmListener(OnFreezeTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
            case R.id.submitTv:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
            case R.id.gotoTeenagerLL:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                if (mDetailDialog != null) {
                    Intent intent = new Intent(getActivity(), YouthModeAct.class);
                    requireActivity().startActivity(intent);
                    mDetailDialog.dismiss();
                }
                break;
        }
    }

    public interface OnFreezeTipsListener {
        void gotoAppeal(int type);//0去申述，2去绑定
    }
}
