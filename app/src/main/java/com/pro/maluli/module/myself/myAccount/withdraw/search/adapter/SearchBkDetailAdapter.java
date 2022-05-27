package com.pro.maluli.module.myself.myAccount.withdraw.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.BKRecordEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchBkDetailAdapter extends BaseQuickAdapter<BKRecordEntity.ListBean, BaseViewHolder> {
    List<BKRecordEntity.ListBean> datas;
    private Context context;

    public SearchBkDetailAdapter(List<BKRecordEntity.ListBean> data, Context context1) {
        super(R.layout.item_bk_detai, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BKRecordEntity.ListBean entity) {
        CircleImageView avatarClv = baseViewHolder.getView(R.id.avatarClv);
        GlideUtils.loadImage(context, entity.getAvatar(), avatarClv);
        baseViewHolder.setText(R.id.nameTv, entity.getNickname());
        baseViewHolder.setText(R.id.timeTv, entity.getCreated_at());
        baseViewHolder.setText(R.id.numberBkTv, entity.getMoney() + "");
//        baseViewHolder.setText(R.id.giftTypeTv, entity.getGift_title());
//        baseViewHolder.setText(R.id.resourceTv, entity.getProduct_type() + "");
        TextView textView = baseViewHolder.getView(R.id.numberBkTv);
        //类型 0:百科充值,1:一对一直播打赏,2:私信打赏,3:对众直播打赏,4:进入付费直播间 5-支出 999-全部
        switch (entity.getFlow_type()) {
            case 0:
            case 4:
            case 5:
                baseViewHolder.getView(R.id.resourceTv).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.giftTypeTv, entity.getProduct_type());
                textView.setTextColor(context.getResources().getColor(R.color.c_ffa743));
                break;
            case 1:
            case 2:
            case 3:
                baseViewHolder.getView(R.id.resourceTv).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.giftTypeTv, entity.getGift_title());
                baseViewHolder.setText(R.id.resourceTv, entity.getProduct_type() + "");
                textView.setTextColor(context.getResources().getColor(R.color.c_8e1d77));
                break;
        }
    }
}
