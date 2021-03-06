package com.pro.maluli.module.home.base.classify.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassiftChildAdapter extends BaseQuickAdapter<HomeInfoEntity.CategoryBean.ListBean.ChildBean, ClassiftChildAdapter.ViewHolder> {
    private Context context;

    public ClassiftChildAdapter(List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> data, Context context) {
        super(R.layout.item_classift_child, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected ClassiftChildAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull ClassiftChildAdapter.ViewHolder baseViewHolder, HomeInfoEntity.CategoryBean.ListBean.ChildBean listBean) {
        baseViewHolder.titleTv.setText(listBean.getTitle());
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.titleTv)
        TextView titleTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
