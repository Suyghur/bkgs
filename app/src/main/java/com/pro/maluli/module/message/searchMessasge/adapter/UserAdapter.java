package com.pro.maluli.module.message.searchMessasge.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends BaseQuickAdapter<NimUserInfo, UserAdapter.ViewHolder> {
    List<NimUserInfo> datas;
    private Context context;

    public UserAdapter(List<NimUserInfo> data, Context context1) {
        super(R.layout.item_user_search, data);
        datas = data;
        context = context1;
    }

    @Override
    protected void convert(@NotNull UserAdapter.ViewHolder baseViewHolder, NimUserInfo bean) {

        HeadImageView circleImageView = baseViewHolder.getView(R.id.img_head);
//        circleImageView.loadBuddyAvatar(bean);
        GlideUtils.loadImageNoImage(getContext(), bean.getAvatar(), circleImageView);
        baseViewHolder.tv_nickname.setText(bean.getName());


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_head)
        HeadImageView img_head;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}