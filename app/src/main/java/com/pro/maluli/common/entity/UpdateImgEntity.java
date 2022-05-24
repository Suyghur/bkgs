package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class UpdateImgEntity implements Serializable {

    private List<String> url;

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
