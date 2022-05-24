package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class RechargeEntity implements Serializable {
    private RechargeEntity.MemberBean member;
    private RechargeEntity.ProtocolBean protocol;
    private List<RechargeEntity.PayBean> pay;

    public RechargeEntity.MemberBean getMember() {
        return member;
    }

    public void setMember(RechargeEntity.MemberBean member) {
        this.member = member;
    }

    public RechargeEntity.ProtocolBean getProtocol() {
        return protocol;
    }

    public void setProtocol(RechargeEntity.ProtocolBean protocol) {
        this.protocol = protocol;
    }

    public List<RechargeEntity.PayBean> getPay() {
        return pay;
    }

    public void setPay(List<RechargeEntity.PayBean> pay) {
        this.pay = pay;
    }

    public static class MemberBean implements Serializable {
        private String nickname;
        private String avatar;
        private String money;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

    public static class ProtocolBean implements Serializable {
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class PayBean implements Serializable {
        private int bk_money;
        private String money;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int id;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getBk_money() {
            return bk_money;
        }

        public void setBk_money(int bk_money) {
            this.bk_money = bk_money;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
