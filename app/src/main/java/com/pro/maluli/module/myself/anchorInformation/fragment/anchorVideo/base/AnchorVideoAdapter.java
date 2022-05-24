package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.transition.Hold;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.AnchorVideoEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnchorVideoAdapter extends BaseQuickAdapter<AnchorInfoEntity.VideoBean,
        AnchorVideoAdapter.ViewHolder> implements LoadMoreModule {
    private Context context;
    public HashMap<Integer, Boolean> SelectedMap;
    List<AnchorInfoEntity.VideoBean> videoBeans;
    private boolean isShowSelect;

    public HashMap<Integer, Boolean> getSelectedMap() {
        return SelectedMap;
    }

    public void setSelectedMap(HashMap<Integer, Boolean> selectedMap) {
        SelectedMap = selectedMap;
    }

    public boolean isShowSelect() {
        return isShowSelect;
    }

    public void setShowSelect(boolean showSelect) {
        isShowSelect = showSelect;

//        notifyDataSetChanged();

    }

    public AnchorVideoAdapter(List<AnchorInfoEntity.VideoBean> data, Context context) {
        super(R.layout.item_info_video, data);
        this.context = context;
        this.videoBeans = data;
        SelectedMap = new HashMap<>();
    }

    @NotNull
    @Override
    protected ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        holder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelectedMap.put(videoBeans.get(position).getId(), isChecked);
            }
        });
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(@NotNull ViewHolder baseViewHolder, AnchorInfoEntity.VideoBean bean) {
//        GlideUtils.loadImage(context, bean.getCover(), baseViewHolder.roundedImageView);
        Glide.with(context).load(bean.getCover()).into(baseViewHolder.roundedImageView);
        if (isShowSelect) {
            baseViewHolder.selectCheckBox.setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.selectCheckBox.setVisibility(View.GONE);
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.anchorListRiv)
        RoundedImageView roundedImageView;
        @BindView(R.id.selectCheckBox)
        CheckBox selectCheckBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
