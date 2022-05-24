package com.pro.maluli.common.networkRequest;

/**
 * CreateTime: 2016-03-29上午9:19
 * Author: wjkjinke00@126.com
 * Description:
 */
public interface ResponseCode {
    int SUCCESS = 200;
    int NO_LOGIN = 401;//没有登录
    int NO_LOGIN_PWD = 402;//短信登录没有设置密码
    int BIND_MOBILE = 1001;//绑定手机号
    int PAY_LIVE = 1004;//进入直播间需要付费
    int NO_MONEY = 1005;//进入直播间需要付费

}
