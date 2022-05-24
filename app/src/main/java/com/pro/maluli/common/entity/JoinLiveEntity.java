package com.pro.maluli.common.entity;

import java.io.Serializable;
import java.util.List;

public class JoinLiveEntity implements Serializable {

        private String status_code;
        private InfoBean info;

        public String getStatus_code() {
            return status_code;
        }

        public void setStatus_code(String status_code) {
            this.status_code = status_code;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean implements Serializable {
            private int is_anchor;
            private String push_url;
            private int room_id;
            private int anchor_id;
            private String accid;
            private String anchor_accid;
            private String channelName;
            private String live_cid;
            private int uid;
            private String http_pull_url;
            private String hls_pull_url;
            private String rtmp_pull_url;
            private ChatBean chat;
            private String nertc_token;
            private String signal_channel_name;
            private String signal_channel_id;
            private String cate_one;
            private String cate;
            private int start_time;
            private int is_appoint;
            private List<String> chat_notice;

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

            public List<String> getChat_notice() {
                return chat_notice;
            }

            public void setChat_notice(List<String> chat_notice) {
                this.chat_notice = chat_notice;
            }

            public int getIs_appoint() {
                return is_appoint;
            }

            public void setIs_appoint(int is_appoint) {
                this.is_appoint = is_appoint;
            }

            public int getAnchor_id() {
                return anchor_id;
            }

            public void setAnchor_id(int anchor_id) {
                this.anchor_id = anchor_id;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public String getSignal_channel_name() {
                return signal_channel_name;
            }

            public void setSignal_channel_name(String signal_channel_name) {
                this.signal_channel_name = signal_channel_name;
            }

            public String getSignal_channel_id() {
                return signal_channel_id;
            }

            public void setSignal_channel_id(String signal_channel_id) {
                this.signal_channel_id = signal_channel_id;
            }

            public String getAnchor_accid() {
                return anchor_accid;
            }

            public void setAnchor_accid(String anchor_accid) {
                this.anchor_accid = anchor_accid;
            }

            public String getNertc_token() {
                return nertc_token;
            }

            public void setNertc_token(String nertc_token) {
                this.nertc_token = nertc_token;
            }

            public int getIs_anchor() {
                return is_anchor;
            }

            public void setIs_anchor(int is_anchor) {
                this.is_anchor = is_anchor;
            }

            public String getPush_url() {
                return push_url;
            }

            public void setPush_url(String push_url) {
                this.push_url = push_url;
            }

            public int getRoom_id() {
                return room_id;
            }

            public void setRoom_id(int room_id) {
                this.room_id = room_id;
            }

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getChannelName() {
                return channelName;
            }

            public void setChannelName(String channelName) {
                this.channelName = channelName;
            }

            public String getLive_cid() {
                return live_cid;
            }

            public void setLive_cid(String live_cid) {
                this.live_cid = live_cid;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getHttp_pull_url() {
                return http_pull_url;
            }

            public void setHttp_pull_url(String http_pull_url) {
                this.http_pull_url = http_pull_url;
            }

            public String getHls_pull_url() {
                return hls_pull_url;
            }

            public void setHls_pull_url(String hls_pull_url) {
                this.hls_pull_url = hls_pull_url;
            }

            public String getRtmp_pull_url() {
                return rtmp_pull_url;
            }

            public void setRtmp_pull_url(String rtmp_pull_url) {
                this.rtmp_pull_url = rtmp_pull_url;
            }

            public ChatBean getChat() {
                return chat;
            }

            public void setChat(ChatBean chat) {
                this.chat = chat;
            }

            public static class ChatBean implements Serializable {
                private String broadcasturl;
                private boolean valid;
                private String ext;
                private String creator;
                private String name;
                private boolean ionotify;
                private boolean muted;
                private int roomid;
                private String announcement;
                private int queuelevel;

                public String getBroadcasturl() {
                    return broadcasturl;
                }

                public void setBroadcasturl(String broadcasturl) {
                    this.broadcasturl = broadcasturl;
                }

                public boolean isValid() {
                    return valid;
                }

                public void setValid(boolean valid) {
                    this.valid = valid;
                }

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public String getCreator() {
                    return creator;
                }

                public void setCreator(String creator) {
                    this.creator = creator;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public boolean isIonotify() {
                    return ionotify;
                }

                public void setIonotify(boolean ionotify) {
                    this.ionotify = ionotify;
                }

                public boolean isMuted() {
                    return muted;
                }

                public void setMuted(boolean muted) {
                    this.muted = muted;
                }

                public int getRoomid() {
                    return roomid;
                }

                public void setRoomid(int roomid) {
                    this.roomid = roomid;
                }

                public String getAnnouncement() {
                    return announcement;
                }

                public void setAnnouncement(String announcement) {
                    this.announcement = announcement;
                }

                public int getQueuelevel() {
                    return queuelevel;
                }

                public void setQueuelevel(int queuelevel) {
                    this.queuelevel = queuelevel;
                }
            }
    }
}