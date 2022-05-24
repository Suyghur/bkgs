package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class SeeLiveUserEntity implements Serializable {

    private AppointBean appoint;
    private String anchor_accid;
    private int status_code;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public AppointBean getAppoint() {
        return appoint;
    }

    public void setAppoint(AppointBean appoint) {
        this.appoint = appoint;
    }

    public String getAnchor_accid() {
        return anchor_accid;
    }

    public void setAnchor_accid(String anchor_accid) {
        this.anchor_accid = anchor_accid;
    }

    public static class AppointBean implements Serializable {
        private int id;
        private int status;
        private List<String> images;
        private String content;
        private String created_at;
        private String nickname;
        private String avatar;
        private String accid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }
    }
}