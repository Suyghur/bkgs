package com.pro.maluli.module.home.previewLive.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewLiveAdapter extends BaseQuickAdapter<AnchorInfoEntity.PictureBean, PreviewLiveAdapter.ViewHolder> {
    public OnItemImgListener itemImgListener;
    private Context context;

    public PreviewLiveAdapter(List<AnchorInfoEntity.PictureBean> data, Context context) {
        super(R.layout.item_picture, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected PreviewLiveAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public OnItemImgListener getItemImgListener() {
        return itemImgListener;
    }

    public void setItemImgListener(OnItemImgListener itemImgListener) {
        this.itemImgListener = itemImgListener;
    }

    @Override
    protected void convert(@NotNull PreviewLiveAdapter.ViewHolder baseViewHolder, AnchorInfoEntity.PictureBean listBean) {
        GlideUtils.loadImage(context, listBean.getUrl(), baseViewHolder.anchorListRiv);
        if (getItemPosition(listBean) == getItemCount() - 1) {
            baseViewHolder.lineVeiw.setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.lineVeiw.setVisibility(View.GONE);
        }
    }

    public interface OnItemImgListener {
        void seeImg(String url);
    }

//    @OnClick(R.id.likeTv)
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.likeTv:
//
//                break;
//        }
//    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.anchorListRiv)
        RoundedImageView anchorListRiv;
        @BindView(R.id.lineVeiw)
        View lineVeiw;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
