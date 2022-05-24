package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class PayInfoEntity implements Serializable {
    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    private String pay;
}