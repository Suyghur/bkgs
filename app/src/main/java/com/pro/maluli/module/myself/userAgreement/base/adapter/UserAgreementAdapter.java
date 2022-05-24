package com.pro.maluli.module.myself.userAgreement.base.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ProtocolEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAgreementAdapter extends BaseQuickAdapter<ProtocolEntity.ListBean, BaseViewHolder> {

    public UserAgreementAdapter(List<ProtocolEntity.ListBean> data) {
        super(R.layout.item_protocol, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ProtocolEntity.ListBean listBean) {
        baseViewHolder.setText(R.id.titleTv, listBean.getTitle());
    }
}
