package com.pro.maluli.module.message.searchMessasge.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.module.message.searchMessasge.SearchContentEntity;

import org.jetbrains.annotations.NotNull;

public class UserProvide extends BaseItemProvider<SearchContentEntity> {
    private UserAdapter adapter;

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_chat_item;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, SearchContentEntity chatRoomMessage) {
        RecyclerView recyclerView = baseViewHolder.getView(R.id.msgRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (chatRoomMessage.getUserInfo() != null && chatRoomMessage.getUserInfo().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.nodataTv).setVisibility(View.GONE);
            adapter = new UserAdapter(chatRoomMessage.getUserInfo(), getContext());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                    NimUIKit.startP2PSession(getContext(), chatRoomMessage.getUserInfo().get(position).getAccount());
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
            baseViewHolder.getView(R.id.nodataTv).setVisibility(View.VISIBLE);
        }

    }


}