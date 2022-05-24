package com.pro.maluli.module.myself.myAccount.recharge.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.PayInfoEntity;
import com.pro.maluli.common.entity.RechargeEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RechargePayTypeAdapter extends BaseQuickAdapter<RechargeEntity.PayBean, BaseViewHolder> {
    List<RechargeEntity.PayBean> datas;

    public RechargePayTypeAdapter(List<RechargeEntity.PayBean> data) {
        super(R.layout.item_recharge_pay_type, data);
        datas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RechargeEntity.PayBean entity) {
        if (entity.isSelect()) {
            baseViewHolder.getView(R.id.payTypeLL).setSelected(true);
        } else {
            baseViewHolder.getView(R.id.payTypeLL).setSelected(false);
        }
//        baseViewHolder.getView(R.id.payTypeLL).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i <datas.size() ; i++) {
//                    datas.get(i).setSelect(false);
//                }
//                    baseViewHolder.getView(R.id.payTypeLL).setSelected(true);
//
//            }
//        });
        baseViewHolder.setText(R.id.bKTv, entity.getBk_money() + "高手币");
        baseViewHolder.setText(R.id.moneyTv, entity.getMoney()+"");
    }
}
