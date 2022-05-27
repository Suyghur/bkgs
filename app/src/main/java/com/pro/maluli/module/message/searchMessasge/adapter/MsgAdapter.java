package com.pro.maluli.module.message.searchMessasge.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgAdapter extends BaseQuickAdapter<IMMessage, MsgAdapter.ViewHolder> {
    List<IMMessage> datas;
    private Context context;

    public MsgAdapter(List<IMMessage> data, Context context1) {
        super(R.layout.item_msg_search, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull MsgAdapter.ViewHolder baseViewHolder, IMMessage bean) {

        HeadImageView circleImageView = baseViewHolder.getView(R.id.img_head);
        circleImageView.loadBuddyAvatar(bean);
        baseViewHolder.tv_nickname.setText(bean.getFromNick());
        baseViewHolder.tv_message.setText(bean.getContent());
        baseViewHolder.tv_date_time.setText(TimeUtils.millis2String(bean.getTime()));


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_head)
        HeadImageView img_head;


        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_message)
        TextView tv_message;


        @BindView(R.id.tv_date_time)
        TextView tv_date_time;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}