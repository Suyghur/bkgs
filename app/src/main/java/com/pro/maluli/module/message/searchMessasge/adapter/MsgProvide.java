package com.pro.maluli.module.message.searchMessasge.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.api.NimUIKit;
import com.pro.maluli.R;
import com.pro.maluli.module.message.searchMessasge.SearchContentEntity;

import org.jetbrains.annotations.NotNull;

public class MsgProvide extends BaseItemProvider<SearchContentEntity> {
    MsgAdapter msgAdapter;

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.search_chat_item;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, SearchContentEntity chatRoomMessage) {
        RecyclerView recyclerView = baseViewHolder.getView(R.id.msgRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (chatRoomMessage.getMessage() != null && chatRoomMessage.getMessage().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.nodataTv).setVisibility(View.GONE);
            msgAdapter = new MsgAdapter(chatRoomMessage.getMessage(), getContext());
            recyclerView.setAdapter(msgAdapter);
            msgAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                    NimUIKit.startP2PSession(getContext(),
                            chatRoomMessage.getMessage().get(position).getSessionId(), chatRoomMessage.getMessage().get(position));
                }
            });

        } else {
            recyclerView.setVisibility(View.GONE);
            baseViewHolder.getView(R.id.nodataTv).setVisibility(View.VISIBLE);
        }
    }


}