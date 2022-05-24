package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class AnchorImgEntity implements Serializable {

        private List<PictureBean> picture;

        public List<PictureBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PictureBean> picture) {
            this.picture = picture;
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
}