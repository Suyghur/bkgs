package com.netease.nim.uikit.business.session.myCustom.extension;

import com.alibaba.fastjson.JSONObject;

//自定义消息体
public class AnchorsPperation extends CustomAttachment {
    private final String KEY_STATUS = "status";
    private final String KEY_VALUE = "value";
    private final String KEY_COMMAND = "command";
    private final String KEY_TITLE = "title";
    private final String KEY_PRIXE = "prize_draw_id";
    private final String KEY_DATA = "data";

    // 图文消息的标题，必须有
    private String status;
    private String value;
    private String data;
    private String command;
    private String title;
    private String prize_draw_id;

    public AnchorsPperation() {
        super(CustomAttachmentType.operation);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPrize_draw_id() {
        return prize_draw_id;
    }

    public void setPrize_draw_id(String prize_draw_id) {
        this.prize_draw_id = prize_draw_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    // 解析数据内容
    @Override
    protected void parseData(JSONObject data1) {
        status = data1.getString(KEY_STATUS);
        value = data1.getString(KEY_VALUE);
        command = data1.getString(KEY_COMMAND);
        prize_draw_id = data1.getString(KEY_PRIXE);
        title = data1.getString(KEY_TITLE);
        data = data1.getString(KEY_DATA);

    }

    // 数据打包
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_STATUS, getStatus());
        data.put(KEY_VALUE, getValue());
        data.put(KEY_COMMAND, getCommand());
        data.put(KEY_PRIXE, getPrize_draw_id());
        data.put(KEY_TITLE, getTitle());
        data.put(KEY_DATA, getData());

        return data;
    }
}
