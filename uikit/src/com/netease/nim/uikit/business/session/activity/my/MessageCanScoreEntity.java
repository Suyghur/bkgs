package com.netease.nim.uikit.business.session.activity.my;

import java.io.Serializable;

public class MessageCanScoreEntity implements Serializable {
    /**
     * "notice_str": "私信顶部公告",私信顶部公告 <string>
     * "is_anchor": 0,是否主播 (是的话显示收到礼物和评分) <number>
     * "is_comment": 1,今天是否已评分 <number>
     * "send_count": ""今天发生礼物次数 <string>
     */
        private String notice_str;
        private int is_anchor;
        private int is_comment;
        private int send_count;
        private int is_back;//是否被拉黑

    public int getIs_back() {
        return is_back;
    }

    public void setIs_back(int is_back) {
        this.is_back = is_back;
    }

    public String getNotice_str() {
            return notice_str;
        }

        public void setNotice_str(String notice_str) {
            this.notice_str = notice_str;
        }

        public int getIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(int is_anchor) {
            this.is_anchor = is_anchor;
        }

        public int getIs_comment() {
            return is_comment;
        }

        public void setIs_comment(int is_comment) {
            this.is_comment = is_comment;
        }

        public int getSend_count() {
            return send_count;
        }

        public void setSend_count(int send_count) {
            this.send_count = send_count;
        }
}