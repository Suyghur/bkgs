package com.pro.maluli.common.entity;

import java.io.Serializable;

public class LastTimeLiveEntity implements Serializable {
    private String status_code;
    private int can_report;
    private String title;
    private String image;
    private int is_live;
    private int type;
    private String reason;
    private int room_id;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIs_live() {
        return is_live;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getCan_report() {
        return can_report;
    }

    public void setCan_report(int can_report) {
        this.can_report = can_report;
    }
}