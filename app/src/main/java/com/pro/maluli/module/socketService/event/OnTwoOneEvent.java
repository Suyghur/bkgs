package com.pro.maluli.module.socketService.event;

import com.pro.maluli.common.entity.OnTwoOneSocketEntity;
import com.pro.maluli.common.entity.ReserveEntity;

public class OnTwoOneEvent {
    private ReserveEntity entity;

    public ReserveEntity getEntity() {
        return entity;
    }

    public void setEntity(ReserveEntity entity) {
        this.entity = entity;
    }

    public OnTwoOneEvent(ReserveEntity entity) {
        this.entity = entity;
    }
}