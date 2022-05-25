package com.netease.nim.uikit.business.session.myCustom.extension;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.CreateMessageCallback;
import com.netease.nim.uikit.api.model.recent.RecentCustomization;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.helper.MessageHelper;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nim.uikit.business.session.module.IMultiRetweetMsgCreator;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderGift;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderPhone;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderReservation;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderScore;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderTip;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderUnknown;
import com.netease.nim.uikit.business.team.model.TeamExtras;
import com.netease.nim.uikit.business.team.model.TeamRequestCode;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nim.uikit.impl.customization.DefaultRecentCustomization;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

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
