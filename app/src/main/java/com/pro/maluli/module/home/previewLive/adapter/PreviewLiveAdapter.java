package com.pro.maluli.module.home.previewLive.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ClickUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewLiveAdapter extends BaseQuickAdapter<AnchorInfoEntity.PictureBean, PreviewLiveAdapter.ViewHolder> {
    private Context context;


    @NotNull
    @Override
    protected PreviewLiveAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public PreviewLiveAdapter(List<AnchorInfoEntity.PictureBean> data, Context context) {
        super(R.layout.item_picture, data);
        this.context = context;
    }

    public interface OnItemImgListener {
        void seeImg(String url);
    }

    public OnItemImgListener itemImgListener;

    public OnItemImgListener getItemImgListener() {
        return itemImgListener;
    }

    public void setItemImgListener(OnItemImgListener itemImgListener) {
        this.itemImgListener = itemImgListener;
    }

    @Override
    protected void convert(@NotNull PreviewLiveAdapter.ViewHolder baseViewHolder, AnchorInfoEntity.PictureBean listBean) {
            GlideUtils.loadImage(context, listBean.getUrl(), baseViewHolder.anchorListRiv);
            if (getItemPosition(listBean)==getItemCount()-1){
                baseViewHolder.lineVeiw.setVisibility(View.VISIBLE);
            }else {
                baseViewHolder.lineVeiw.setVisibility(View.GONE);
            }
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
