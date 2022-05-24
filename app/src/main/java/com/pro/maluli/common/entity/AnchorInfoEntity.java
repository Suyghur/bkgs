package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class AnchorInfoEntity implements Serializable {

        private int anchor_id;
        private int is_ban_live;
        private int member_id;
        private int is_live;
        private int is_report;
        private int is_edit;
        private int is_sub;
        private int is_back;
        private int is_appoint;
        private String anchor_no;
        private String anchor_accid;
        private String new_video;
        private String new_video_cover;
        private String avatar;
        private String nickname;
        private String sex;
        private int fans;
        private String cate_one;
        private String cate;
        private String level;
        private int professional;
        private int service;
        private int service_num;
        private String desc;
        private String share_url;
        private int appoint_num;
        private int report_num;
        private String max_desc;
        private LiveBean live;
        private List<TagsBean> tags;
        private List<PictureBean> picture;
        private List<VideoBean> video;

    public String getMax_desc() {
        return max_desc;
    }

    public void setMax_desc(String max_desc) {
        this.max_desc = max_desc;
    }

    public int getIs_appoint() {
        return is_appoint;
    }

    public void setIs_appoint(int is_appoint) {
        this.is_appoint = is_appoint;
    }

    public String getShare_url() {
        return share_url;
    }

    public int getIs_ban_live() {
        return is_ban_live;
    }

    public void setIs_ban_live(int is_ban_live) {
        this.is_ban_live = is_ban_live;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
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

        public int getIs_edit() {
            return is_edit;
        }

        public void setIs_edit(int is_edit) {
            this.is_edit = is_edit;
        }

        public int getIs_sub() {
            return is_sub;
        }

        public void setIs_sub(int is_sub) {
            this.is_sub = is_sub;
        }

        public int getIs_back() {
            return is_back;
        }

        public void setIs_back(int is_back) {
            this.is_back = is_back;
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

        public String getNew_video() {
            return new_video;
        }

        public void setNew_video(String new_video) {
            this.new_video = new_video;
        }

        public String getNew_video_cover() {
            return new_video_cover;
        }

        public void setNew_video_cover(String new_video_cover) {
            this.new_video_cover = new_video_cover;
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

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public String getCate_one() {
            return cate_one;
        }

        public void setCate_one(String cate_one) {
            this.cate_one = cate_one;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<PictureBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PictureBean> picture) {
            this.picture = picture;
        }

        public List<VideoBean> getVideo() {
            return video;
        }

        public void setVideo(List<VideoBean> video) {
            this.video = video;
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

        public static class TagsBean implements Serializable {
            private int id;
            private String tag_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }
        }

        public static class PictureBean implements Serializable {
            private int id;
            private int sort;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class VideoBean implements Serializable {
            private int id;
            private String url;
            private String cover;
            private String desc;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

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

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }
}