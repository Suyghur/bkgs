package com.pro.maluli.module.home.announcement.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.NoticeEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnouncementAdapter extends BaseQuickAdapter<NoticeEntity.ListBean, AnnouncementAdapter.ViewHolder> {
    private Context context;

    public AnnouncementAdapter(List<NoticeEntity.ListBean> data, Context context) {
        super(R.layout.item_announcement, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected AnnouncementAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull AnnouncementAdapter.ViewHolder baseViewHolder, NoticeEntity.ListBean listBean) {
        baseViewHolder.titleTv.setText(listBean.getTitle());
        baseViewHolder.timeTv.setText(listBean.getCreated_at());
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
