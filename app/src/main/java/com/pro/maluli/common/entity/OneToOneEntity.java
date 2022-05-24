package com.pro.maluli.common.entity;

import java.io.Serializable;

public class OneToOneEntity implements Serializable {

    private String status_code;
    private int room_id;
    private String channelName;
    private long live_cid;
    private int uid;
    private String push_url;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getLive_cid() {
        return live_cid;
    }

    public void setLive_cid(long live_cid) {
        this.live_cid = live_cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }
}