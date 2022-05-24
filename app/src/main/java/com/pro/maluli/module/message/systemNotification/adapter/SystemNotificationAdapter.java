package com.pro.maluli.module.message.systemNotification.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SystemNotificationAdapter extends BaseQuickAdapter<SystemMsgEntity.ListBean, SystemNotificationAdapter.ViewHolder> {
    private Context context;

    @NotNull
    @Override
    protected SystemNotificationAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public SystemNotificationAdapter(List<SystemMsgEntity.ListBean> data, Context context) {
        super(R.layout.item_system_notic, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull SystemNotificationAdapter.ViewHolder baseViewHolder, SystemMsgEntity.ListBean listBean) {
        baseViewHolder.titleTv.setText(listBean.getTitle());
        baseViewHolder.timeTv.setText(listBean.getCreated_at());
        baseViewHolder.contentTv.setText(listBean.getContent());
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

    @OnClick(R.id.likeTv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.likeTv:

                break;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.topIv)
        ImageView topIv;
        @BindView(R.id.isSeeView)
        View isSeeView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
