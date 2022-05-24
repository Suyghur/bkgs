package com.pro.maluli.common.entity;

import java.io.Serializable;

public class CanTimeVideoEntity implements Serializable {
    /**
     * "max_desc": "500",个人说明最大字数限制 <number>
     * "max_photo": "10",相册上传最大值/张 <string>
     * "max_video_time": "80000",小视频最大时长/秒 <string>
     * "max_video": "20",小视频上传最大值 <string>
     * "max_video_desc": "500"小视频上传文案数量最大值 <string> 8
     */
        private String max_desc;
        private String max_photo;
        private String max_video_time;
        private String max_video;
        private String max_video_desc;

        public String getMax_desc() {
            return max_desc;
        }

        public void setMax_desc(String max_desc) {
            this.max_desc = max_desc;
        }

        public String getMax_photo() {
            return max_photo;
        }

        public void setMax_photo(String max_photo) {
            this.max_photo = max_photo;
        }

        public String getMax_video_time() {
            return max_video_time;
        }

        public void setMax_video_time(String max_video_time) {
            this.max_video_time = max_video_time;
        }

        public String getMax_video() {
            return max_video;
        }

        public void setMax_video(String max_video) {
            this.max_video = max_video;
        }

        public String getMax_video_desc() {
            return max_video_desc;
        }

        public void setMax_video_desc(String max_video_desc) {
            this.max_video_desc = max_video_desc;
        }
}