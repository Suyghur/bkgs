package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class RewardDetailEntity implements Serializable {

    private String pay_in;
    private String pay_out;
    private int total;
    private List<ListBean> list;

    public String getPay_in() {
        return pay_in;
    }

    public void setPay_in(String pay_in) {
        this.pay_in = pay_in;
    }

    public String getPay_out() {
        return pay_out;
    }

    public void setPay_out(String pay_out) {
        this.pay_out = pay_out;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        private int id;
        private int anchor_id;
        private int member_id;
        private int live_id;
        private int flow_type;
        private String product_type;
        private int gift_id;
        private String money;
        private String created_at;
        private String updated_at;
        private Object deleted_at;
        private String nickname;
        private String avatar;
        private String gift_title;
        private int type;
        private GiftBean gift;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAnchor_id() {
            return anchor_id;
        }

        public void setAnchor_id(int anchor_id) {
            this.anchor_id = anchor_id;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public int getLive_id() {
            return live_id;
        }

        public void setLive_id(int live_id) {
            this.live_id = live_id;
        }

        public int getFlow_type() {
            return flow_type;
        }

        public void setFlow_type(int flow_type) {
            this.flow_type = flow_type;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public int getGift_id() {
            return gift_id;
        }

        public void setGift_id(int gift_id) {
            this.gift_id = gift_id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

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

        public String getGift_title() {
            return gift_title;
        }

        public void setGift_title(String gift_title) {
            this.gift_title = gift_title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public GiftBean getGift() {
            return gift;
        }

        public void setGift(GiftBean gift) {
            this.gift = gift;
        }

        public static class GiftBean implements Serializable {
            private int id;
            private String title;
            private String logo;
            private int money;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }
        }
    }
}