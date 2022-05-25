package com.pro.maluli.common.entity;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class HomeInfoEntity implements Serializable {

    private List<NoticeBean> notice;
    private CategoryBean category;
    private List<BannerBean> banner;

    public static HomeInfoEntity objectFromData(String str) {
        return new Gson().fromJson(str, HomeInfoEntity.class);
    }

    public List<NoticeBean> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeBean> notice) {
        this.notice = notice;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class NoticeBean implements Serializable {
        private int id;
        private String title;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @Override
        public String toString() {
            return "NoticeBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", created_at='" + created_at + '\'' +
                    '}';
        }
    }

    public static class CategoryBean implements Serializable {
        private List<AllBean> all;
        private List<ListBean> list;

        public static CategoryBean objectFromData(String str) {
            return new Gson().fromJson(str, CategoryBean.class);
        }

        public List<AllBean> getAll() {
            return all;
        }

        public void setAll(List<AllBean> all) {
            this.all = all;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class AllBean implements Serializable {
            private int id;
            private String title;
            private int is_teenager;

            public static AllBean objectFromData(String str) {

                return new Gson().fromJson(str, AllBean.class);
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getIs_teenager() {
                return is_teenager;
            }

            public void setIs_teenager(int is_teenager) {
                this.is_teenager = is_teenager;
            }

            @Override
            public String toString() {
                return "AllBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", is_teenager=" + is_teenager +
                        '}';
            }
        }

        public static class ListBean implements Serializable, IPickerViewData {
            private int id;
            private String title;
            private int is_teenager;
            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            private List<ChildBean> children;

            public static ListBean objectFromData(String str) {

                return new Gson().fromJson(str, ListBean.class);
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getIs_teenager() {
                return is_teenager;
            }

            public void setIs_teenager(int is_teenager) {
                this.is_teenager = is_teenager;
            }

            public List<ChildBean> getChildren() {
                return children;
            }

            @Override
            public String getPickerViewText() {
                return title;
            }

            public static class ChildBean implements Serializable, IPickerViewData {
                private int id;
                private String title;
                private int is_teenager;

                public static CategoryBean.AllBean objectFromData(String str) {

                    return new Gson().fromJson(str, CategoryBean.AllBean.class);
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getIs_teenager() {
                    return is_teenager;
                }

                public void setIs_teenager(int is_teenager) {
                    this.is_teenager = is_teenager;
                }

                @Override
                public String getPickerViewText() {
                    return title;
                }

                @Override
                public String toString() {
                    return "ChildBean{" +
                            "id=" + id +
                            ", title='" + title + '\'' +
                            ", is_teenager=" + is_teenager +
                            '}';
                }
            }

            public void setChildren(List<ChildBean> children) {
                this.children = children;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", is_teenager=" + is_teenager +
                        ", isSelect=" + isSelect +
                        ", children=" + children +
                        '}';
            }
        }
    }

    public static class BannerBean implements Serializable {
        private int id;
        private String url;
        private int file_type;
        private String title;
        private String link;
        private String created_at;

        public static BannerBean objectFromData(String str) {

            return new Gson().fromJson(str, BannerBean.class);
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
        return "HomeInfoEntity{" +
                "notice=" + notice +
                ", category=" + category +
                ", banner=" + banner +
                '}';
    }
}
