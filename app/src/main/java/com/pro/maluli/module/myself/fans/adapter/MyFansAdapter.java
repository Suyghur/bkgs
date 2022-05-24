package com.pro.maluli.module.myself.fans.adapter;

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

public class MyFansAdapter extends BaseQuickAdapter<WatchListEntity.ListBean, MyFansAdapter.ViewHolder> {
    private Context context;

    @NotNull
    @Override
    protected MyFansAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public MyFansAdapter(List<WatchListEntity.ListBean> data, Context context) {
        super(R.layout.item_fans_list, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull MyFansAdapter.ViewHolder baseViewHolder, WatchListEntity.ListBean listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.userNameTv.setText(listBean.getNickname());
    }

    @OnClick(R.id.likeTv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.likeTv:

                break;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.watchCiv)
        CircleImageView watchCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
