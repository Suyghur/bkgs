package com.pro.maluli.common.view.dialogview.dialogAdapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.LianmaiEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LianmaiAdapter extends BaseQuickAdapter<ChatRoomMember, LianmaiAdapter.ViewHolder> {
    private List<LianmaiEntity> entitiesList;
    private Context context;
    private String anchorAccid;

    public LianmaiAdapter(List<ChatRoomMember> data, String anchorAccid, Context context) {
        super(R.layout.item_lianmai, data);
        this.context = context;
        this.anchorAccid = anchorAccid;
    }

    public LianmaiAdapter(List<ChatRoomMember> data, String anchorAccid, Context context, List<LianmaiEntity> entities) {
        super(R.layout.item_lianmai, data);
        this.context = context;
        this.anchorAccid = anchorAccid;
        this.entitiesList = entities;
    }

    @NotNull
    @Override
    protected LianmaiAdapter.ViewHolder createBaseViewHolder(@NotNull View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void convert(@NotNull LianmaiAdapter.ViewHolder baseViewHolder, ChatRoomMember listBean) {
        GlideUtils.loadImage(context, listBean.getAvatar(), baseViewHolder.watchCiv);
        baseViewHolder.userNameTv.setText(listBean.getNick());
        if (listBean.getAccount().equalsIgnoreCase(anchorAccid)) {
            baseViewHolder.statusTv.setVisibility(View.GONE);
        } else {
            baseViewHolder.statusTv.setVisibility(View.VISIBLE);
        }
        boolean isLianmai = false;
        if (entitiesList != null) {
            //判断是否有连麦
            for (int i = 0; i < entitiesList.size(); i++) {
                if (listBean.getAccount().equalsIgnoreCase(entitiesList.get(i).getAccid())) {
                    isLianmai = true;
                }
            }
            if (isLianmai) {
                baseViewHolder.statusTv.setVisibility(View.VISIBLE);
                baseViewHolder.statusTv.setText("同意连麦");
                baseViewHolder.statusTv.setSelected(true);
            } else {
                baseViewHolder.statusTv.setVisibility(View.GONE);
                baseViewHolder.statusTv.setSelected(false);
            }
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
