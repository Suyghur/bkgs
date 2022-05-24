package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class LiveListEntity implements Serializable {


    private DataBean data;
    private String message;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean implements Serializable {
        private int total;
        private List<ListBean> list;

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
            private String nickname;
            private String avatar;
            private int cate_id;
            private int is_top;
            private int month_spend_money;
            private int join_one_num;
            private int start;
            private String level;
            private String title;
            private String image;
            private int type;
            private int is_live;

            public int getRoom_id() {
                return room_id;
            }

            public void setRoom_id(int room_id) {
                this.room_id = room_id;
            }

            private int room_id;
            private List<String> tags;

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

            public int getCate_id() {
                return cate_id;
            }

            public void setCate_id(int cate_id) {
                this.cate_id = cate_id;
            }

            public int getIs_top() {
                return is_top;
            }

            public void setIs_top(int is_top) {
                this.is_top = is_top;
            }

            public int getMonth_spend_money() {
                return month_spend_money;
            }

            public void setMonth_spend_money(int month_spend_money) {
                this.month_spend_money = month_spend_money;
            }

            public int getJoin_one_num() {
                return join_one_num;
            }

            public void setJoin_one_num(int join_one_num) {
                this.join_one_num = join_one_num;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getIs_live() {
                return is_live;
            }

            public void setIs_live(int is_live) {
                this.is_live = is_live;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }
        }
    }
}
