package com.pro.maluli.module.myself.myAccount.appeal.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ImageEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.app.BKGSApplication;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppealAdapter extends BaseQuickAdapter<ImageEntity, BaseViewHolder> {
    List<ImageEntity> datas;

    public AppealAdapter(List<ImageEntity> data) {
        super(R.layout.item_select_image, data);
        datas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ImageEntity entity) {
        RoundedImageView roundedImageView = baseViewHolder.getView(R.id.selectImgRiv);
        ImageView deleteImg = baseViewHolder.getView(R.id.deleteImg);
        // type =0 添加图片按钮 type=1 加载本地图片
        if (entity.getType() == 0) {
            deleteImg.setVisibility(View.GONE);
            roundedImageView.setImageDrawable(BKGSApplication.getApp().getResources().getDrawable(R.mipmap.ic_add_img));
        } else {
            deleteImg.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(BKGSApplication.getApp(), entity.getUrl(), roundedImageView);
        }
    }
}
