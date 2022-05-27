package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorImgEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditImgAdapter extends BaseQuickAdapter<AnchorImgEntity.PictureBean, BaseViewHolder> {
    private Context context;
    private boolean isCanDelete;

    public EditImgAdapter(List<AnchorImgEntity.PictureBean> data, Context context) {
        super(R.layout.item_edit_img, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AnchorImgEntity.PictureBean listBean) {
        Glide.with(context).load(listBean.getUrl()).into((ImageView) baseViewHolder.getView(R.id.anchorListRiv));
//        GlideUtils.loadImage(context, listBean.getUrl(), baseViewHolder.getView(R.id.anchorListRiv));
        if (isCanDelete) {
            baseViewHolder.getView(R.id.deleteImg).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.deleteImg).setVisibility(View.GONE);
        }
    }


    public void setCanDelete(boolean isEdit) {
        isCanDelete = isEdit;
        notifyDataSetChanged();
    }
}
