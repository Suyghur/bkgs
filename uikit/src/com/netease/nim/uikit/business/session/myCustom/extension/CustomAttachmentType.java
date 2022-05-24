package com.netease.nim.uikit.business.session.myCustom.extension;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    // 多端统一
    int Guess = 1;//石头剪刀布
    int SnapChat = 2;//阅后即焚
    int Sticker = 3;//贴图
    int RTS = 4;//白板的发起结束消息

    int RedPacket = 5;//发送礼物
    int OpenedRedPacket = 6;
    int phoneStatus=7;//没有接通电话，电话状态显示
    int score=8;//发送评价
    int lianmai=9;//打开关闭摄像头提示
    int subscribe=10;//一对一预约消息
    int MultiRetweet = 15;//多条消息合并转发
    int operation = 16;//自定义主播操作
    int SystemMsg = 17;//进入直播间的系统消息
    int SystemMsgOut = 18;//取消预约
}
