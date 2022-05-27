package com.pro.maluli.module.home.base.fragment.rechild.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.StarBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeLiveAdapter extends BaseQuickAdapter<LiveListEntity.DataBean.ListBean, HomeLiveAdapter.ViewHolder> implements LoadMoreModule {
    private Context context;

    public HomeLiveAdapter(List<LiveListEntity.DataBean.ListBean> data, Context context) {
        super(R.layout.item_home_live, data);
        this.context = context;
    }

    @NotNull
    @Override
    protected HomeLiveAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull ViewHolder baseViewHolder, LiveListEntity.DataBean.ListBean bean) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        baseViewHolder.linearlayout.removeAllViews();
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

        GlideUtils.loadImageNoImage(context, bean.getImage(), baseViewHolder.CoverRiv);
        GlideUtils.loadImageNoImage(context, bean.getAvatar(), baseViewHolder.anchorAvatarCiv);
        GlideUtils.loadImageNoImage(context, bean.getLevel(), baseViewHolder.levelImg);
        baseViewHolder.anchorNameTv.setText(bean.getNickname());
        baseViewHolder.statusLL.setVisibility(View.VISIBLE);

//       type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
        switch (bean.getType()) {
            case 4:
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

        }

    }

    static class ViewHolder extends BaseViewHolder {
        //        @BindView(R.id.CoverRiv)
        RoundedImageView CoverRiv;

        //        @BindView(R.id.liveTypeImg)
        ImageView liveTypeImg;

        //        @BindView(R.id.liveTypeTv)
        TextView liveTypeTv;

        //        @BindView(R.id.liveTitleTv)
        TextView liveTitleTv;

        //        @BindView(R.id.anchorAvatarCiv)
        CircleImageView anchorAvatarCiv;

        //        @BindView(R.id.anchorNameTv)
        TextView anchorNameTv;

        //        @BindView(R.id.cumulativeServiceTv)
        TextView cumulativeServiceTv;

        //        @BindView(R.id.likenumberTv)
        TextView likenumberTv;

        //        @BindView(R.id.levelImg)
        ImageView levelImg;

        //        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        //        @BindView(R.id.statusLL)
        LinearLayout statusLL;
        //        @BindView(R.id.abilityStar)
        StarBar abilityStar;


        //        @BindView(R.id.horizaotalscrollview)
        HorizontalScrollView horizaotalscrollview;

        ViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            CoverRiv = (RoundedImageView) view.findViewById(R.id.CoverRiv);
            liveTypeImg = view.findViewById(R.id.liveTypeImg);
            liveTypeTv = view.findViewById(R.id.liveTypeTv);
            liveTitleTv = view.findViewById(R.id.liveTitleTv);
            anchorAvatarCiv = view.findViewById(R.id.anchorAvatarCiv);
            anchorNameTv = view.findViewById(R.id.anchorNameTv);
            cumulativeServiceTv = view.findViewById(R.id.cumulativeServiceTv);
            likenumberTv = view.findViewById(R.id.likenumberTv);
            levelImg = view.findViewById(R.id.levelImg);
            linearlayout = view.findViewById(R.id.linearlayout);
            statusLL = view.findViewById(R.id.statusLL);
            abilityStar = view.findViewById(R.id.abilityStar);
            horizaotalscrollview = view.findViewById(R.id.horizaotalscrollview);

        }
    }
}
