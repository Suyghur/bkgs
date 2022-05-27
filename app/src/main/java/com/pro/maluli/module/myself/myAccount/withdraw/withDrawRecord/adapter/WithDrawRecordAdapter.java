package com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.WithDrawRecordEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WithDrawRecordAdapter extends BaseQuickAdapter<WithDrawRecordEntity.ListBean, BaseViewHolder> {
    List<WithDrawRecordEntity.ListBean> datas;
    private Context context;

    public WithDrawRecordAdapter(List<WithDrawRecordEntity.ListBean> data, Context context1) {
        super(R.layout.item_withdraw_record, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, WithDrawRecordEntity.ListBean entity) {
        baseViewHolder.setText(R.id.nameTv, entity.getAlipay_account());
        baseViewHolder.setText(R.id.timeTv, entity.getCreated_at());
        baseViewHolder.setText(R.id.numberBkTv, "提现金额：" + entity.getMoney());
        baseViewHolder.setText(R.id.statusTv, entity.getStatus());
//        baseViewHolder.setText(R.id.resourceTv, entity.getProduct_type() + "");
        TextView resourceTv = baseViewHolder.getView(R.id.resourceTv);
        TextView statusTv = baseViewHolder.getView(R.id.statusTv);
        TextView contentTv = baseViewHolder.getView(R.id.contentTv);
        if (!TextUtils.isEmpty(entity.getContent())) {
            contentTv.setText(entity.getContent());
        }
        //类型 0- 通过 1-审核中 2-失败
        switch (entity.getType()) {
            case 0:
                resourceTv.setVisibility(View.GONE);
                statusTv.setTextColor(context.getResources().getColor(R.color.c_8e1d77));
                break;
            case 1:
                statusTv.setTextColor(context.getResources().getColor(R.color.c_ffa743));
                resourceTv.setVisibility(View.GONE);
                break;
            case 2:
                statusTv.setTextColor(context.getResources().getColor(R.color.c_FC595E));
                resourceTv.setVisibility(View.VISIBLE);
                break;
        }
        resourceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resourceTv.getText().toString().equalsIgnoreCase("展开原因")) {
                    resourceTv.setText("收起原因");
                    contentTv.setVisibility(View.VISIBLE);
                } else {
                    resourceTv.setText("展开原因");
                    contentTv.setVisibility(View.GONE);
                }
            }
        });
    }
}
