package com.pro.maluli.common.view.dialogview.checkMsg;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckMsgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    List<String> datas;
    private Context context;

    public CheckMsgAdapter(List<String> data1, Context context1) {
        super(R.layout.item_check_msg, data1);
        datas = data1;
        this.context = context1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String entity) {
        RoundedImageView roundedImageView = baseViewHolder.getView(R.id.selectImgRiv);
        // type =0 添加图片按钮 type=1 加载本地图片
        GlideUtils.loadImage(context, entity, roundedImageView);
    }
}
