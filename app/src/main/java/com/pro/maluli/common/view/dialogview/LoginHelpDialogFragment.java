package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;

import org.jetbrains.annotations.NotNull;


/**
 * 上传图片，选择上传方式
 */
public class LoginHelpDialogFragment extends DialogFragment implements View.OnClickListener {

    public OnSelectGoldenListener onSelectGoldenListener;
    private Dialog mDetailDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = super.onCreateDialog(savedInstanceState);
//        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
//        mDetailDialog.setContentView(R.layout.dialog_comment);
        mDetailDialog.setCanceledOnTouchOutside(true);
        Window win = mDetailDialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        //这里就是直接去掉边距的代码。
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        win.setSoftInputMode(params.SOFT_INPUT_ADJUST_NOTHING);//设置使软键盘弹出的时候dialog不会被顶起
        win.setWindowAnimations(R.style.dialog_bottom);//这里设置dialog的进出动画
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnSelectGoldenListener onSelectGoldenListener) {
        this.onSelectGoldenListener = onSelectGoldenListener;
    }

    public void show() {
        if (mDetailDialog != null) {
            mDetailDialog.show();
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // 在这里将view的高度设置为精确高度，即可屏蔽向上滑动不占全屏的手势。如果不设置高度的话 会默认向上滑动时dialog覆盖全屏
        View view = inflater.inflate(R.layout.dialog_login_help, container, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView copyTv = view.findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        ImageView dismissImg = view.findViewById(R.id.dismissImg);
        dismissImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                ClipboardUtils.copyText("BKGS_help@163.com");
                ToastUtils.showShort("复制成功");
                break;
            case R.id.dismissImg:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;

        }
    }

    public interface OnSelectGoldenListener {
        void goldenSelect();
    }
}

