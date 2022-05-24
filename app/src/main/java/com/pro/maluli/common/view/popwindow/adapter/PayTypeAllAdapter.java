package com.pro.maluli.common.view.popwindow.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.RechargeEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PayTypeAllAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    List<String> datas;

    public PayTypeAllAdapter(List<String> data) {
        super(R.layout.popup_text_item, data);
        datas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String entity) {
        baseViewHolder.setText(R.id.contentTv, entity);
    }
}
