package com.pro.maluli.common.entity;

import java.io.Serializable;

public class ApplyLimitEntity implements Serializable {
    /**
     *
     */
    private String max_picture;
    private String max_desc;

    public String getMax_picture() {
        return max_picture;
    }

    public void setMax_picture(String max_picture) {
        this.max_picture = max_picture;
    }

    public String getMax_desc() {
        return max_desc;
    }

    public void setMax_desc(String max_desc) {
        this.max_desc = max_desc;
    }
}