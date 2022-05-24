package com.pro.maluli.module.myself.setting.feedback.base.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.FeedbackEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedBackAdapter extends BaseQuickAdapter<FeedbackEntity.ListBean, BaseViewHolder> {

    public FeedBackAdapter(List<FeedbackEntity.ListBean> data) {
        super(R.layout.item_feedback_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FeedbackEntity.ListBean bean) {
        baseViewHolder.setText(R.id.contentTv, bean.getContent());
        baseViewHolder.setText(R.id.feedTimeTv, bean.getCreated_at());
        baseViewHolder.setText(R.id.feedTypeTv, bean.getReport_type());
        if (bean.getStatus().equalsIgnoreCase("已回复")) {
            baseViewHolder.getView(R.id.statusTv).setVisibility(View.VISIBLE);
            if (bean.getIs_read() == 1) {
                baseViewHolder.getView(R.id.statusIv).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.statusIv).setVisibility(View.VISIBLE);
            }
        } else {
            baseViewHolder.getView(R.id.statusIv).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.statusTv).setVisibility(View.GONE);
        }
    }
}
