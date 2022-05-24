package com.pro.maluli.module.message.reserveMessage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReserveMessageAdapter extends BaseQuickAdapter<ReserveMessageEntity.ListBean, ReserveMessageAdapter.ViewHolder> {
    private Context context;

    @NotNull
    @Override
    protected ReserveMessageAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public ReserveMessageAdapter(List<ReserveMessageEntity.ListBean> data, Context context) {
        super(R.layout.item_reserve_message, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull ReserveMessageAdapter.ViewHolder baseViewHolder, ReserveMessageEntity.ListBean listBean) {
        baseViewHolder.titleTv.setText("预约通知");
        baseViewHolder.timeTv.setText(listBean.getCreated_at());
        baseViewHolder.userNameTv.setText(listBean.getTitle());
        GlideUtils.loadImageNoImage(context, listBean.getAvatar(), baseViewHolder.userAvatarCiv);

        if (listBean.getIs_top() == 1) {
            baseViewHolder.topIv.setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.topIv.setVisibility(View.GONE);
        }
        if (listBean.getStatus() == 0) {
            baseViewHolder.isSeeView.setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.isSeeView.setVisibility(View.GONE);

        }
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.topIv)
        ImageView topIv;
        @BindView(R.id.userAvatarCiv)
        CircleImageView userAvatarCiv;
        @BindView(R.id.isSeeView)
        View isSeeView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
