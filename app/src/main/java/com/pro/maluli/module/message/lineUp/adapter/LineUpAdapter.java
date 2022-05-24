package com.pro.maluli.module.message.lineUp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.LineUpEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LineUpAdapter extends BaseQuickAdapter<LineUpEntity.ListBean, LineUpAdapter.ViewHolder> {
    private Context context;

    @NotNull
    @Override
    protected ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public LineUpAdapter(List<LineUpEntity.ListBean> data, Context context) {
        super(R.layout.item_line_up, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull ViewHolder baseViewHolder, LineUpEntity.ListBean listBean) {

        GlideUtils.loadImage(context, listBean.getAnchor().getAvatar(), baseViewHolder.userAvatarCiv);
        GlideUtils.loadImage(context, listBean.getAnchor().getLevel(), baseViewHolder.levelImg);
        baseViewHolder.userNameTv.setText(listBean.getAnchor().getNickname());
        baseViewHolder.timeTv.setText("预约时间：" + listBean.getCreated_at());
        baseViewHolder.mingciTv.setText(String.format("第%1$s名", listBean.getRand()));
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.userAvatarCiv)
        CircleImageView userAvatarCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.levelImg)
        ImageView levelImg;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.mingciTv)
        TextView mingciTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
