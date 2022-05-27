package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class WatchListEntity implements Serializable {

    /**
     * data : {"list":[{"id":2,"nickname":"林师金","avatar":"https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210706/c5543ec1f7e97a68d390c63197905f05.jpg"}]}
     * message : success
     * code : 200
     */


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 2
         * nickname : 林师金
         * avatar : https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210706/c5543ec1f7e97a68d390c63197905f05.jpg
         */

        private int id;
        private String nickname;
        private String avatar;
        private int anchor_id;

        public int getAnchor_id() {
            return anchor_id;
        }

        public void setAnchor_id(int anchor_id) {
            this.anchor_id = anchor_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
