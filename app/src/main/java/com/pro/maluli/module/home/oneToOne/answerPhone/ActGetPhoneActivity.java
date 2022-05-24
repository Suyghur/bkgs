//package com.pro.maluli.module.home.oneToOne.answerPhone;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CancellationSignal;
//import android.provider.SyncStateContract;
//import android.view.View;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.netease.lava.nertc.sdk.NERtcEx;
//import com.netease.lava.nertc.sdk.video.NERtcVideoConfig;
//import com.netease.lava.nertc.sdk.video.NERtcVideoView;
//import com.netease.nim.uikit.business.robot.parser.elements.group.LinearLayout;
//import com.netease.yunxin.nertc.nertcvideocall.bean.InvitedInfo;
//import com.netease.yunxin.nertc.nertcvideocall.model.NERTCCallingDelegate;
//import com.netease.yunxin.nertc.ui.base.CallParam;
//import com.netease.yunxin.nertc.ui.base.CommonCallActivity;
//import com.netease.yunxin.nertc.ui.p2p.NERtcCallDelegateForP2P;
//import com.pro.maluli.R;
//import com.pro.maluli.common.utils.glideImg.GlideUtils;
//import com.pro.maluli.module.home.startLive.StartLiveAct;
//
//import org.jetbrains.annotations.NotNull;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ActGetPhoneActivity extends CommonCallActivity {
//    private boolean startPreview = false;
//    private CircleImageView avatarCiv;
//    private TextView userNameTv;
//    private String roomId;
//    private String avatar;
//    private String userName;
//    private String uid;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        avatarCiv = findViewById(R.id.avatarCiv);
//        userNameTv = (TextView) findViewById(R.id.userNameTv);
//
//
//        // 被叫拒绝
//        View reject = findViewById(R.id.ivReject);
//        reject.setOnClickListener(new View.OnClickListener() {
//                                      @Override
//                                      public void onClick(View v) {
//                                          ActGetPhoneActivity.this.doReject(null);
////                                          Intent intent = new Intent(ActGetPhoneActivity.this, StartLiveAct.class);
////                                          intent.putExtra("ROOMID", roomId);
////                                          intent.putExtra("UID", uid);
////                                          startActivity(intent);
////                                          gotoActivity(StartLiveAct.class, false);
//                                      }
//                                  }
//
//        );
//
//        // 被叫接受
//        View accept = findViewById(R.id.ivAccept);
//        accept.setOnClickListener(v -> doReject(null));
//
////        View hangup = findViewById(R.id.ivHangUp);
////        hangup.setOnClickListener(v -> {
////            doHangup(null);
////            finish();
////        });
//        CallParam ada = getCallParam();
//
////       SyncStateContract.Helpers.get();
//    }
//
//    @Override// 资源释放
//    protected void releaseAndFinish(boolean finishCall) {
//        super.releaseAndFinish(finishCall);
//        if (finishCall) {
//            doHangup(null);
//        }
//    }
//
//    @Override
//    protected int provideLayoutId() {
//        return R.layout.act_get_phone;
//    }
//
//    @NotNull
//    @Override
//    protected NERTCCallingDelegate provideRtcDelegate() {
//        return new NERtcCallDelegateForP2P() {
//
//            @Override
//            public void onUserEnter(@Nullable String userId) {
//                uid = userId;
//            }
//
//            @Override
//            public void onInvited(@org.jetbrains.annotations.Nullable InvitedInfo invitedInfo) {
//                super.onInvited(invitedInfo);
//
////                String doctorInfomation = invitedInfo.attachment;
////                JSONObject json = JSONObject.parseObject(doctorInfomation);
////                roomId = json.getString("roomId");
////                avatar = json.getString("avatar");
////                userName = json.getString("userName");
////                GlideUtils.loadImage(ActGetPhoneActivity.this, avatar, avatarCiv);
////                userNameTv.setText(userName);
//            }
//
//            @Override // 主要用户取消
//            public void onCancelByUserId(@Nullable String userId) {
//                super.onCancelByUserId(userId);
//                finish();
//            }
//
//            @Override// 被叫用户拒绝
//            public void onRejectByUserId(@Nullable String userId) {
//                super.onRejectByUserId(userId);
//                finish();
//            }
//
//            @Override// 通过结束
//            public void onCallEnd(@Nullable String userId) {
//                super.onCallEnd(userId);
//                finish();
//            }
//
//            @Override// 被叫用户占线
//            public void onUserBusy(@Nullable String userId) {
//                super.onUserBusy(userId);
//                finish();
//            }
//        };
//    }
//}