package com.pro.maluli.common.view.dialogview.checkMsg;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class CheckMsgDialog extends DialogFragment implements View.OnClickListener {
    CheckMsgAdapter adapter;
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private RecyclerView selectImgRv;
    private TextView titleTv, msgContentTv;
    private SeeLiveUserEntity.AppointBean seeLiveUserEntity;
    private OnEditPersonListener onEditPersonListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_check_msg);
        mDetailDialog.setCancelable(true);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        selectImgRv = mDetailDialog.findViewById(R.id.selectImgRv);
        titleTv = mDetailDialog.findViewById(R.id.titleTv);
        msgContentTv = mDetailDialog.findViewById(R.id.msgContentTv);
        seeLiveUserEntity = (SeeLiveUserEntity.AppointBean) getArguments().getSerializable("Message");
        dismissLL.setOnClickListener(this);

        if (seeLiveUserEntity != null && seeLiveUserEntity.getImages().size() != 0) {
            selectImgRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            adapter = new CheckMsgAdapter(seeLiveUserEntity.getImages(), getActivity());
            selectImgRv.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                    ArrayList<String> urls = new ArrayList<>();
                    for (int i = 0; i < seeLiveUserEntity.getImages().size(); i++) {
                        urls.add(seeLiveUserEntity.getImages().get(i));
                    }
                    CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                    bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                    bigPictureDialog.setArguments(bundle);
                    bigPictureDialog.show(getChildFragmentManager(), "CheckBigPictureDialog");
                }
            });
        }
        if (seeLiveUserEntity != null) {
            msgContentTv.setText(seeLiveUserEntity.getContent());
        }

//        //         先注册需要点击的子控件id（注意，请不要写在convert方法里）
//        adapter.addChildClickViewIds(R.id.selectImgRiv, R.id.deleteImg);
//// 设置子控件点击监听
//        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//                switch (view.getId()) {
//                    case R.id.selectImgRiv:
//                        break;
//                }
//
//            }
//        });

        return mDetailDialog;
    }

    public void setOnConfirmListener(OnEditPersonListener onEditPersonListener) {
        this.onEditPersonListener = onEditPersonListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toAppealTv:
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
        void subNumber(List<File> files, String type);
    }
}
