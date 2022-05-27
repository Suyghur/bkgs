package com.pro.maluli.module.home.base.classify.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassiftAdapter extends BaseQuickAdapter<HomeInfoEntity.CategoryBean.ListBean, ClassiftAdapter.ViewHolder> {
    public onItemOnclickListener onItemOnclickListener;
    private Context context;
    private int selectPosition;

    public ClassiftAdapter(List<HomeInfoEntity.CategoryBean.ListBean> data, Context context) {
        super(R.layout.item_classift, data);
        this.context = context;
    }

    public ClassiftAdapter.onItemOnclickListener getOnItemOnclickListener() {
        return onItemOnclickListener;
    }

    public void setOnItemOnclickListener(ClassiftAdapter.onItemOnclickListener onItemOnclickListener) {
        this.onItemOnclickListener = onItemOnclickListener;
    }

    @NotNull
    @Override
    protected ClassiftAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull ClassiftAdapter.ViewHolder baseViewHolder, HomeInfoEntity.CategoryBean.ListBean listBean) {
//        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.titleTv.setText(listBean.getTitle());
        baseViewHolder.mainClassiftLl.setSelected(listBean.isSelect());

    }

    public interface onItemOnclickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.mainClassiftLl)
        LinearLayout mainClassiftLl;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
