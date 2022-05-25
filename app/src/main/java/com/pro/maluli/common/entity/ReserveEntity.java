package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class ReserveEntity implements Serializable {

    private InfoBean info;
    private List<BannerBean> banner;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class InfoBean implements Serializable {
        private int is_live;
        private int is_report;
        private int is_appoint;
        private int is_self;
        private int is_sub;
        private String anchor_no;
        private String avatar;
        private String nickname;
        private String level;
        private String appoint_desc;
        private int is_progress;
        private String progress_text;
        private String appoint_time;
        private int appoint_num;
        private int report_num;
        private LiveBean live;
        private List<AppointListBean> appoint_list;

        public int getIs_progress() {
            return is_progress;
        }

        public void setIs_progress(int is_progress) {
            this.is_progress = is_progress;
        }

        public String getProgress_text() {
            return progress_text;
        }

        public void setProgress_text(String progress_text) {
            this.progress_text = progress_text;
        }

        public String getAppoint_time() {
            return appoint_time;
        }

        public void setAppoint_time(String appoint_time) {
            this.appoint_time = appoint_time;
        }

        public int getIs_live() {
            return is_live;
        }

        public void setIs_live(int is_live) {
            this.is_live = is_live;
        }

        public int getIs_report() {
            return is_report;
        }

        public void setIs_report(int is_report) {
            this.is_report = is_report;
        }

        public int getIs_appoint() {
            return is_appoint;
        }

        public void setIs_appoint(int is_appoint) {
            this.is_appoint = is_appoint;
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

        public String getAppoint_desc() {
            return appoint_desc;
        }

        public void setAppoint_desc(String appoint_desc) {
            this.appoint_desc = appoint_desc;
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

        public LiveBean getLive() {
            return live;
        }

        public void setLive(LiveBean live) {
            this.live = live;
        }

        public List<AppointListBean> getAppoint_list() {
            return appoint_list;
        }

        public void setAppoint_list(List<AppointListBean> appoint_list) {
            this.appoint_list = appoint_list;
        }

        public static class LiveBean implements Serializable {
            private int id;
            private int type;
            private String title;
            private String image;
            private String start_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            @Override
            public String toString() {
                return "LiveBean{" +
                        "id=" + id +
                        ", type=" + type +
                        ", title='" + title + '\'' +
                        ", image='" + image + '\'' +
                        ", start_time='" + start_time + '\'' +
                        '}';
            }
        }

        public static class AppointListBean implements Serializable {
            private int id;
            private int status;
            private List<String> images;
            private String content;
            private String created_at;
            private String nickname;
            private String avatar;

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

            @Override
            public String toString() {
                return "AppointListBean{" +
                        "id=" + id +
                        ", status=" + status +
                        ", images=" + images +
                        ", content='" + content + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", avatar='" + avatar + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "is_live=" + is_live +
                    ", is_report=" + is_report +
                    ", is_appoint=" + is_appoint +
                    ", is_self=" + is_self +
                    ", is_sub=" + is_sub +
                    ", anchor_no='" + anchor_no + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level='" + level + '\'' +
                    ", appoint_desc='" + appoint_desc + '\'' +
                    ", is_progress=" + is_progress +
                    ", progress_text='" + progress_text + '\'' +
                    ", appoint_time='" + appoint_time + '\'' +
                    ", appoint_num=" + appoint_num +
                    ", report_num=" + report_num +
                    ", live=" + live +
                    ", appoint_list=" + appoint_list +
                    '}';
        }
    }

    public static class BannerBean implements Serializable {
        private int id;
        private String url;
        private int file_type;
        private String title;
        private String link;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @Override
        public String toString() {
            return "BannerBean{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", file_type=" + file_type +
                    ", title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", created_at='" + created_at + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ReserveEntity{" +
                "info=" + info +
                ", banner=" + banner +
                '}';
    }
}