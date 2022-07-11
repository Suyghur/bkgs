package com.pro.maluli.common.entity;

import java.io.Serializable;

public class ImageEntity implements Serializable {
    private String url;
    private int type;

    public ImageEntity(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}