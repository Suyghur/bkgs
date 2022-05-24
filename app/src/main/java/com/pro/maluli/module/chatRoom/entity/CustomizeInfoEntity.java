package com.pro.maluli.module.chatRoom.entity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

public class CustomizeInfoEntity implements Serializable {
    private String gift_name;//  礼物名称
    private String gift_id;//  礼物id
    private String gift_logo;// 礼物logo
    private String gift_number;// 红包名称

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

    public CustomizeInfoEntity() {

    }

    public CustomizeInfoEntity(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        goJsonYes(string);

    }

    public CustomizeInfoEntity goJsonYes(String string) {
        CustomizeInfoEntity entity = new CustomizeInfoEntity();
        JSONObject jsonObjectTop = JSONObject.parseObject(string);
        int type = jsonObjectTop.getInteger("type");
        try {
            String datas = jsonObjectTop.getString("data");
            if (!TextUtils.isEmpty(datas)) {
                entity = JSONObject.parseObject(datas, CustomizeInfoEntity.class);
                entity.setType(type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setType(type);

        return entity;
    }

    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }





}
