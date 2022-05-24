package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class EditPersonDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView comfirmTv, toAppealTv;
    private int reserve_number;
    private EditText contentTv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_edit_person);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        comfirmTv = mDetailDialog.findViewById(R.id.comfirmTv);
        toAppealTv = mDetailDialog.findViewById(R.id.toAppealTv);
        contentTv = mDetailDialog.findViewById(R.id.contentTv);
        contentTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    contentTv.setText("1");
                    contentTv.setSelection(1);
                }
            }
        });

        reserve_number = getArguments().getInt("Reserve_number");
        dismissLL.setOnClickListener(this::onClick);
        comfirmTv.setOnClickListener(this::onClick);
        toAppealTv.setOnClickListener(this::onClick);
        return mDetailDialog;
    }

    private OnEditPersonListener onEditPersonListener;

    public void setOnConfirmListener(OnEditPersonListener onEditPersonListener) {
        this.onEditPersonListener = onEditPersonListener;
    }

    public interface OnEditPersonListener {
        void subNumber(String type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toAppealTv:
                if (TextUtils.isEmpty(contentTv.getText().toString().trim())
                        || reserve_number > Integer.valueOf(contentTv.getText().toString().trim())) {
                    ToastUtils.showShort("请输入正确的可预约人数");
                    return;
                }
                if (onEditPersonListener != null) {
                    onEditPersonListener.subNumber(contentTv.getText().toString().trim());
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
}
