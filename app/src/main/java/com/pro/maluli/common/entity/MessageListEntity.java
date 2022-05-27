package com.pro.maluli.common.entity;

import java.io.Serializable;

public class MessageListEntity implements Serializable {

    private int is_anchor;
    private int system_notice_count;
    private int anchor_appoint_count;
    private int appoint_count;

    public int getIs_anchor() {
        return is_anchor;
    }

    public void setIs_anchor(int is_anchor) {
        this.is_anchor = is_anchor;
    }

    public int getSystem_notice_count() {
        return system_notice_count;
    }

    public void setSystem_notice_count(int system_notice_count) {
        this.system_notice_count = system_notice_count;
    }

    public int getAnchor_appoint_count() {
        return anchor_appoint_count;
    }

    public void setAnchor_appoint_count(int anchor_appoint_count) {
        this.anchor_appoint_count = anchor_appoint_count;
    }

    public int getAppoint_count() {
        return appoint_count;
    }

    public void setAppoint_count(int appoint_count) {
        this.appoint_count = appoint_count;
    }
}