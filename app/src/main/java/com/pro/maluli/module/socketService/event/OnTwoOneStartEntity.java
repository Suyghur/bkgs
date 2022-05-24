package com.pro.maluli.module.socketService.event;

import java.io.Serializable;
import java.util.List;

public class OnTwoOneStartEntity implements Serializable {


        private int is_self;
        private int is_sub;
        private String anchor_no;
        private String anchor_accid;
        private String avatar;
        private String nickname;
        private String level;
        private int appoint_num;
        private int report_num;
        private int now_time;
        private int play_time;
        private int set_time;
        private int type;
        private int special_count;
        private int now_appoint_num;

    public int getPlay_time() {
        return play_time;
    }

    public void setPlay_time(int play_time) {
        this.play_time = play_time;
    }

    private List<SpecialListBean> special_list;

    public int getNow_appoint_num() {
        return now_appoint_num;
    }

    public void setNow_appoint_num(int now_appoint_num) {
        this.now_appoint_num = now_appoint_num;
    }

    public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public int getIs_sub() {
            return is_sub;
        }

        public void setIs_sub(int is_sub) {
            this.is_sub = is_sub;
        }

        public String getAnchor_no() {
            return anchor_no;
        }

        public void setAnchor_no(String anchor_no) {
            this.anchor_no = anchor_no;
        }

        public String getAnchor_accid() {
            return anchor_accid;
        }

        public void setAnchor_accid(String anchor_accid) {
            this.anchor_accid = anchor_accid;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getAppoint_num() {
            return appoint_num;
        }

        public void setAppoint_num(int appoint_num) {
            this.appoint_num = appoint_num;
        }

        public int getReport_num() {
            return report_num;
        }

        public void setReport_num(int report_num) {
            this.report_num = report_num;
        }

        public int getNow_time() {
            return now_time;
        }

        public void setNow_time(int now_time) {
            this.now_time = now_time;
        }

        public int getSet_time() {
            return set_time;
        }

        public void setSet_time(int set_time) {
            this.set_time = set_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSpecial_count() {
            return special_count;
        }

        public void setSpecial_count(int special_count) {
            this.special_count = special_count;
        }

        public List<SpecialListBean> getSpecial_list() {
            return special_list;
        }

        public void setSpecial_list(List<SpecialListBean> special_list) {
            this.special_list = special_list;
        }

        public static class SpecialListBean implements Serializable {
            private int id;
            private int anchor_id;
            private int status;
            private String content;
            private int start_time;
            private int end_time;
            private String created_at;
            private int uid;
            private String nickname;
            private String avatar;
            private String accid;
            private List<String> images;

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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
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

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }
}