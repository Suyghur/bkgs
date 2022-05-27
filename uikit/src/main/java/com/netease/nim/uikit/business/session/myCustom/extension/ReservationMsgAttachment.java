package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ReservationMsgAttachment extends CustomAttachment {

    private static final String KEY_NAME = "reservation_message";
    private static final String KEY_ID = "imgs";
    private String reservation_message;//  礼物名称
    private List<String> imgs;//  礼物id

    public ReservationMsgAttachment() {
        super(CustomAttachmentType.subscribe);
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    @Override
    protected void parseData(JSONObject data) {
        reservation_message = data.getString(KEY_NAME);
//        imgs= (List<String>) data.getJSONArray(KEY_ID);
        JSONArray jsonArray = data.getJSONArray(KEY_ID);
        imgs = jsonArray.toJavaList(String.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_NAME, reservation_message);
        JSONArray jsonObj = (JSONArray) JSONArray.toJSON(imgs);
        data.put(KEY_ID, jsonObj);
        return data;
    }

    public String getReservation_message() {
        return reservation_message;
    }

    public void setReservation_message(String reservation_message) {
        this.reservation_message = reservation_message;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
