package com.pro.maluli.module.myself.setting.blacklist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.BlackListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base.AnchorVideoAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlackListAdapter extends BaseQuickAdapter<BlackListEntity.ListBean, BlackListAdapter.ViewHolder> {
    private Context context;

    public BlackListAdapter(List<BlackListEntity.ListBean> data, Context context) {
        super(R.layout.item_black_list, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull ViewHolder baseViewHolder, BlackListEntity.ListBean listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.blackCiv);
        baseViewHolder.timeTv.setText(listBean.getCreated_at());
        baseViewHolder.userNameTv.setText(listBean.getNickname());
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.blackCiv)
        CircleImageView blackCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.deleteBlackTv)
        TextView deleteBlackTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
