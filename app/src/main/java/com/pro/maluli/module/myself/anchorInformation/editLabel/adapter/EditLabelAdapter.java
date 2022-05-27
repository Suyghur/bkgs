package com.pro.maluli.module.myself.anchorInformation.editLabel.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorLabelEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditLabelAdapter extends BaseQuickAdapter<AnchorLabelEntity.TagsBean, BaseViewHolder> {
    private Context context;
    private boolean isCanDelete;

    public EditLabelAdapter(List<AnchorLabelEntity.TagsBean> data, Context context) {
        super(R.layout.item_edit_label, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AnchorLabelEntity.TagsBean listBean) {
        baseViewHolder.setText(R.id.labelTv, listBean.getTag_name());
        if (isCanDelete) {
            baseViewHolder.getView(R.id.deleteLabelIv).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.deleteLabelIv).setVisibility(View.GONE);
        }
    }


    public void setCanDelete(boolean isEdit) {
        isCanDelete = isEdit;
        notifyDataSetChanged();
    }
}
