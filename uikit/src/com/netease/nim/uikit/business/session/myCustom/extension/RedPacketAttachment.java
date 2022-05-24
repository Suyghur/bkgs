package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONObject;

public class RedPacketAttachment extends CustomAttachment {

    private String gift_name;//  礼物名称
    private String gift_id;//  礼物id
    private String gift_logo;// 礼物logo
    private String gift_number;// 红包名称

    private static final String KEY_NAME = "gift_name";
    private static final String KEY_ID = "gift_id";
    private static final String KEY_LOGO = "gift_logo";
    private static final String KEY_NUMBER = "gift_number";

    public RedPacketAttachment() {
        super(CustomAttachmentType.RedPacket);
    }

    @Override
    protected void parseData(JSONObject data) {
        gift_name = data.getString(KEY_NAME);
        gift_id = data.getString(KEY_ID);
        gift_logo = data.getString(KEY_LOGO);
        gift_number = data.getString(KEY_NUMBER);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_NAME, gift_name);
        data.put(KEY_ID, gift_id);
        data.put(KEY_LOGO, gift_logo);
        data.put(KEY_NUMBER, gift_number);
        return data;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_logo() {
        return gift_logo;
    }

    public void setGift_logo(String gift_logo) {
        this.gift_logo = gift_logo;
    }

    public String getGift_number() {
        return gift_number;
    }

    public void setGift_number(String gift_number) {
        this.gift_number = gift_number;
    }
}
