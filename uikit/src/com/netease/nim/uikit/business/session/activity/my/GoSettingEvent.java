package com.netease.nim.uikit.business.session.activity.my;

public class GoSettingEvent {
    public String accid;
    public boolean isGift;//是请求礼物
    public boolean isSendGift;//是发送礼物
    public boolean isGetGiftForMe;//是获取礼物列表
    public GiftEntity.ListBean listBean;
    public String link;//不为空，调用接口子再次预约
    public boolean isGoRecharge;//跳转到充值页面；
    public boolean isScore;//评价窗口
    public  boolean isSeeNotic;//只是获取聊天公告信息
    private boolean isCanScore;//检查是否能评价
    private boolean isGoAnchorInfo;//跳转到主播信息页
    private int ablitityNumber,serviceNumber;

    public boolean isGoAnchorInfo() {
        return isGoAnchorInfo;
    }

    public void setGoAnchorInfo(boolean goAnchorInfo) {
        isGoAnchorInfo = goAnchorInfo;
    }

    public int getAblitityNumber() {
        return ablitityNumber;
    }

    public void setAblitityNumber(int ablitityNumber) {
        this.ablitityNumber = ablitityNumber;
    }

    public int getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(int serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public boolean isCanScore() {
        return isCanScore;
    }

    public void setCanScore(boolean canScore) {
        isCanScore = canScore;
    }

    public boolean isSeeNotic() {
        return isSeeNotic;
    }

    public void setSeeNotic(boolean seeNotic) {
        isSeeNotic = seeNotic;
    }

    public boolean isScore() {
        return isScore;
    }

    public void setScore(boolean score) {
        isScore = score;
    }

    public boolean isGetGiftForMe() {
        return isGetGiftForMe;
    }

    public void setGetGiftForMe(boolean getGiftForMe) {
        isGetGiftForMe = getGiftForMe;
    }

    public boolean isGoRecharge() {
        return isGoRecharge;
    }

    public void setGoRecharge(boolean goRecharge) {
        isGoRecharge = goRecharge;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public GiftEntity.ListBean getListBean() {
        return listBean;
    }

    public void setListBean(GiftEntity.ListBean listBean) {
        this.listBean = listBean;
    }


    public boolean isSendGift() {
        return isSendGift;
    }

    public void setSendGift(boolean sendGift) {
        isSendGift = sendGift;
    }


    public GoSettingEvent(String accid) {
        this.accid = accid;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }


    public boolean isGift() {
        return isGift;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }
}