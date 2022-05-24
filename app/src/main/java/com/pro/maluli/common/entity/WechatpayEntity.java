package com.pro.maluli.common.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class WechatpayEntity implements Serializable {

        private PayBean pay;

        public PayBean getPay() {
            return pay;
        }

        public void setPay(PayBean pay) {
            this.pay = pay;
        }

        public static class PayBean implements Serializable {
            private String sub_appid;
            private String sub_mch_id;
            private String partnerid;
            private String prepayid;
            private String packagex;
            private int timestamp;
            private String appid;
            private String noncestr;
            private String sign;

            public String getSub_appid() {
                return sub_appid;
            }

            public void setSub_appid(String sub_appid) {
                this.sub_appid = sub_appid;
            }

            public String getSub_mch_id() {
                return sub_mch_id;
            }

            public void setSub_mch_id(String sub_mch_id) {
                this.sub_mch_id = sub_mch_id;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getPackageX() {
                return packagex;
            }

            public void setPackageX(String packagex) {
                this.packagex = packagex;
            }

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
        }
    }
}