package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class AnchorLabelEntity implements Serializable {
    private List<TagsBean> tags;

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean implements Serializable {
        private int id;
        private String tag_name;
        private boolean isDelete;

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

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
}