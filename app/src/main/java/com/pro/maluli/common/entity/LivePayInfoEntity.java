package com.pro.maluli.common.entity;

import java.io.Serializable;

public class LivePayInfoEntity implements Serializable {

    private String status_code;
    private int room_id;
    private int is_need_pay;
    private int money;

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

    public int getIs_need_pay() {
        return is_need_pay;
    }

    public void setIs_need_pay(int is_need_pay) {
        this.is_need_pay = is_need_pay;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}