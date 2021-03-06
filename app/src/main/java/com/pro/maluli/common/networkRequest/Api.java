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
     * ????????????access_token
     */
    @GET("start")
    Observable<BaseResponse<WelcomInfoEntity>> GetWelcomeInfo();

    /**
     * ??????????????????
     */
    @GET("room")
    Observable<BaseResponse<Object>> getHomeLiveList(@Query("category_id") String category_id, @Query("page") String page, @Query("limit") String limit);

    /**
     * ????????????????????????
     */
    @GET("anchor/subs")
    Observable<BaseResponse<WatchListEntity>> getWatchList(@Query("page") String page, @Query("limit") String limit);

    /**
     * ??????????????????
     */
    @GET("anchor/fans")
    Observable<BaseResponse<WatchListEntity>> getFansList(@Query("page") String page, @Query("limit") String limit);

    /**
     * ??????????????????
     */
    @GET("message/notice_list")
    Observable<BaseResponse<SystemMsgEntity>> getSystemList(@Query("page") String page);

    /**
     * ??????????????????
     */
    @GET("message/notice_list")
    Observable<BaseResponse<SystemDetailEntity>> getSystemDetail(@Query("id") String id);

    /**
     * ????????????????????????
     */
    @GET("message/anchor_appoint_list")
    Observable<BaseResponse<ReserveMessageEntity>> getReserveList(@Query("page") String page);

    /**
     * ??????????????????????????????
     */
    @GET("message/anchor_appoint_list")
    Observable<BaseResponse<ReserveDetailEntity>> getReserveDetail(@Query("id") String id);

    /**
     * ???????????????
     */
    @GET("message/appoint_list")
    Observable<BaseResponse<LineUpEntity>> getlineUpList();

    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("message/appoint_list")
    Observable<BaseResponse<Object>> getReadLineUpMsg(@Field("id") String id);

    /**
     * ?????????????????????????????????
     */
    @FormUrlEncoded
    @POST("message/notice_list")
    Observable<BaseResponse<SystemMsgEntity>> getSystemTop(@Field("id") String id);

    /**
     * ?????????????????????????????????
     */
    @FormUrlEncoded
    @POST("message/anchor_appoint_list")
    Observable<BaseResponse<SystemMsgEntity>> getReserveTop(@Field("id") String id);

    /**
     * ??????????????????
     */
    @HTTP(method = "DELETE", path = "message/notice_list", hasBody = true)
    Observable<BaseResponse<SystemMsgEntity>> getDeleteSystemTop(@Query("id") String id);

    /**
     * ??????????????????
     */
    @HTTP(method = "DELETE", path = "message/anchor_appoint_list", hasBody = true)
    Observable<BaseResponse<SystemMsgEntity>> getDeleteReverseTop(@Query("id") String id);

    /**
     * ??????????????????
     */
    @GET("notice_list")
    Observable<BaseResponse<NoticeEntity>> getNoticeList();

    /**
     * ??????????????????
     */
    @GET("notice_list")
    Observable<BaseResponse<NoticeEntity.ListBean>> getNoticeList(@Query("id") String id);

    /**
     * ??????????????????
     */
    @GET("anchor/tags")
    Observable<BaseResponse<AnchorLabelEntity>> getAnchorLabel();

    /**
     * ??????????????????
     */
    @GET("anchor/picture")
    Observable<BaseResponse<AnchorImgEntity>> getAnchorImg();

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("anchor/tags")
    Observable<BaseResponse<AnchorLabelEntity>> addAnchorLabel(@Field("name") String name);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("anchor/picture")
    Observable<BaseResponse<AnchorLabelEntity>> addAnchorImg(@Field("url") String url, @Field("is_water") String is_water);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("anchor/picture_sort")
    Observable<BaseResponse<AnchorLabelEntity>> sortAnchorImg(@Field("ids") String ids);

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("live/join")
    Observable<BaseResponse<Object>> joinLive(@Field("id") String id);

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("live/score")
    Observable<BaseResponse<JoinLiveEntity>> oneToOneScore(@Field("room_id") String room_id, @Field("professional_num") String professional_num, @Field("service_num") String service_num);

    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("live/invite_special")
    Observable<BaseResponse<SeeLiveUserEntity>> callGuestList(@Field("appoint_id") String appoint_id);

    /**
     * ?????????????????????
     */
    @GET("live/share")
    Observable<BaseResponse<ShareLiveEntity>> shareImgForOtOLive(@Query("id") String id);

    /**
     * ?????????????????????
     */
    @GET("live/apply")
    Observable<BaseResponse<ApplyLimitEntity>> subApplyLimit();


    /**
     * ?????????????????????????????????
     */
    @POST("anchor/video_limit")
    Observable<BaseResponse<CanTimeVideoEntity>> getVideoCanTime();

    /**
     * ??????????????????
     */
    @HTTP(method = "DELETE", path = "anchor/tags", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteLabel(@Query("id") String id);

    /**
     * ??????????????????
     */
    @HTTP(method = "DELETE", path = "/anchor/picture", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteImg(@Query("id") String id);

    /**
     * ???????????????
     */
    @GET("anchor/info")
    Observable<BaseResponse<AnchorInfoEntity>> getAnchorInformation(@Query("anchor_id") String anchor_id);

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("live/pay_join")
    Observable<BaseResponse<Object>> payGoInLive(@Field("id") String id);

    /**
     * ????????????????????????????????????
     */
    @GET("live/next")
    Observable<BaseResponse<SeeLiveUserEntity>> getNestLive();

    /**
     * ????????????????????????
     */
    @GET("live/live_info")
    Observable<BaseResponse<OneToOneLiveEntity>> getOneLiveInfo();

    /**
     * ??????????????????
     */
    @GET("live/gift")
    Observable<BaseResponse<GiftEntity>> getGiftInfo();

    /**
     * ??????????????????
     */
    @GET("live/room_gift")
    Observable<BaseResponse<GiftForMeEntity>> getGiftforMeLive();

    /**
     * ????????????????????????
     */
    @POST("live/stop_live")
    Observable<BaseResponse<Object>> getCloseLive();

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("live/reward")
    Observable<BaseResponse<Object>> sendGift(@Field("id") String id, @Field("gift_id") String gift_id);

    /**
     * ???????????????????????????
     */

    @FormUrlEncoded
    @POST("live/set_room_time")
    Observable<BaseResponse<Object>> setOTOLiveTime(@Field("time") String time);

    /**
     * ????????????????????????
     */

    @FormUrlEncoded
    @POST("live/add_room_time")
    Observable<BaseResponse<Object>> addOTOLiveTime(@Field("time") String time);

    /**
     * ?????????????????????
     */

    @POST("live/end_room_time")
    Observable<BaseResponse<Object>> endOTOLive();

    /**
     * ?????????????????????????????????????????????
     */

    @POST("live/end_user_invite")
    Observable<BaseResponse<Object>> jumpOTOLive();

    /**
     * ??????????????????
     */
    @GET("member_info")
    Observable<BaseResponse<UserInfoEntity>> getUserInfo();

    /**
     * ??????????????????????????????
     */
    @GET("anchor/video")
    Observable<BaseResponse<AnchorVideoEntity>> getAnchorVideo();

    /**
     * ??????????????????????????????
     */
    @HTTP(method = "DELETE", path = "/anchor/video", hasBody = true)
    Observable<BaseResponse<AnchorVideoEntity>> deleteAnchorVideo(@Query("id") String id);

    /**
     * ???????????????
     */
    @GET("message/list")
    Observable<BaseResponse<MessageListEntity>> getMessageList();

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("anchor/is_anchor")
    Observable<BaseResponse<Object>> checkIsAnchor(@Field("accid") String accid);

    /**
     * ???????????????
     */
    @POST("video/time")
    Observable<BaseResponse<VideoEntity>> videoTime();

    /**
     * ?????????????????????
     */
    @GET("video/dash")
    Observable<BaseResponse<VideoEntity>> getVideo(@Query("id") String id);

    /**
     * ?????????????????????????????????
     */
    @GET("video/comment_video")
    Observable<BaseResponse<CommentVideoEntity>> getCommentVideo(@Query("id") String id, @Query("page") String page);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("video/comment_video")
    Observable<BaseResponse<VideoEntity>> subCommentVideo(@Field("id") String id, @Field("comment") String comment);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("video/share_video")
    Observable<BaseResponse<VideoEntity>> subShareVideo(@Field("id") String id);

    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("video/download_video")
    Observable<BaseResponse<Object>> subDownloadVideo(@Field("id") String id);

    /**
     * ?????????????????????
     */
    @HTTP(method = "DELETE", path = "video/comment_video", hasBody = true)
    Observable<BaseResponse<VideoEntity>> deleteCommentVideo(@Query("id") String id, @Query("comment_id") String comment_id);

    /**
     * ????????????
     */
    @GET("user/share")
    Observable<BaseResponse<AboutMeEntity>> getAboutMeInfo();

    /**
     * ?????????????????????
     */
    @GET("user/backlist")
    Observable<BaseResponse<BlackListEntity>> getBlackList();

    /**
     * ????????????????????????
     */
    @GET("user/protocol")
    Observable<BaseResponse<ProtocolEntity>> getProtocolList();

    /**
     * ????????????????????????
     */
    @GET("user/protocol")
    Observable<BaseResponse<ProtocolDetailEntity>> getProtocoDetail(@Query("id") String id);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("user/backlist")
    Observable<BaseResponse<BlackListEntity>> getDeleteBlack(@Field("id") String id);

    /**
     * ??????????????????
     */
    @POST("read_conceal")
    Observable<BaseResponse<Object>> readConceal();

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("read_notice")
    Observable<BaseResponse<Object>> readNotic(@Field("id") String id);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("message/reward")
    Observable<BaseResponse<Object>> sendGiftForSX(@Field("gift_id") String gift_id, @Field("accid") String accid);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("message/send_score")
    Observable<BaseResponse<Object>> sendScoreForGz(@Field("accid") String accid);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("message/score")
    Observable<BaseResponse<Object>> sendEvaluationForAnchor(@Field("accid") String accid, @Field("service_num") String service_num, @Field("professional_num") String professional_num);

    /**
     * ????????????
     */
    @POST()
    Observable<BaseResponse<Object>> reOrder(@Url String url);

    /**
     * ??????????????????????????????????????????
     */
    @GET("message/chat")
    Observable<BaseResponse<MessageCanScoreEntity>> getMessageScore(@Query("accid") String accid);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("user/back_anchor")
    Observable<BaseResponse<Object>> addBlack(@Field("accid") String accid, @Field("anchor_id") String anchor_id);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("user/backlist")
    Observable<BaseResponse<Object>> removeBlack(@Field("accid") String accid, @Field("anchor_id") String anchor_id, @Field("id") String id);

    /**
     * ??????????????????????????????
     */
    @GET("message/chat_gift")
    Observable<BaseResponse<GiftForMeEntity>> getGiftListForMe(@Query("accid") String accid
    );

    /**
     * ??????????????????
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
     * ??????????????????
     */
    @GET("home")
    Observable<BaseResponse<HomeInfoEntity>> getHomeData();

    /**
     * ??????????????????
     */
    @GET("report")
    Observable<BaseResponse<FeedbackEntity>> getFeedBackList(@Query("page") String page, @Query("limit") String limit);

    /**
     * ??????????????????
     */
    @GET("live/appoint")
    Observable<BaseResponse<ReserveEntity>> getQueueInfo(@Query("anchor_id") String anchor_id);

    /**
     * ???????????????????????????
     */
    @GET("live/start")
    Observable<BaseResponse<LastTimeLiveEntity>> getLastTimeLive(@Query("type") String type);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("report")
    Observable<BaseResponse<FeedbackEntity>> getSubReport(@Field("content") String content, @Field("images") String images,
                                                          @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("anchor/draw_report")
    Observable<BaseResponse<FeedbackEntity>> getCashReport(@Field("content") String content, @Field("images") String images,
                                                           @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("live/report")
    Observable<BaseResponse<FeedbackEntity>> getLiveSubReport(@Field("content") String content, @Field("images") String images,
                                                              @Field("rel_type") String rel_type, @Field("rel") String rel);

    /**
     * ?????????????????????????????????
     */
    @FormUrlEncoded
    @POST("user/report")
    Observable<BaseResponse<FeedbackEntity>> getReport(@Field("content") String content, @Field("images") String images, @Field("rel_type") String rel_type,
                                                       @Field("rel") String rel, @Field("type") String type, @Field("report_member_id") String report_member_id,
                                                       @Field("room_id") String room_id);

    /**
     * ??????????????????
     */
    @GET("report")
    Observable<BaseResponse<FeedBackDetailEntity>> getFeedBackDetail(@Query("id") String id);

    /**
     * ????????????
     */
    @GET("pay/info")
    Observable<BaseResponse<RechargeEntity>> getPayInfo();

    /**
     * ??????????????????
     */
    @HTTP(method = "DELETE", path = "report", hasBody = true)
    Observable<BaseResponse<AnchorLabelEntity>> getDeleteFeedBack(@Query("id") String id);

    /**
     * ????????????
     * type=1 ???????????????????????????
     */
    @FormUrlEncoded
    @POST("change_password")
    Observable<BaseResponse<Object>> changePassword(@Field("phone") String phone, @Field("code") String code,
                                                    @Field("password") String password, @Field("type") String type);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("pay/invest")
    Observable<BaseResponse<PayInfoEntity>> subAliPay(@Field("type") String type, @Field("id") String id);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("pay/invest")
    Observable<BaseResponse<WechatpayEntity>> subWechatPay(@Field("type") String type, @Field("id") String id);

    /**
     * ?????????????????????
     * type=?????? 1-??????????????? 2-??????????????? (???????????????????????????) -> (?????????????????????)
     */
    @FormUrlEncoded
    @POST("change_phone")
    Observable<BaseResponse<Object>> changeBindMobile(@Field("phone") String phone, @Field("code") String code, @Field("type") String type
    );

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("anchor/sub")
    Observable<BaseResponse<Object>> removeLike(@Field("anchor_id") String phone);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("video/like_video")
    Observable<BaseResponse<Object>> likeVideo(@Field("id") String id);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("live/appoint")
    Observable<BaseResponse<Object>> subReserve(@Field("anchor_id") String anchor_id, @Field("content") String content, @Field("images") String images);

    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("live/change")
    Observable<BaseResponse<Object>> changeLiveInfo(@Field("people") String people, @Field("desc") String desc);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("member_info")
    Observable<BaseResponse<Object>> changeUserInfo(@Field("avatar") String avatar, @Field("nickname") String nickname, @Field("sex") String sex);

    /**
     * ???????????????
     */
    @GET("anchor/cash_out")
    Observable<BaseResponse<MyAccountEntity>> myAccountInfo();

    /**
     * ???????????????
     */
    @GET("anchor/cash_out_record")
    Observable<BaseResponse<BKRecordEntity>> bkWithdrawalDetail();

    /**
     * ???????????????
     */
    @GET("user/flow_log")
    Observable<BaseResponse<BKRecordEntity>> bkDetailRecord(@Query("page") String page, @Query("end_time") String end_time,
                                                            @Query("start_time") String start_time, @Query("date") String date,
                                                            @Query("flow_type") String flow_type, @Query("keyword") String keyword);

    /**
     * ??????????????????
     */
    @GET("user/anchor_flow_log")
    Observable<BaseResponse<RewardDetailEntity>> rewardDetailRecord(@Query("page") String page, @Query("end_time") String end_time,
                                                                    @Query("start_time") String start_time, @Query("date") String date,
                                                                    @Query("flow_type") String flow_type, @Query("keyword") String keyword);

    /**
     * ????????????
     */
    @GET("search")
    Observable<BaseResponse<Object>> homeSearchLive(@Query("keyword") String keyword);

    /**
     * ????????????????????????
     */
    @GET("user/flow_search_history")
    Observable<BaseResponse<SearchEntity>> getSearchHistory();

    /**
     * ????????????????????????
     */
    @GET("user/anchor_flow_search_history")
    Observable<BaseResponse<SearchEntity>> getSearchRewardHistory();

    /**
     * ????????????
     */
    @GET("anchor/cash_out_record")
    Observable<BaseResponse<WithDrawRecordEntity>> getWithdrawRecord(@Query("page") String page);

    /**
     * ????????????
     */
    @GET("search_history")
    Observable<BaseResponse<SearchEntity>> getSearchHomeHistory();

    /**
     * ??????????????????
     */
    @GET("message/search_history")
    Observable<BaseResponse<SearchEntity>> getSearchMSgHistory();

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("message/search")
    Observable<BaseResponse<Object>> searchMSg(@Field("keyword") String keyword);

    /**
     * ????????????????????????
     */
    @HTTP(method = "DELETE", path = "user/flow_search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteSearchHistory();

    /**
     * ????????????????????????
     */
    @HTTP(method = "DELETE", path = "user/anchor_flow_search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteRewardSearchHistory();

    /**
     * ??????????????????????????????
     */
    @HTTP(method = "DELETE", path = "search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteHomeSearchHistory();

    /**
     * ??????????????????????????????
     */
    @HTTP(method = "DELETE", path = "message/search_history", hasBody = true)
    Observable<BaseResponse<Object>> deleteMsgSearchHistory();

    /**
     * ??????
     */
    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<Object>> login(@Field("phone") String phone, @Field("code") String code, @Field("type") String type,
                                           @Field("password") String password, @Field("openid") String openid);

    /**
     * ??????
     */
    @FormUrlEncoded
    @POST("register")
    Observable<BaseResponse<Object>> register(@Field("phone") String phone, @Field("code") String code);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("bind_phone")
    Observable<BaseResponse<Object>> bindMobile(@Field("phone") String phone, @Field("code") String code,
                                                @Field("type") String type, @Field("openid") String openid);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("cancel_thrid_bind")
    Observable<BaseResponse<Object>> unBindWechat(@Field("type") String type);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("thrid_bind")
    Observable<BaseResponse<Object>> BindWechatAndQQ(@Field("type") String type, @Field("openid") String openid);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("anchor/bind_alipay")
    Observable<BaseResponse<Object>> BindAlipay(@Field("auth_code") String auth_code, @Field("alipay_open_id") String alipay_open_id, @Field("user_id") String user_id);

    /**
     * ???????????????
     */
    @POST("anchor/cancle_bind_alipay")
    Observable<BaseResponse<Object>> unBindAlipay();

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("send_sms")
    Observable<BaseResponse<Object>> getVerificationCode(@Field("phone") String phone, @Field("type") String type);

    /**
     * ???????????????????????????
     */
    @GET("teenager_info")
    Observable<BaseResponse<YouthEntity>> getYouthModelInfo();

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("anchor/change_desc")
    Observable<BaseResponse<Object>> changeAnchorDesc(@Field("desc") String desc);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("live/start")
    Observable<BaseResponse<OneToOneEntity>> startLive(@Field("type") String type, @Field("title") String title,
                                                       @Field("image") String image, @Field("mode") String mode,
                                                       @Field("money") String money, @Field("appoint_type") String appoint_type);

    /**
     * ??????
     */
    @FormUrlEncoded
    @POST("anchor/cash_out")
    Observable<BaseResponse<OneToOneEntity>> subCashOut(@Field("code") String code, @Field("money") String money);

    /**
     * ????????????
     */
    @POST("live/change_appoint")
    Observable<BaseResponse<OneToOneEntity>> canReserve();

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("live/cancle_appoint")
    Observable<BaseResponse<Object>> cancelReserve(@Field("anchor_id") String anchor_id);

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("set_teenager_password")
    Observable<BaseResponse<YouthEntity>> startYouth(@Field("password") String password);

    /**
     * ?????????????????????
     */
    @FormUrlEncoded
    @POST("teenager_mode")
    Observable<BaseResponse<YouthEntity>> stopYouth(@Field("password") String password);


    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("salesman/salesman/loginByWx2")
    Observable<BaseResponse<Object>> wxLogin(@Field("unionid") String unionid, @Field("refresh_token") String refresh_token,
                                             @Field("registration_id") String registrationId, @Field("device_type") String device_type);

    /**
     * ????????????
     */
    @Multipart
    @POST("https://lemon.fengxiangzb.com/admin/ajax/upload_many")
    Observable<BaseResponse<UpdateImgEntity>> uploadImage(@PartMap Map<String, RequestBody> files);

    /**
     * ????????????
     */
    @Multipart
    @POST("https://lemon.fengxiangzb.com/admin/ajax/upload_many")
    Observable<BaseResponse<UpdateImgEntity>> uploadVideo(@PartMap Map<String, RequestBody> files);

    /**
     * ???????????????
     */
    @Multipart
    @POST("anchor/video")
    Observable<BaseResponse<JoinLiveEntity>> subVideo(@PartMap Map<String, RequestBody> filesl, @Part MultipartBody.Part desc);
}
