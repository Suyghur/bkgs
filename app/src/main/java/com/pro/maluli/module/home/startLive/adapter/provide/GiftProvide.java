package com.pro.maluli.module.home.startLive.adapter.provide;

import android.widget.ImageView;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.chatRoom.entity.CustomizeInfoEntity;

import org.jetbrains.annotations.NotNull;

public class GiftProvide extends BaseItemProvider<ChatRoomMessage> {
    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.chat_item_gift;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
        baseViewHolder.setText(R.id.nameTv, ChatRoomViewHolderHelper.getNameText(chatRoomMessage));

        HeadImageView circleImageView = baseViewHolder.getView(R.id.headCiv);
        ImageView giftImg = baseViewHolder.getView(R.id.giftImg);
        String avatar = ChatRoomViewHolderHelper.getAvatar(chatRoomMessage);
        circleImageView.loadAvatar(chatRoomMessage.getSessionId(), avatar);

        String alldata = chatRoomMessage.getAttachment().toJson(false);
        CustomizeInfoEntity entity = new CustomizeInfoEntity();
        CustomizeInfoEntity entity1 = entity.goJsonYes(alldata);
        baseViewHolder.setText(R.id.msgTv, "送出" + entity1.getGift_name());
        GlideUtils.loadImage(getContext(),entity1.getGift_logo(),giftImg);
    }
}