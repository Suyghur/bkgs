package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class EditGongGaoDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView toAppealTv, inputMaxTv;
    private EditText editGgEt;
    private String desc;
    private OnEditPersonListener onEditPersonListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_edit_gonggao);

        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        desc = getArguments().getString("DESC");
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        toAppealTv = mDetailDialog.findViewById(R.id.toAppealTv);
        editGgEt = mDetailDialog.findViewById(R.id.editGgEt);
        inputMaxTv = mDetailDialog.findViewById(R.id.inputMaxTv);
        if (!TextUtils.isEmpty(desc)) {
            editGgEt.setText(desc);
        }
        editGgEt.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                inputMaxTv.setText(s.length() + "/" + 200);
                selectionStart = editGgEt.getSelectionStart();
                selectionEnd = editGgEt.getSelectionEnd();
                if (wordNum.length() > 200) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    editGgEt.setText(s);
                    editGgEt.setSelection(tempSelection);//设置光标在最后
                }

            }
        });

        dismissLL.setOnClickListener(this::onClick);
        toAppealTv.setOnClickListener(this::onClick);

        return mDetailDialog;
    }

    public void setOnConfirmListener(OnEditPersonListener onEditPersonListener) {
        this.onEditPersonListener = onEditPersonListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toAppealTv:
                if (TextUtils.isEmpty(editGgEt.getText().toString().trim())) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请输入公告内容");
                    return;
                }
                if (onEditPersonListener != null) {
                    onEditPersonListener.subNumber(editGgEt.getText().toString().trim());
                }
                dismiss();
                break;

            default:
                dismiss();
                break;
        }

    }

    public void dismiss() {
        if (mDetailDialog != null) {
            mDetailDialog.dismiss();
        }
    }

    public interface OnEditPersonListener {
        void subNumber(String type);
    }
}
