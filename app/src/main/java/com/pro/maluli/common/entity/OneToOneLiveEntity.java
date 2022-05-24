package com.pro.maluli.common.entity;

import java.io.Serializable;

public class OneToOneLiveEntity implements Serializable {


    private String status_code;
    private int room_id;
    private String channelName;
    private String live_cid;
    private int uid;
    private String push_url;
    private ChatBean chat;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
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

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
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