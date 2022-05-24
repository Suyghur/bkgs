package com.pro.maluli.common.view.dialogview.dialogAdapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineMemberAdapter extends BaseQuickAdapter<ChatRoomMember, OnlineMemberAdapter.ViewHolder> {
    private Context context;
    private String anchorAccid;

    @NotNull
    @Override
    protected OnlineMemberAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    public OnlineMemberAdapter(List<ChatRoomMember> data,String anchorAccid, Context context) {
        super(R.layout.item_online_number, data);
        this.context = context;
        this.anchorAccid =anchorAccid;
    }

    @Override
    protected void convert(@NotNull OnlineMemberAdapter.ViewHolder baseViewHolder, ChatRoomMember listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.userNameTv.setText(listBean.getNick());
        if (listBean.isMuted()) {
            baseViewHolder.statusTv.setSelected(true);
            baseViewHolder.statusTv.setText("解禁");
        } else {
            baseViewHolder.statusTv.setSelected(false);
            baseViewHolder.statusTv.setText("禁言");
        }
        if (listBean.getAccount().equalsIgnoreCase(anchorAccid)) {
            baseViewHolder.statusTv.setVisibility(View.GONE);
        } else {
            baseViewHolder.statusTv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.likeTv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.likeTv:

                break;
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.watchCiv)
        CircleImageView watchCiv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.statusTv)
        TextView statusTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
