package com.netease.nim.uikit.business.session.viewholder;

import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.log.sdk.wrapper.NimLog;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachmentWithExtension;

import java.util.Map;

public class MsgViewHolderNotification extends MsgViewHolderBase {

    private static final String TAG = "MsgViewHolderNotification";
    protected TextView notificationTextView;

    public MsgViewHolderNotification(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.nim_message_item_notification;
    }

    @Override
    public void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(R.id.message_item_notification_label);
    }

    @Override
    public void bindContentView() {
        handleTextNotification(getDisplayText());

        //for test
        if (message != null && message.getAttachment() instanceof NotificationAttachmentWithExtension) {
            NotificationAttachmentWithExtension attachment = (NotificationAttachmentWithExtension) message.getAttachment();
            Map<String, Object> extension = attachment.getExtension();
            NimLog.i(TAG, String.format("NotificationAttachmentWithExtension extension size:%s", extension == null ? "empty" : extension.size()));

            if (extension != null) {
                for (Map.Entry<String, Object> entry : extension.entrySet()) {
                    NimLog.i(TAG, String.format("NotificationAttachmentWithExtension key:%s value:%s", entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    protected String getDisplayText() {
        return TeamNotificationHelper.getTeamNotificationText(message, message.getSessionId());
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

