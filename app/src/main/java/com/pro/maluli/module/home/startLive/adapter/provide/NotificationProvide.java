package com.pro.maluli.module.home.startLive.adapter.provide;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.business.chatroom.helper.ChatRoomNotificationHelper;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.pro.maluli.R;

import org.jetbrains.annotations.NotNull;

public class NotificationProvide extends BaseItemProvider<ChatRoomMessage> {
    @Override
    public int getItemViewType() {
        return 4;
    }

    @Override
    public int getLayoutId() {
        return R.layout.chat_item_notification;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
        String content = ChatRoomNotificationHelper.getNotificationText((ChatRoomNotificationAttachment) chatRoomMessage.getAttachment());
        TextView tvContent = baseViewHolder.getView(R.id.msgTv);
        tvContent.setText(content);

//        String alldata = chatRoomMessage.getAttachment().toJson(false);
//        JSONObject jsonObjectTop = JSONObject.parseObject(alldata);
//        JSONObject jsonObject = jsonObjectTop.getJSONObject("data");
//        String tips = "系统通知：" + jsonObject.getString("tips");
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//        ssb.append(tips);
//        ssb.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setColor(Color.parseColor("#ffa743"));
//                ds.setUnderlineText(false);
//            }
//        }, 0, 5, 0);
//
//        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
//        tvContent.setText(ssb, TextView.BufferType.SPANNABLE);
    }

}