package com.pro.maluli.common.entity;

import java.io.Serializable;

public class AboutMeEntity implements Serializable {

        private ShareBean share;
        private WebsiteBean website;
        private UpdateBean update;

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public WebsiteBean getWebsite() {
            return website;
        }

        public void setWebsite(WebsiteBean website) {
            this.website = website;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public static class ShareBean implements Serializable {
            private String title;
            private String image;

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
        }

        public static class WebsiteBean implements Serializable {
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class UpdateBean implements Serializable {
            private AndroidBean android;
            private AndroidBean ios;

            public AndroidBean getAndroid() {
                return android;
            }

            public void setAndroid(AndroidBean android) {
                this.android = android;
            }

            public AndroidBean getIos() {
                return ios;
            }

            public void setIos(AndroidBean ios) {
                this.ios = ios;
            }

            public static class AndroidBean implements Serializable {
                private String version;
                private String download;
                private String desc;
                private int update_force;

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }

                public String getDownload() {
                    return download;
                }

                public void setDownload(String download) {
                    this.download = download;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public int getUpdate_force() {
                    return update_force;
                }

                public void setUpdate_force(int update_force) {
                    this.update_force = update_force;
                }
            }
    }
}