package com.pro.maluli.common.networkRequest;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.entity.AnchorVideoEntity;
import com.pro.maluli.common.entity.ApplyLimitEntity;
import com.pro.maluli.common.entity.BKRecordEntity;
import com.pro.maluli.common.entity.BlackListEntity;
import com.pro.maluli.common.entity.CanTimeVideoEntity;
import com.pro.maluli.common.entity.CommentVideoEntity;
import com.pro.maluli.common.entity.FeedBackDetailEntity;
import com.pro.maluli.common.entity.FeedbackEntity;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.JoinLiveEntity;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.LineUpEntity;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.OneToOneLiveEntity;
import com.pro.maluli.common.entity.PayInfoEntity;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.entity.ProtocolEntity;
import com.pro.maluli.common.entity.RechargeEntity;
import com.pro.maluli.common.entity.ReserveDetailEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.ShareLiveEntity;
import com.pro.maluli.common.entity.SystemDetailEntity;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.entity.WechatpayEntity;
import com.pro.maluli.common.entity.WelcomInfoEntity;
import com.pro.maluli.common.entity.WithDrawRecordEntity;
import com.pro.maluli.common.entity.YouthEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {


    /**
     * 获取微信access_token
     */
    @GET("start")
    Observable<BaseResponse<WelcomInfoEntity>> GetWelcomeInfo();

    /**
     * 获取首页登录
     */
    @GET("room")
    Observable<BaseResponse<Object>> getHomeLiveList(@Query("category_id") String category_id, @Query("page") String page, @Query("limit") String limit);

    /**
     * 获取主播关注列表
     */
    @GET("anchor/subs")
    Observable<BaseResponse<WatchListEntity>> getWatchList(@Query("page") String page, @Query("limit") String limit);

    /**
     * 获取粉丝列表
     */
    @GET("anchor/fans")
    Observable<BaseResponse<WatchListEntity>> getFansList(@Query("page") String page, @Query("limit") String limit);

    /**
     * 获取系统通知
     */
    @GET("message/notice_list")
    Observable<BaseResponse<SystemMsgEntity>> getSystemList(@Query("page") String page);

    /**
     * 获取系统通知
     */
    @GET("message/notice_list")
    Observable<BaseResponse<SystemDetailEntity>> getSystemDetail(@Query("id") String id);

    /**
     * 获取预约消息通知
     */
    @GET("message/anchor_appoint_list")
    Observable<BaseResponse<ReserveMessageEntity>> getReserveList(@Query("page") String page);

    /**
     * 获取预约消息通知详情
     */
    @GET("message/anchor_appoint_list")
    Observable<BaseResponse<ReserveDetailEntity>> getReserveDetail(@Query("id") String id);

    /**
     * 预约排队也
     */
    @GET("message/appoint_list")
    Observable<BaseResponse<LineUpEntity>> getlineUpList();

    /**
     * 预约排队已读接口
     */
    @FormUrlEncoded
    @POST("message/appoint_list")
    Observable<BaseResponse<Object>> getReadLineUpMsg(@Field("id") String id);

    /**
     * 系统通知置顶和取消置顶
     */
    @FormUrlEncoded
    @POST("message/notice_list")
    Observable<BaseResponse<SystemMsgEntity>> getSystemTop(@Field("id") String id);

    /**
     * 系统通知置顶和取消置顶
     */
    @FormUrlEncoded
    @POST("message/anchor_appoint_list")
    Observable<BaseResponse<SystemMsgEntity>> getReserveTop(@Field("id") String id);

    /**
     * 系统通知删除
     */
    @HTTP(method = "DELETE", path = "message/notice_list", hasBody = true)
    Observable<BaseResponse<SystemMsgEntity>> getDeleteSystemTop(@Query("id") String id);

    /**
     * 删除预约通知
     */
    @HTTP(method = "DELETE", path = "message/anchor_appoint_list", hasBody = true)
    Observable<BaseResponse<SystemMsgEntity>> getDeleteReverseTop(@Query("id") String id);

    /**
     * 首页公告列表
     */
    @GET("notice_list")
    Observable<BaseResponse<NoticeEntity>> getNoticeList();

    /**
     * 首页公告详情
     */
    @GET("notice_list")
    Observable<BaseResponse<NoticeEntity.ListBean>> getNoticeList(@Query("id") String id);

    /**
     * 获取主播标签
     */
    @GET("anchor/tags")
    Observable<BaseResponse<AnchorLabelEntity>> getAnchorLabel();

    /**
     * 获取主播图片
     */
    @GET("anchor/picture")
    Observable<BaseResponse<AnchorImgEntity>> getAnchorImg();

    /**
     * 添加主播标签
     */
    @FormUrlEncoded
    @POST("anchor/tags")
    Observable<BaseResponse<AnchorLabelEntity>> addAnchorLabel(@Field("name") String name);

    /**
     * 添加主播图片
     */
    @FormUrlEncoded
    @POST("anchor/picture")
    Observable<BaseResponse<AnchorLabelEntity>> addAnchorImg(@Field("url") String url, @Field("is_water") String is_water);

    /**
     * 添加主播图片
     */
    @FormUrlEncoded
    @POST("anchor/picture_sort")
    Observable<BaseResponse<AnchorLabelEntity>> sortAnchorImg(@Field("ids") String ids);

    /**
     * 进入直播间接口
     */
    @FormUrlEncoded
    @POST("live/join")
    Observable<BaseResponse<Object>> joinLive(@Field("id") String id);

    /**
     * 一对一直播评分
     */
    @FormUrlEncoded
    @POST("live/score")
    Observable<BaseResponse<JoinLiveEntity>> oneToOneScore(@Field("room_id") String room_id, @Field("professional_num") String professional_num, @Field("service_num") String service_num);

    /**
     * 邀请特邀列表名单
     */
    @FormUrlEncoded
    @POST("live/invite_special")
    Observable<BaseResponse<SeeLiveUserEntity>> callGuestList(@Field("appoint_id") String appoint_id);

    /**
     * 一对一直播分享
     */
    @GET("live/share")
    Observable<BaseResponse<ShareLiveEntity>> shareImgForOtOLive(@Query("id") String id);

    /**
     * 一对一直播分享
     */
    @GET("live/apply")
    Observable<BaseResponse<ApplyLimitEntity>> subApplyLimit();


    /**
     * 上传小视频最大时长限制
     */
    @POST("anchor/video_limit")
    Observable<BaseResponse<CanTimeVideoEntity>> getVideoCanTime();

    /**
     * 删除个人标签
     */
    @HTTP(method = "DELETE", path = "anchor/tags", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteLabel(@Query("id") String id);

    /**
     * 删除个人图片
     */
    @HTTP(method = "DELETE", path = "/anchor/picture", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteImg(@Query("id") String id);

    /**
     * 主播信息页
     */
    @GET("anchor/info")
    Observable<BaseResponse<AnchorInfoEntity>> getAnchorInformation(@Query("anchor_id") String anchor_id);

    /**
     * 支付进入直播间
     */
    @FormUrlEncoded
    @POST("live/pay_join")
    Observable<BaseResponse<Object>> payGoInLive(@Field("id") String id);

    /**
     * 获取主播一对一下一位数据
     */
    @GET("live/next")
    Observable<BaseResponse<SeeLiveUserEntity>> getNestLive();

    /**
     * 获取主播信息页面
     */
    @GET("live/live_info")
    Observable<BaseResponse<OneToOneLiveEntity>> getOneLiveInfo();

    /**
     * 获取礼物列表
     */
    @GET("live/gift")
    Observable<BaseResponse<GiftEntity>> getGiftInfo();

    /**
     * 获取礼物列表
     */
    @GET("live/room_gift")
    Observable<BaseResponse<GiftForMeEntity>> getGiftforMeLive();

    /**
     * 关闭一对一直播间
     */
    @POST("live/stop_live")
    Observable<BaseResponse<Object>> getCloseLive();

    /**
     * 打赏礼物
     */
    @FormUrlEncoded
    @POST("live/reward")
    Observable<BaseResponse<Object>> sendGift(@Field("id") String id, @Field("gift_id") String gift_id);

    /**
     * 设置一对一直播时间
     */

    @FormUrlEncoded
    @POST("live/set_room_time")
    Observable<BaseResponse<Object>> setOTOLiveTime(@Field("time") String time);

    /**
     * 加一对一直播时间
     */

    @FormUrlEncoded
    @POST("live/add_room_time")
    Observable<BaseResponse<Object>> addOTOLiveTime(@Field("time") String time);

    /**
     * 结束本场直播间
     */

    @POST("live/end_room_time")
    Observable<BaseResponse<Object>> endOTOLive();

    /**
     * 跳过直播间，邀请下一位，直播间
     */

    @POST("live/end_user_invite")
    Observable<BaseResponse<Object>> jumpOTOLive();

    /**
     * 获取用户信息
     */
    @GET("member_info")
    Observable<BaseResponse<UserInfoEntity>> getUserInfo();

    /**
     * 获取直播小视频列表页
     */
    @GET("anchor/video")
    Observable<BaseResponse<AnchorVideoEntity>> getAnchorVideo();

    /**
     * 获取直播小视频列表页
     */
    @HTTP(method = "DELETE", path = "/anchor/video", hasBody = true)
    Observable<BaseResponse<AnchorVideoEntity>> deleteAnchorVideo(@Query("id") String id);

    /**
     * 我的消息页
     */
    @GET("message/list")
    Observable<BaseResponse<MessageListEntity>> getMessageList();

    /**
     * 我的消息页
     */
    @FormUrlEncoded
    @POST("anchor/is_anchor")
    Observable<BaseResponse<Object>> checkIsAnchor(@Field("accid") String accid);

    /**
     * 我的消息页
     */
    @POST("video/time")
    Observable<BaseResponse<VideoEntity>> videoTime();

    /**
     * 获取小视频数据
     */
    @GET("video/dash")
    Observable<BaseResponse<VideoEntity>> getVideo(@Query("id") String id);

    /**
     * 获取小视频评论视频列表
     */
    @GET("video/comment_video")
    Observable<BaseResponse<CommentVideoEntity>> getCommentVideo(@Query("id") String id, @Query("page") String page);

    /**
     * 小视频评论
     */
    @FormUrlEncoded
    @POST("video/comment_video")
    Observable<BaseResponse<VideoEntity>> subCommentVideo(@Field("id") String id, @Field("comment") String comment);

    /**
     * 分享小视频
     */
    @FormUrlEncoded
    @POST("video/share_video")
    Observable<BaseResponse<VideoEntity>> subShareVideo(@Field("id") String id);

    /**
     * 下载小视频小视频
     */
    @FormUrlEncoded
    @POST("video/download_video")
    Observable<BaseResponse<Object>> subDownloadVideo(@Field("id") String id);

    /**
     * 删除小视频评论
     */
    @HTTP(method = "DELETE", path = "video/comment_video", hasBody = true)
    Observable<BaseResponse<VideoEntity>> deleteCommentVideo(@Query("id") String id, @Query("comment_id") String comment_id);

    /**
     * 关于我们
     */
    @GET("user/share")
    Observable<BaseResponse<AboutMeEntity>> getAboutMeInfo();

    /**
     * 获取黑名单列表
     */
    @GET("user/backlist")
    Observable<BaseResponse<BlackListEntity>> getBlackList();

    /**
     * 用户协议列表数据
     */
    @GET("user/protocol")
    Observable<BaseResponse<ProtocolEntity>> getProtocolList();

    /**
     * 用户协议列表数据
     */
    @GET("user/protocol")
    Observable<BaseResponse<ProtocolDetailEntity>> getProtocoDetail(@Query("id") String id);

    /**
     * 删除黑名单
     */
    @FormUrlEncoded
    @POST("user/backlist")
    Observable<BaseResponse<BlackListEntity>> getDeleteBlack(@Field("id") String id);

    /**
     * 确认隐私协议
     */
    @POST("read_conceal")
    Observable<BaseResponse<Object>> readConceal();

    /**
     * 确认隐私协议
     */
    @FormUrlEncoded
    @POST("read_notice")
    Observable<BaseResponse<Object>> readNotic(@Field("id") String id);

    /**
     * 私信打赏礼物
     */
    @FormUrlEncoded
    @POST("message/reward")
    Observable<BaseResponse<Object>> sendGiftForSX(@Field("gift_id") String gift_id, @Field("accid") String accid);

    /**
     * 私信打赏礼物
     */
    @FormUrlEncoded
    @POST("message/send_score")
    Observable<BaseResponse<Object>> sendScoreForGz(@Field("accid") String accid);

    /**
     * 私信评价主播
     */
    @FormUrlEncoded
    @POST("message/score")
    Observable<BaseResponse<Object>> sendEvaluationForAnchor(@Field("accid") String accid, @Field("service_num") String service_num, @Field("professional_num") String professional_num);

    /**
     * 再次预约
     */
    @POST()
    Observable<BaseResponse<Object>> reOrder(@Url String url);

    /**
     * 获取聊天是否可以评分信息页面
     */
    @GET("message/chat")
    Observable<BaseResponse<MessageCanScoreEntity>> getMessageScore(@Query("accid") String accid);

    /**
     * 拉入黑名单
     */
    @FormUrlEncoded
    @POST("user/back_anchor")
    Observable<BaseResponse<Object>> addBlack(@Field("accid") String accid, @Field("anchor_id") String anchor_id);

    /**
     * 移除黑名单
     */
    @FormUrlEncoded
    @POST("user/backlist")
    Observable<BaseResponse<Object>> removeBlack(@Field("accid") String accid, @Field("anchor_id") String anchor_id, @Field("id") String id);

    /**
     * 私信打赏礼物获取列表
     */
    @GET("message/chat_gift")
    Observable<BaseResponse<GiftForMeEntity>> getGiftListForMe(@Query("accid") String accid
    );

    /**
     * 上传主播认证
     */
    @FormUrlEncoded
    @POST("live/apply")
    Observable<BaseResponse<BlackListEntity>> submitApplyForAnchor(@Field("level") String level,
                                                                   @Field("realname") String realname,
                                                                   @Field("phone") String phone,
                                                                   @Field("card_id") String card_id,
                                                                   @Field("desc") String desc,
                                                                   @Field("font_card_image") String font_card_image,
                                                                   @Field("back_card_image") String back_card_image,
                                                                   @Field("holding_card_image") String holding_card_image,
                                                                   @Field("certificate_image") String certificate_image);

    /**
     * 获取首页信息
     */
    @GET("home")
    Observable<BaseResponse<HomeInfoEntity>> getHomeData();

    /**
     * 意见反馈列表
     */
    @GET("report")
    Observable<BaseResponse<FeedbackEntity>> getFeedBackList(@Query("page") String page, @Query("limit") String limit);

    /**
     * 预约页面信息
     */
    @GET("live/appoint")
    Observable<BaseResponse<ReserveEntity>> getQueueInfo(@Query("anchor_id") String anchor_id);

    /**
     * 获取上一次开播信息
     */
    @GET("live/start")
    Observable<BaseResponse<LastTimeLiveEntity>> getLastTimeLive(@Query("type") String type);

    /**
     * 提交意见反馈
     */
    @FormUrlEncoded
    @POST("report")
    Observable<BaseResponse<FeedbackEntity>> getSubReport(@Field("content") String content, @Field("images") String images,
                                                          @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * 冻结申诉
     */
    @FormUrlEncoded
    @POST("anchor/draw_report")
    Observable<BaseResponse<FeedbackEntity>> getCashReport(@Field("content") String content, @Field("images") String images,
                                                           @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * 主播申诉
     */
    @FormUrlEncoded
    @POST("live/report")
    Observable<BaseResponse<FeedbackEntity>> getLiveSubReport(@Field("content") String content, @Field("images") String images,
                                                              @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * 举报是直播间，或者私信
     */
    @FormUrlEncoded
    @POST("user/report")
    Observable<BaseResponse<FeedbackEntity>> getReport(@Field("content") String content, @Field("images") String images, @Field("rel_type") String rel_type,
                                                       @Field("rel") String rel, @Field("type") String type, @Field("report_member_id") String report_member_id,
                                                       @Field("room_id") String room_id);

    /**
     * 意见反馈详情
     */
    @GET("report")
    Observable<BaseResponse<FeedBackDetailEntity>> getFeedBackDetail(@Query("id") String id);

    /**
     * 充值页面
     */
    @GET("pay/info")
    Observable<BaseResponse<RechargeEntity>> getPayInfo();

    /**
     * 删除意见反馈
     */
    @HTTP(method = "DELETE", path = "report", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteFeedBack(@Query("id") String id);

    /**
     * 修改密码
     * type=1 验证验证码是否正确
     */
    @FormUrlEncoded
    @POST("change_password")
    Observable<BaseResponse<Object>> changePassword(@Field("phone") String phone, @Field("code") String code,
                                                    @Field("password") String password, @Field("type") String type);

    /**
     * 提交支付信息
     */
    @FormUrlEncoded
    @POST("pay/invest")
    Observable<BaseResponse<PayInfoEntity>> subAliPay(@Field("type") String type, @Field("id") String id);

    /**
     * 提交支付信息
     */
    @FormUrlEncoded
    @POST("pay/invest")
    Observable<BaseResponse<WechatpayEntity>> subWechatPay(@Field("type") String type, @Field("id") String id);

    /**
     * 更改绑定手机号
     * type=类型 1-验证手机号 2-更改手机号 (第一步先验证手机号) -> (第二步更改手机)
     */
    @FormUrlEncoded
    @POST("change_phone")
    Observable<BaseResponse<Object>> changeBindMobile(@Field("phone") String phone, @Field("code") String code, @Field("type") String type
    );

    /**
     * 取消关注
     */
    @FormUrlEncoded
    @POST("anchor/sub")
    Observable<BaseResponse<Object>> removeLike(@Field("anchor_id") String phone);

    /**
     * 点赞小视频
     */
    @FormUrlEncoded
    @POST("video/like_video")
    Observable<BaseResponse<Object>> likeVideo(@Field("id") String id);

    /**
     * 提交预约信息
     */
    @FormUrlEncoded
    @POST("live/appoint")
    Observable<BaseResponse<Object>> subReserve(@Field("anchor_id") String anchor_id, @Field("content") String content, @Field("images") String images);

    /**
     * 更改预约页面信息
     */
    @FormUrlEncoded
    @POST("live/change")
    Observable<BaseResponse<Object>> changeLiveInfo(@Field("people") String people, @Field("desc") String desc);

    /**
     * 修改用户信息
     */
    @FormUrlEncoded
    @POST("member_info")
    Observable<BaseResponse<Object>> changeUserInfo(@Field("avatar") String avatar, @Field("nickname") String nickname, @Field("sex") String sex);

    /**
     * 提现信息页
     */
    @GET("anchor/cash_out")
    Observable<BaseResponse<MyAccountEntity>> myAccountInfo();

    /**
     * 提现信息页
     */
    @GET("anchor/cash_out_record")
    Observable<BaseResponse<BKRecordEntity>> bkWithdrawalDetail();

    /**
     * 提现信息页
     */
    @GET("user/flow_log")
    Observable<BaseResponse<BKRecordEntity>> bkDetailRecord(@Query("page") String page, @Query("end_time") String end_time,
                                                            @Query("start_time") String start_time, @Query("date") String date,
                                                            @Query("flow_type") String flow_type, @Query("keyword") String keyword);

    /**
     * 收到打赏明细
     */
    @GET("user/anchor_flow_log")
    Observable<BaseResponse<RewardDetailEntity>> rewardDetailRecord(@Query("page") String page, @Query("end_time") String end_time,
                                                                    @Query("start_time") String start_time, @Query("date") String date,
                                                                    @Query("flow_type") String flow_type, @Query("keyword") String keyword);

    /**
     * 首页搜素
     */
    @GET("search")
    Observable<BaseResponse<Object>> homeSearchLive(@Query("keyword") String keyword);

    /**
     * 获取搜索历史列表
     */
    @GET("user/flow_search_history")
    Observable<BaseResponse<SearchEntity>> getSearchHistory();

    /**
     * 获取搜索历史列表
     */
    @GET("user/anchor_flow_search_history")
    Observable<BaseResponse<SearchEntity>> getSearchRewardHistory();

    /**
     * 提现记录
     */
    @GET("anchor/cash_out_record")
    Observable<BaseResponse<WithDrawRecordEntity>> getWithdrawRecord(@Query("page") String page);

    /**
     * 首页搜索
     */
    @GET("search_history")
    Observable<BaseResponse<SearchEntity>> getSearchHomeHistory();

    /**
     * 消息搜索记录
     */
    @GET("message/search_history")
    Observable<BaseResponse<SearchEntity>> getSearchMSgHistory();

    /**
     * 消息搜索记录
     */
    @FormUrlEncoded
    @POST("message/search")
    Observable<BaseResponse<Object>> searchMSg(@Field("keyword") String keyword);

    /**
     * 获取搜索历史列表
     */
    @HTTP(method = "DELETE", path = "user/flow_search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteSearchHistory();

    /**
     * 清空打赏搜索记录
     */
    @HTTP(method = "DELETE", path = "user/anchor_flow_search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteRewardSearchHistory();

    /**
     * 清空首页搜索搜索记录
     */
    @HTTP(method = "DELETE", path = "search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteHomeSearchHistory();

    /**
     * 清空聊天搜索搜索记录
     */
    @HTTP(method = "DELETE", path = "message/search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteMsgSearchHistory();

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<Object>> login(@Field("phone") String phone, @Field("code") String code, @Field("type") String type,
                                           @Field("password") String password, @Field("openid") String openid);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("register")
    Observable<BaseResponse<Object>> register(@Field("phone") String phone, @Field("code") String code);

    /**
     * 绑定手机
     */
    @FormUrlEncoded
    @POST("bind_phone")
    Observable<BaseResponse<Object>> bindMobile(@Field("phone") String phone, @Field("code") String code,
                                                @Field("type") String type, @Field("openid") String openid);

    /**
     * 取消绑定微信
     */
    @FormUrlEncoded
    @POST("cancel_thrid_bind")
    Observable<BaseResponse<Object>> unBindWechat(@Field("type") String type);

    /**
     * 取消绑定微信
     */
    @FormUrlEncoded
    @POST("thrid_bind")
    Observable<BaseResponse<Object>> BindWechatAndQQ(@Field("type") String type, @Field("openid") String openid);

    /**
     * 绑定支付宝
     */
    @FormUrlEncoded
    @POST("anchor/bind_alipay")
    Observable<BaseResponse<Object>> BindAlipay(@Field("auth_code") String auth_code, @Field("alipay_open_id") String alipay_open_id, @Field("user_id") String user_id);

    /**
     * 解绑支付宝
     */
    @POST("anchor/cancle_bind_alipay")
    Observable<BaseResponse<Object>> unBindAlipay();

    /**
     * 获取手机验证码
     */
    @FormUrlEncoded
    @POST("send_sms")
    Observable<BaseResponse<Object>> getVerificationCode(@Field("phone") String phone, @Field("type") String type);

    /**
     * 获取青少年模式信息
     */
    @GET("teenager_info")
    Observable<BaseResponse<YouthEntity>> getYouthModelInfo();

    /**
     * 主播个人简介
     */
    @FormUrlEncoded
    @POST("anchor/change_desc")
    Observable<BaseResponse<Object>> changeAnchorDesc(@Field("desc") String desc);

    /**
     * 主播开始直播
     */
    @FormUrlEncoded
    @POST("live/start")
    Observable<BaseResponse<OneToOneEntity>> startLive(@Field("type") String type, @Field("title") String title,
                                                       @Field("image") String image, @Field("mode") String mode,
                                                       @Field("money") String money, @Field("appoint_type") String appoint_type);

    /**
     * 提现
     */
    @FormUrlEncoded
    @POST("anchor/cash_out")
    Observable<BaseResponse<OneToOneEntity>> subCashOut(@Field("code") String code, @Field("money") String money);

    /**
     * 接受预约
     */
    @POST("live/change_appoint")
    Observable<BaseResponse<OneToOneEntity>> canReserve();

    /**
     * 用户取消预约
     */
    @FormUrlEncoded
    @POST("live/cancle_appoint")
    Observable<BaseResponse<Object>> cancelReserve(@Field("anchor_id") String anchor_id);

    /**
     * 开启青少年模式
     */
    @FormUrlEncoded
    @POST("set_teenager_password")
    Observable<BaseResponse<YouthEntity>> startYouth(@Field("password") String password);

    /**
     * 关闭青少年模式
     */
    @FormUrlEncoded
    @POST("teenager_mode")
    Observable<BaseResponse<YouthEntity>> stopYouth(@Field("password") String password);


    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST("salesman/salesman/loginByWx2")
    Observable<BaseResponse<Object>> wxLogin(@Field("unionid") String unionid, @Field("refresh_token") String refresh_token,
                                             @Field("registration_id") String registrationId, @Field("device_type") String device_type);

    /**
     * 上传图片
     */
    @Multipart
    @POST("https://lemon.fengxiangzb.com/admin/ajax/upload_many")
    Observable<BaseResponse<UpdateImgEntity>> uploadImage(@PartMap Map<String, RequestBody> files);

    /**
     * 上传图片
     */
    @Multipart
    @POST("https://lemon.fengxiangzb.com/admin/ajax/upload_many")
    Observable<BaseResponse<UpdateImgEntity>> uploadVideo(@PartMap Map<String, RequestBody> files);

    /**
     * 添加小视频
     */
    @Multipart
    @POST("anchor/video")
    Observable<BaseResponse<JoinLiveEntity>> subVideo(@PartMap Map<String, RequestBody> filesl, @Part MultipartBody.Part desc);
}
