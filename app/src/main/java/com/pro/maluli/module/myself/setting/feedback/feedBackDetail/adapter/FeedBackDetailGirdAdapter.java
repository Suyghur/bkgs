package com.pro.maluli.module.myself.setting.feedback.feedBackDetail.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedBackDetailGirdAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public FeedBackDetailGirdAdapter(List<String> data, Context context) {
        super(R.layout.item_fed_gird, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        GlideUtils.loadImage(context, s, baseViewHolder.getView(R.id.selectImgRiv));
    }
}
