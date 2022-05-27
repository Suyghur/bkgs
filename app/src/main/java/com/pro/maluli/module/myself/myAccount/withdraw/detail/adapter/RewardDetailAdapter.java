package com.pro.maluli.module.myself.myAccount.withdraw.detail.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RewardDetailAdapter extends BaseQuickAdapter<RewardDetailEntity.ListBean, BaseViewHolder> {
    List<RewardDetailEntity.ListBean> datas;
    private Context context;

    public RewardDetailAdapter(List<RewardDetailEntity.ListBean> data, Context context1) {
        super(R.layout.item_reward_detai, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RewardDetailEntity.ListBean entity) {
        CircleImageView avatarClv = baseViewHolder.getView(R.id.avatarClv);
        GlideUtils.loadImage(context, entity.getAvatar(), avatarClv);
        baseViewHolder.setText(R.id.nameTv, entity.getNickname());
        baseViewHolder.setText(R.id.timeTv, entity.getCreated_at());
//        baseViewHolder.setText(R.id.giftTypeTv, entity.getGift_title());
        baseViewHolder.setText(R.id.resourceTv, entity.getProduct_type() + "");
        TextView giftTypeTv = baseViewHolder.getView(R.id.giftTypeTv);
        if (!TextUtils.isEmpty(entity.getGift_title())) {
            giftTypeTv.setVisibility(View.VISIBLE);
            giftTypeTv.setText(entity.getGift_title());
            giftTypeTv.setPadding((int) context.getResources().getDimension(R.dimen.dp_6), 0, 0, 0);
        } else {
            giftTypeTv.setVisibility(View.GONE);
        }
        TextView textView = baseViewHolder.getView(R.id.numberBkTv);
        if (entity.getType() == 0) {//0-收入 1-提现
            baseViewHolder.setText(R.id.numberBkTv, "+" + entity.getMoney());
            textView.setTextColor(context.getResources().getColor(R.color.c_8e1d77));
        } else {
            baseViewHolder.setText(R.id.numberBkTv, "-" + entity.getMoney());
            textView.setTextColor(context.getResources().getColor(R.color.c_ffa743));
        }
    }
}
