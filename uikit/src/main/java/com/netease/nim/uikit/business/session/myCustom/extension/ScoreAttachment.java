package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONObject;

public class ScoreAttachment extends CustomAttachment {

    private static final String KEY_ICON = "icon";
    private static final String KEY_MSG = "msg";
    private String icon;//  礼物名称
    private String msg;//  礼物id

    public ScoreAttachment() {
        super(CustomAttachmentType.score);
    }

    @Override
    protected void parseData(JSONObject data) {
        icon = data.getString(KEY_ICON);
        msg = data.getString(KEY_MSG);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_ICON, icon);
        data.put(KEY_MSG, msg);
        return data;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
