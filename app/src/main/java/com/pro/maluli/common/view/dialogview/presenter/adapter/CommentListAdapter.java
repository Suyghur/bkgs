package com.pro.maluli.common.view.dialogview.presenter.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.CommentVideoEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends BaseQuickAdapter<CommentVideoEntity.ListBean, BaseViewHolder> implements LoadMoreModule, UpFetchModule {
    private Context context;

    public CommentListAdapter(List<CommentVideoEntity.ListBean> data, Context context) {
        super(R.layout.item_comment, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CommentVideoEntity.ListBean listBean) {
//        baseViewHolder.setText(R.id.titleTv, listBean.getTitle());
        CircleImageView avaterIv = baseViewHolder.getView(R.id.avaterIv);
        TextView nameTv = baseViewHolder.getView(R.id.nameTv);
        TextView commentContentTv = baseViewHolder.getView(R.id.commentContentTv);
        TextView timeTv = baseViewHolder.getView(R.id.timeTv);
        GlideUtils.loadImage(context, listBean.getMember().getAvatar(), avaterIv);
        nameTv.setText(listBean.getMember().getNickname());
        commentContentTv.setText(listBean.getComment());
        timeTv.setText(listBean.getCreated_at());

    }
}
