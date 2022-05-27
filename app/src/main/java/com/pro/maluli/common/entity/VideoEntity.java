package com.pro.maluli.common.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable {

    private AnchorBean anchor;
    private VideoBean video;
    private String status_code;//判断是否是广告信息101，4002限制观看
    private AdvertBean advert;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public AdvertBean getAdvert() {
        return advert;
    }

    public void setAdvert(AdvertBean advert) {
        this.advert = advert;
    }

    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public static class AdvertBean implements Serializable {
        private String url;
        private String title;
        private String link;
        private String icon;
        private String content;
        private String created_at;
        private int file_type;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }
    }

    public static class AnchorBean implements Serializable {
        private int is_live;
        private int is_report;
        private int is_self;
        private int is_sub;
        private String anchor_no;
        private String avatar;
        private String nickname;
        private String level;
        private int professional;
        private int service;
        private int service_num;
        private LiveBean live;

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

        public int getProfessional() {
            return professional;
        }

        public void setProfessional(int professional) {
            this.professional = professional;
        }

        public int getService() {
            return service;
        }

        public void setService(int service) {
            this.service = service;
        }

        public int getService_num() {
            return service_num;
        }

        public void setService_num(int service_num) {
            this.service_num = service_num;
        }

        public LiveBean getLive() {
            return live;
        }

        public void setLive(LiveBean live) {
            this.live = live;
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
        }
    }

    public static class VideoBean implements Serializable {
        private int video_id;
        private int anchor_id;
        private int is_like;
        private String desc;
        private String url;
        private String cover;
        private int like_num;
        private int comment_num;
        private int download_num;
        private int share_num;
        private String share_url;

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public int getAnchor_id() {
            return anchor_id;
        }

        public void setAnchor_id(int anchor_id) {
            this.anchor_id = anchor_id;
        }

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getDownload_num() {
            return download_num;
        }

        public void setDownload_num(int download_num) {
            this.download_num = download_num;
        }

        public int getShare_num() {
            return share_num;
        }

        public void setShare_num(int share_num) {
            this.share_num = share_num;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}