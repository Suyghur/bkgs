package com.pro.maluli.module.home.oneToMore.StartOneToMoreLive;

import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;

import java.io.Serializable;

public class LianmaiEntity implements Serializable {
    private String accid;
    private String avatar;
    private InvitedEvent invitedEvent;

    public InvitedEvent getInvitedEvent() {
        return invitedEvent;
    }

    public void setInvitedEvent(InvitedEvent invitedEvent) {
        this.invitedEvent = invitedEvent;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}