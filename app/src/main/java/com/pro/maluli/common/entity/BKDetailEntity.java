package com.pro.maluli.common.entity;

import java.io.Serializable;

public class BKDetailEntity implements Serializable {
    private String number;
    private boolean isSelect;

    public BKDetailEntity(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
