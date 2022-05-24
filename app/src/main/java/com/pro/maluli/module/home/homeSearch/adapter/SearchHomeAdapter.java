package com.pro.maluli.module.home.homeSearch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.StarBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchHomeAdapter extends BaseQuickAdapter<LiveListEntity.DataBean.ListBean, SearchHomeAdapter.ViewHolder> {
    List<LiveListEntity.DataBean.ListBean> datas;
    private Context context;

    public SearchHomeAdapter(List<LiveListEntity.DataBean.ListBean> data, Context context1) {
        super(R.layout.item_home_live, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull SearchHomeAdapter.ViewHolder baseViewHolder, LiveListEntity.DataBean.ListBean bean) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < bean.getTags().size(); i++) {
            TextView textView = new TextView(context);
            textView.setText(bean.getTags().get(i));
            textView.setBackground(context.getResources().getDrawable(R.drawable.shape_f9f9f9_3));
            lp.rightMargin = (int) context.getResources().getDimension(R.dimen.dp_10);
            textView.setLayoutParams(lp);
            textView.setPadding(5, 5, 5, 5);
            baseViewHolder.linearlayout.addView(textView);
        }
        baseViewHolder.liveTitleTv.setText(bean.getTitle());
        baseViewHolder.cumulativeServiceTv.setText(String.valueOf(bean.getJoin_one_num()));
        baseViewHolder.likenumberTv.setText(String.valueOf(bean.getMonth_spend_money()));

//        baseViewHolder.abilityStar.setStarMark(bean.getStart());
        baseViewHolder.abilityStar.setStarCount(bean.getStart() == 0 ? 1 : bean.getStart());

        GlideUtils.loadImage(context,bean.getImage(),baseViewHolder.CoverRiv);
        GlideUtils.loadImage(context,bean.getAvatar(),baseViewHolder.anchorAvatarCiv);
        GlideUtils.loadImage(context,bean.getLevel(),baseViewHolder.levelImg);
        baseViewHolder.anchorNameTv.setText(bean.getNickname());


//       type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
        switch (bean.getType()){
            case 0:
                baseViewHolder.statusLL.setVisibility(View.GONE);
                break;
            case 1:
                baseViewHolder.liveTypeTv.setText("一对一直播");
                baseViewHolder.liveTypeImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_one_to_one));
                break;
            case 2:
                baseViewHolder.liveTypeTv.setText("对众直播");
                baseViewHolder.liveTypeImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_one_to_more));
                break;
            case 3:
                baseViewHolder.liveTypeTv.setText("接受预约");
                baseViewHolder.liveTypeImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_yu_yue));
                break;
            case 4:
                break;
        }

    }
    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.CoverRiv)
        RoundedImageView CoverRiv;
        @BindView(R.id.statusLL)
        LinearLayout statusLL;

        @BindView(R.id.liveTypeImg)
        ImageView liveTypeImg;

        @BindView(R.id.liveTypeTv)
        TextView liveTypeTv;

        @BindView(R.id.liveTitleTv)
        TextView liveTitleTv;

        @BindView(R.id.anchorAvatarCiv)
        CircleImageView anchorAvatarCiv;

        @BindView(R.id.anchorNameTv)
        TextView anchorNameTv;

        @BindView(R.id.cumulativeServiceTv)
        TextView cumulativeServiceTv;

        @BindView(R.id.likenumberTv)
        TextView likenumberTv;

        @BindView(R.id.levelImg)
        ImageView levelImg;

        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        @BindView(R.id.abilityStar)
        StarBar abilityStar;


        @BindView(R.id.horizaotalscrollview)
        HorizontalScrollView horizaotalscrollview;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
