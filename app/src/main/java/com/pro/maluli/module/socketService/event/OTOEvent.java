package com.pro.maluli.module.socketService.event;

public class OTOEvent {

    private OnTwoOneStartEntity entity;//总数居
    private int msg_id;//数据类型  msg_id 0-推送失败 1-正常推送 2-设定时间 3-加时 4-开始 5-结束

    public OTOEvent() {
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public OnTwoOneStartEntity getEntity() {
        return entity;
    }

    public void setEntity(OnTwoOneStartEntity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "OTOEvent{" +
                "entity=" + entity +
                ", msg_id=" + msg_id +
                '}';
    }
}