package com.pro.maluli.module.myself.setting.feedback.feedBackDetail.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.FeedBackDetailEntity;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FeedBackDetailAdapter extends BaseQuickAdapter<FeedBackDetailEntity.ReplyBean, BaseViewHolder> {
    FeedBackDetailGirdAdapter feedBackDetailGirdAdapter;
    private Context context;

    public FeedBackDetailAdapter(List<FeedBackDetailEntity.ReplyBean> data, Context context) {
        super(R.layout.item_feed_detail, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FeedBackDetailEntity.ReplyBean replyBean) {
        baseViewHolder.setText(R.id.replyTimeTv, replyBean.getCreated_at());
        baseViewHolder.setText(R.id.replyTv, replyBean.getContent());
        RecyclerView recyclerView = baseViewHolder.getView(R.id.replyXrl);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        feedBackDetailGirdAdapter = new FeedBackDetailGirdAdapter(replyBean.getImages(), context);
        recyclerView.setAdapter(feedBackDetailGirdAdapter);
        feedBackDetailGirdAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                ArrayList<String> urls = new ArrayList<>();
                for (int i = 0; i < replyBean.getImages().size(); i++) {
                    urls.add(replyBean.getImages().get(i));
                }
                CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                bigPictureDialog.setArguments(bundle);
                bigPictureDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "CheckBigPictureDialog");
            }
        });

    }
}
