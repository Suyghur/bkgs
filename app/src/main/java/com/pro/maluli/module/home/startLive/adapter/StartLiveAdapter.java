package com.pro.maluli.module.home.startLive.adapter;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.pro.maluli.module.home.startLive.adapter.provide.AudioProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.GiftProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.NotificationProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.SystemProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.TextProvide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StartLiveAdapter extends BaseProviderMultiAdapter<ChatRoomMessage> {

    public StartLiveAdapter() {
        super();
        // 注册 Provider
        addItemProvider(new TextProvide());
        addItemProvider(new AudioProvide());
        addItemProvider(new GiftProvide());
        addItemProvider(new SystemProvide());
        addItemProvider(new NotificationProvide());
    }

    @Override
    protected int getItemType(@NotNull List<? extends ChatRoomMessage> list, int i) {
        MsgTypeEnum msgType = list.get(i).getMsgType();
        if (msgType == MsgTypeEnum.text) {
            return 0;
        } else if (msgType == MsgTypeEnum.audio) {
            return 1;
        } else if (msgType == MsgTypeEnum.custom) {
            try {
                String alldata = list.get(i).getAttachment().toJson(false);
                JSONObject jsonObjectTop = JSONObject.parseObject(alldata);
                int type = jsonObjectTop.getInteger("type");
                switch (type) {
                    case 5:
                        return 2;
                    case 17:
                        return 3;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (msgType == MsgTypeEnum.notification) {
            return 4;
        }
        return 0;
    }
}
