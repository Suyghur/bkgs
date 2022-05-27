package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONObject;

public class LianmaiAttachment extends CustomAttachment {

    private static final String KEY_STATUS = "playingStatus";
    private String playingStatus;//  1打开麦克风，2关闭麦克风，3打开摄像头，4关闭摄像头

    public LianmaiAttachment() {
        super(CustomAttachmentType.lianmai);
    }

    @Override
    protected void parseData(JSONObject data) {
        playingStatus = data.getString(KEY_STATUS);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_STATUS, playingStatus);
        return data;
    }

    public String getPlayingStatus() {
        return playingStatus;
    }

    public void setPlayingStatus(String playingStatus) {
        this.playingStatus = playingStatus;
    }
}
