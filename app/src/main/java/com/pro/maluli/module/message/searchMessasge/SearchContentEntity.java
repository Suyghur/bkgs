package com.pro.maluli.module.message.searchMessasge;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.Serializable;
import java.util.List;

public class SearchContentEntity implements Serializable {
    private int typee;
    private List<IMMessage> message;
    private List<NimUserInfo> userInfo;

    public int getTypee() {
        return typee;
    }

    public void setTypee(int typee) {
        this.typee = typee;
    }

    public List<IMMessage> getMessage() {
        return message;
    }

    public void setMessage(List<IMMessage> message) {
        this.message = message;
    }

    public List<NimUserInfo> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<NimUserInfo> userInfo) {
        this.userInfo = userInfo;
    }
}