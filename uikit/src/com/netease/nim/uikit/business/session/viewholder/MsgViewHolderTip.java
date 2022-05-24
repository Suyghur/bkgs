package com.netease.nim.uikit.business.session.viewholder;

import android.graphics.Color;
import android.icu.util.TimeUnit;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.util.TimeUtils;

import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by huangjun on 2015/11/25.
 * Tip类型消息ViewHolder
 */
public class MsgViewHolderTip extends MsgViewHolderBase {

    protected TextView notificationTextView;

    public MsgViewHolderTip(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_notification;
    }

    @Override
    public void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_notification_label);
    }

    @Override
    public void bindContentView() {
        String text = "未知通知提醒";
        if (TextUtils.isEmpty(message.getContent())) {
            Map<String, Object> content = message.getRemoteExtension();
            if (content != null && !content.isEmpty()) {
                text = (String) content.get("content");
            }
        } else {
            text = message.getContent();
        }
        String link = "";
        int type = 1;
        Map<String, Object> remote = message.getRemoteExtension();
        if (remote != null && !remote.isEmpty()) {
            link = (String) remote.get("link");
            try {
                type = (int) remote.get("type");
            } catch (Exception e) {

            }
        }
        if (!TextUtils.isEmpty(link) && message.getStatus() != MsgStatusEnum.draft) {
            MsgDirectionEnum direct = message.getDirect();
            if (TimeUtil.isToday(message.getTime()) && direct != MsgDirectionEnum.Out) {
                notificationTextView.setTextColor(Color.parseColor("#ff00ff"));
            }
        } else {
            notificationTextView.setTextColor(Color.parseColor("#ffffff"));
        }

        handleTextNotification(text);
        String finalLink = link;
        int finalType = type;
        notificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MsgDirectionEnum direct = message.getDirect();
                if (direct == MsgDirectionEnum.Out || finalType == 2) {
                    return;
                }
                if (TextUtils.isEmpty(finalLink)) {
                    return;
                }
                message.setStatus(MsgStatusEnum.draft);
                NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                GoSettingEvent goSettingEvent = new GoSettingEvent(null);
                goSettingEvent.setLink(finalLink);
                EventBus.getDefault().post(goSettingEvent);
                notificationTextView.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
