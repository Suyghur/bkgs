package com.pro.maluli.module.home.startLive.adapter.provide;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.app.MyAppliaction;
import com.pro.maluli.module.chatRoom.entity.CustomizeInfoEntity;
import com.pro.maluli.module.main.base.MainActivity;

import org.jetbrains.annotations.NotNull;

public class SystemProvide extends BaseItemProvider<ChatRoomMessage> {
    @Override
    public int getItemViewType() {
        return 3;
    }

    @Override
    public int getLayoutId() {
        return R.layout.chat_item_system;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {

        String alldata = chatRoomMessage.getAttachment().toJson(false);
        JSONObject jsonObjectTop = JSONObject.parseObject(alldata);
        JSONObject jsonObject = jsonObjectTop.getJSONObject("data");
        String tips = "系统通知："+jsonObject.getString("tips");
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tips);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#ffa743"));
                ds.setUnderlineText(false);
            }
        }, 0, 5, 0);
        TextView tvContent = baseViewHolder.getView(R.id.msgTv);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}