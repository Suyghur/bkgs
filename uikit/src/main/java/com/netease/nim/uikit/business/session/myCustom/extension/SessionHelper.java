package com.netease.nim.uikit.business.session.myCustom.extension;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderGift;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderPhone;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderReservation;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderScore;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderTip;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {


    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();
    }

    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderGift.class);
        NimUIKit.registerMsgItemViewHolder(ReservationMsgAttachment.class, MsgViewHolderReservation.class);
        NimUIKit.registerMsgItemViewHolder(PhoneAttachment.class, MsgViewHolderPhone.class);
        NimUIKit.registerMsgItemViewHolder(ScoreAttachment.class, MsgViewHolderScore.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }


}
