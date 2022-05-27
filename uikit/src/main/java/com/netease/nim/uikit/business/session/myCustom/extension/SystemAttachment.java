package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONObject;

public class SystemAttachment extends CustomAttachment {

    private static final String TIPS = "tips";
    private String tips;//  提示信息

    public SystemAttachment() {
        super(CustomAttachmentType.SystemMsg);
    }

    @Override
    protected void parseData(JSONObject data) {
        tips = data.getString(TIPS);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(TIPS, tips);
        return data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
