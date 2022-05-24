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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ImageEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.module.myself.myAccount.appeal.adapter.AppealAdapter;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class SubReserveDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView comfirmTv, toAppealTv, inputMaxTv;
    private EditText personTv;
    private RecyclerView selectImgRv;
    AppealAdapter adapter;
    private List<ImageEntity> imageEntities = new ArrayList<>();
    List<File> files = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_sub_reserve);
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
        personTv = mDetailDialog.findViewById(R.id.personTv);
        selectImgRv = mDetailDialog.findViewById(R.id.selectImgRv);
        inputMaxTv = mDetailDialog.findViewById(R.id.inputMaxTv);
        personTv.addTextChangedListener(new TextWatcher() {
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
                selectionStart = personTv.getSelectionStart();
                selectionEnd = personTv.getSelectionEnd();
                if (wordNum.length() > 200) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    personTv.setText(s);
                    personTv.setSelection(tempSelection);//设置光标在最后
                }

            }
        });
        dismissLL.setOnClickListener(this);
        comfirmTv.setOnClickListener(this);
        toAppealTv.setOnClickListener(this);
        imageEntities.add(new ImageEntity("", 0));

        adapter = new AppealAdapter(imageEntities);
        selectImgRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        selectImgRv.setAdapter(adapter);

        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        adapter.addChildClickViewIds(R.id.selectImgRiv, R.id.deleteImg);
        // 设置子控件点击监听
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.selectImgRiv:
                        selectFormLocation(position);
                        break;
                    case R.id.deleteImg:
                        imageEntities.remove(position);//删除数据源,移除集合中当前下标的数据
                        boolean isAdd = false;
                        for (int i = 0; i < imageEntities.size(); i++) {
                            if (imageEntities.get(i).getType() == 0) {
                                isAdd = true;
                            }
                        }
                        if (!isAdd) {
                            imageEntities.add(new ImageEntity("添加", 0));
                        }
//                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(position);//刷新被删除的地方
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount()); //刷新被删除数据，以及其后面的数据
                        break;
                }

            }
        });

        return mDetailDialog;
    }

    private void selectFormLocation(int position) {
        if (imageEntities.get(position).getType() == 1) {
            return;
        }
        int max = 4 - imageEntities.size();
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(max)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // onResult Callback

                        for (int i = 0; i < result.size(); i++) {
                            imageEntities.add(imageEntities.size() - 1 >= 0 ? imageEntities.size() - 1 : 0
                                    , new ImageEntity(result.get(i).getCutPath(), 1));
//                            File file = new File(result.get(i).getCutPath());
//                            files.add(file);
                        }
                        if (imageEntities.size() > 3) {
                            imageEntities.remove(imageEntities.size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }

    private OnEditPersonListener onEditPersonListener;

    public void setOnConfirmListener(OnEditPersonListener onEditPersonListener) {
        this.onEditPersonListener = onEditPersonListener;
    }

    public interface OnEditPersonListener {
        void subNumber(List<File> files, String type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toAppealTv:
                if (TextUtils.isEmpty(personTv.getText().toString().trim())) {
                    ToastUtils.showShort("请填写预约信息");
                    return;
                }
                files.clear();
                for (int i = 0; i < imageEntities.size(); i++) {
                    if (imageEntities.get(i).getType() == 1) {
                        File file = new File(imageEntities.get(i).getUrl());
                        files.add(file);
                    }
                }
                if (onEditPersonListener != null) {
                    onEditPersonListener.subNumber(files, personTv.getText().toString().trim());
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
