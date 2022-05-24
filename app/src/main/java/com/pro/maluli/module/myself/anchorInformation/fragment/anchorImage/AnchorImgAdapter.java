package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.StarBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AnchorImgAdapter extends BaseQuickAdapter<AnchorInfoEntity.PictureBean,
        AnchorImgAdapter.ViewHolder> implements LoadMoreModule {
    private Context context;

    public AnchorImgAdapter(List<AnchorInfoEntity.PictureBean> data, Context context) {
        super(R.layout.item_anchorimg, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected AnchorImgAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull ViewHolder baseViewHolder, AnchorInfoEntity.PictureBean bean) {
//        GlideUtils.loadImage(context, bean.getUrl(), baseViewHolder.roundedImageView);
        Glide.with(context).load(bean.getUrl()).into(baseViewHolder.roundedImageView);
//        Glide.with(context).load(bean.getUrl()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(baseViewHolder.roundedImageView);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.anchorListRiv)
        RoundedImageView roundedImageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
