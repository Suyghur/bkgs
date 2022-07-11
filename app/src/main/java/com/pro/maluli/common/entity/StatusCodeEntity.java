package com.pro.maluli.common.entity;

import java.io.Serializable;

/**
 * @author #Suyghur.
 * Created on 2022/6/20
 */
public class StatusCodeEntity implements Serializable {

    String status_code = "0";

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    @Override
    public String toString() {
        return "StatusCodeEntity{" +
                "status_code=" + status_code +
                '}';
    }
}
