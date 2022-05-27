package com.pro.maluli.common.view.dialogview.dialogAdapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.socketService.event.OnTwoOneStartEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class GuestAdapter extends BaseQuickAdapter<OnTwoOneStartEntity.SpecialListBean, GuestAdapter.ViewHolder> {
    private Context context;

    public GuestAdapter(List<OnTwoOneStartEntity.SpecialListBean> data, Context context) {
        super(R.layout.item_guest_number, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected GuestAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull GuestAdapter.ViewHolder baseViewHolder, OnTwoOneStartEntity.SpecialListBean listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.userNameTv.setText(listBean.getNickname());
    }

    @OnClick(R.id.likeTv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.likeTv:

                break;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.watchCiv)
        CircleImageView watchCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
