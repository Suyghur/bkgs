package com.pro.maluli.module.home.startLive.adapter.provide;

import android.text.TextUtils;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;

import org.jetbrains.annotations.NotNull;

public class TextProvide extends BaseItemProvider<ChatRoomMessage> {
    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.chat_item_text;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
        baseViewHolder.setText(R.id.nameTv, ChatRoomViewHolderHelper.getNameText(chatRoomMessage));
        baseViewHolder.setText(R.id.msgTv, chatRoomMessage.getContent());
        HeadImageView circleImageView = baseViewHolder.getView(R.id.headCiv);
        String avatar = ChatRoomViewHolderHelper.getAvatar(chatRoomMessage);
        circleImageView.loadAvatar(chatRoomMessage.getSessionId(), avatar);
        if (TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(chatRoomMessage))) {
            UserInfoEntity entity = (UserInfoEntity) ACache.get(getContext()).getAsObject(ACEConstant.USERINFO);
            if (entity != null) {
                baseViewHolder.setText(R.id.nameTv, entity.getNickname());
                circleImageView.loadAvatar(chatRoomMessage.getSessionId(), entity.getAvatar());
            }
        }

    }


}