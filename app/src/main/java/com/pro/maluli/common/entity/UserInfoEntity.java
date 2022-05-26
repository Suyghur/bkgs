package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class UserInfoEntity implements Serializable {
    private int id;
    private int is_anchor;
    private String avatar;
    private String nickname;
    private String sex;
    private String money;
    private int fans;
    private int subs;
    private String phone;
    private String bind_wx;
    private String bind_qq;
    private String bind_alipay;
    private int is_read_conceal;
    private int is_teenager;
    private int new_video;
    private int new_message;
    private int new_report;
    private String android_version;
    private String ios_version;
    private String anchor_money;
    private String anchor_level;
    private String anchor_no;
    private int anchor_id;
    private List<NoticeBean> notice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_anchor() {
        return is_anchor;
    }

    public void setIs_anchor(int is_anchor) {
        this.is_anchor = is_anchor;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getSubs() {
        return subs;
    }

    public void setSubs(int subs) {
        this.subs = subs;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBind_wx() {
        return bind_wx;
    }

    public void setBind_wx(String bind_wx) {
        this.bind_wx = bind_wx;
    }

    public String getBind_qq() {
        return bind_qq;
    }

    public void setBind_qq(String bind_qq) {
        this.bind_qq = bind_qq;
    }

    public String getBind_alipay() {
        return bind_alipay;
    }

    public void setBind_alipay(String bind_alipay) {
        this.bind_alipay = bind_alipay;
    }

    public int getIs_read_conceal() {
        return is_read_conceal;
    }

    public void setIs_read_conceal(int is_read_conceal) {
        this.is_read_conceal = is_read_conceal;
    }

    public int getIs_teenager() {
        return is_teenager;
    }

    public void setIs_teenager(int is_teenager) {
        this.is_teenager = is_teenager;
    }

    public int getNew_video() {
        return new_video;
    }

    public void setNew_video(int new_video) {
        this.new_video = new_video;
    }

    public int getNew_message() {
        return new_message;
    }

    public void setNew_message(int new_message) {
        this.new_message = new_message;
    }

    public int getNew_report() {
        return new_report;
    }

    public void setNew_report(int new_report) {
        this.new_report = new_report;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getIos_version() {
        return ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }

    public String getAnchor_money() {
        return anchor_money;
    }

    public void setAnchor_money(String anchor_money) {
        this.anchor_money = anchor_money;
    }

    public String getAnchor_level() {
        return anchor_level;
    }

    public void setAnchor_level(String anchor_level) {
        this.anchor_level = anchor_level;
    }

    public String getAnchor_no() {
        return anchor_no;
    }

    public void setAnchor_no(String anchor_no) {
        this.anchor_no = anchor_no;
    }

    public int getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(int anchor_id) {
        this.anchor_id = anchor_id;
    }

    public List<NoticeBean> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeBean> notice) {
        this.notice = notice;
    }

    public static class NoticeBean implements Serializable {
        private int id;
        private String title;
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "NoticeBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "id=" + id +
                ", is_anchor=" + is_anchor +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", money='" + money + '\'' +
                ", fans=" + fans +
                ", subs=" + subs +
                ", phone='" + phone + '\'' +
                ", bind_wx='" + bind_wx + '\'' +
                ", bind_qq='" + bind_qq + '\'' +
                ", bind_alipay='" + bind_alipay + '\'' +
                ", is_read_conceal=" + is_read_conceal +
                ", is_teenager=" + is_teenager +
                ", new_video=" + new_video +
                ", new_message=" + new_message +
                ", new_report=" + new_report +
                ", android_version='" + android_version + '\'' +
                ", ios_version='" + ios_version + '\'' +
                ", anchor_money='" + anchor_money + '\'' +
                ", anchor_level='" + anchor_level + '\'' +
                ", anchor_no='" + anchor_no + '\'' +
                ", anchor_id=" + anchor_id +
                ", notice=" + notice +
                '}';
    }
}
