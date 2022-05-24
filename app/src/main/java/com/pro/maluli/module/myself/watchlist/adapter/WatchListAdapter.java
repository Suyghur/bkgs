package com.pro.maluli.module.myself.watchlist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class WatchListAdapter extends BaseQuickAdapter<WatchListEntity.ListBean, WatchListAdapter.ViewHolder> {
    private Context context;

    @NotNull
    @Override
    protected WatchListAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public WatchListAdapter(List<WatchListEntity.ListBean> data, Context context) {
        super(R.layout.item_watch_list, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull WatchListAdapter.ViewHolder baseViewHolder, WatchListEntity.ListBean listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.userNameTv.setText(listBean.getNickname());
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.watchCiv)
        CircleImageView watchCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.likeTv)
        TextView likeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
