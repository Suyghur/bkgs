package com.netease.nim.uikit.business.session.activity.my;

public class GiftEvent {
    private GiftEntity giftEntity;
    private GiftEntity.ListBean giftBean;//
    private GiftForMeEntity giftForMeEntity;//收到的礼物列表
    private boolean isShowGift;//查看收到礼物列表
    private MessageCanScoreEntity messageCanScoreEntity;//聊天顶部公告，以及是否可以发送评价
    private boolean isSendScore;//发送评价
    private String reOrder;//再次预约主播
    private boolean isSendGift;//发送礼物
    private boolean isSeeNotic;//获取聊天公告信息
    private boolean isSHowScore;//是否提示评价弹框
    private boolean isGOtoRecharge;//是否没钱跳转到充值

    public boolean isGOtoRecharge() {
        return isGOtoRecharge;
    }

    public void setGOtoRecharge(boolean GOtoRecharge) {
        isGOtoRecharge = GOtoRecharge;
    }

    public boolean isSHowScore() {
        return isSHowScore;
    }

    public void setSHowScore(boolean SHowScore) {
        isSHowScore = SHowScore;
    }

    public boolean isSeeNotic() {
        return isSeeNotic;
    }

    public void setSeeNotic(boolean seeNotic) {
        isSeeNotic = seeNotic;
    }

    public boolean isSendScore() {
        return isSendScore;
    }

    public void setSendScore(boolean sendScore) {
        isSendScore = sendScore;
    }

    public MessageCanScoreEntity getMessageCanScoreEntity() {
        return messageCanScoreEntity;
    }

    public void setMessageCanScoreEntity(MessageCanScoreEntity messageCanScoreEntity) {
        this.messageCanScoreEntity = messageCanScoreEntity;
    }

    public boolean isShowGift() {
        return isShowGift;
    }

    public void setShowGift(boolean showGift) {
        isShowGift = showGift;
    }

    public String getReOrder() {
        return reOrder;
    }

    public void setReOrder(String reOrder) {
        this.reOrder = reOrder;
    }



    public GiftForMeEntity getGiftForMeEntity() {
        return giftForMeEntity;
    }

    public void setGiftForMeEntity(GiftForMeEntity giftForMeEntity) {
        this.giftForMeEntity = giftForMeEntity;
    }

    public GiftEntity.ListBean getGiftBean() {
        return giftBean;
    }

    public void setGiftBean(GiftEntity.ListBean giftBean) {
        this.giftBean = giftBean;
    }



    public boolean isSendGift() {
        return isSendGift;
    }

    public void setSendGift(boolean sendGift) {
        isSendGift = sendGift;
    }

    public GiftEntity getGiftEntity() {
        return giftEntity;
    }

    public void setGiftEntity(GiftEntity giftEntity) {
        this.giftEntity = giftEntity;
    }

    public GiftEvent() {
    }

}