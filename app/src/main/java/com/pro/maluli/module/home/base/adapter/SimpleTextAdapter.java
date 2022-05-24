package com.pro.maluli.module.home.base.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pro.maluli.R;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;

import java.util.List;

public class SimpleTextAdapter extends CommonAdapter<HomeInfoEntity.NoticeBean> {
    public SimpleTextAdapter(Context context, List<HomeInfoEntity.NoticeBean> datas) {
        super(context, R.layout.item_simple_text, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HomeInfoEntity.NoticeBean item, int position) {
        TextView textTv = viewHolder.getView(R.id.textTv);
        TextView newTv = viewHolder.getView(R.id.newTv);
        textTv.setText(item.getTitle());
        if (position==0){
            newTv.setVisibility(View.VISIBLE);
        }else {
            newTv.setVisibility(View.GONE);

        }
    }

}
