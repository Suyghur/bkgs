package com.pro.maluli.module.message.fromUikit.messageSetting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.business.session.activity.my.MessageCanScoreEntity;
import com.netease.nim.uikit.impl.cache.StickTopCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.StickTopSessionInfo;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.module.message.fromUikit.messageSetting.presenter.IMessageSettingContraction;
import com.pro.maluli.module.message.fromUikit.messageSetting.presenter.MessageSettingPresenter;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class MessageSettingAct extends BaseMvpActivity<IMessageSettingContraction.View, MessageSettingPresenter>
        implements IMessageSettingContraction.View {

    @BindView(R.id.setTopTv)
    TextView setTopTv;
    @BindView(R.id.setTopLL)
    LinearLayout setTopLL;
    @BindView(R.id.setBlackListTv)
    TextView setBlackListTv;
    @BindView(R.id.addBlackListLL)
    LinearLayout addBlackListLL;
    @BindView(R.id.setQuiteTv)
    TextView setQuiteTv;
    @BindView(R.id.reportLL)
    LinearLayout reportLL;
    @BindView(R.id.userAvaterCiv)
    CircleImageView userAvaterCiv;
    @BindView(R.id.usernameTv)
    TextView usernameTv;
    NimUserInfo user;
    MessageCanScoreEntity messageCanScoreEntity;
    private String accid;
    private UserInfoEntity userInfoEntity;

    @Override
    public MessageSettingPresenter initPresenter() {
        return new MessageSettingPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        accid = getIntent().getStringExtra("accID");
        user = NIMClient.getService(UserService.class).getUserInfo(accid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfoEntity = (UserInfoEntity) ACache.get(this).getAsObject(ACEConstant.USERINFO);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_message_setting;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("设置");
        setBackPress();
        if (accid != null) {
            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(accid);
            setQuiteTv.setSelected(!notice);
            boolean black = NIMClient.getService(FriendService.class).isInBlackList(accid);
            setBlackListTv.setSelected(black);
        }
        if (user != null) {
            usernameTv.setText(user.getName());
            GlideUtils.loadImage(MessageSettingAct.this, user.getAvatar(), userAvaterCiv);
        }
        boolean stickTop = StickTopCache.isStickTop(accid, SessionTypeEnum.P2P);
        setTopTv.setSelected(stickTop);

    }


    @Override
    public void doBusiness() {
        presenter.getMessageCanScore(accid);
    }

    @Override
    public void setWatchInfo(WatchListEntity entity) {

    }

    @Override
    public void setMessageCanScore(MessageCanScoreEntity data) {
        messageCanScoreEntity = data;
        if (data.getIs_back() == 1) {
            setBlackListTv.setSelected(true);
        } else {
            setBlackListTv.setSelected(false);

        }
    }


    @Override
    public void setAddBlackSuccess() {
        setBlackListTv.setSelected(true);
        addBlack();
    }

    @Override
    public void removeBlackSuccess() {
        setBlackListTv.setSelected(false);
        removeBlack();
    }


    @OnClick({R.id.setTopTv, R.id.setBlackListTv, R.id.setQuiteTv, R.id.reportLL})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.setTopTv:
                if (setTopTv.isSelected()) {
                    remoreTopMessage();
                } else {
                    addTopMessage();
                }
                break;
            case R.id.setBlackListTv:
                if (setBlackListTv.isSelected()) {
                    presenter.removeBlack(accid);
                } else {
                    BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("showContent", "加入黑名单后将不可进入主播直播间、预约一对一及私信聊天");
                    bundle2.putString("TITLE_DIO", "温馨提示");
                    bundle2.putString("comfirm", "确认");
                    bundle2.putString("cancel", "取消");
                    baseTipsDialog.setArguments(bundle2);
                    baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            presenter.addBlack(accid);
                        }
                    });

                }
                break;
            case R.id.setQuiteTv:
                if (setQuiteTv.isSelected()) {
                    setQuite(true);
                } else {
                    setQuite(false);
                }
                break;
            case R.id.reportLL:
                if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                    ToastUtils.showShort("青少年模式不能使用该功能");
                    return;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString("SUB_TYPE", "5");
                bundle2.putString("reportUserid", accid);
                gotoActivity(AppealAct.class, false, bundle2);
                break;
        }
    }

    private void remoreTopMessage() {
        NIMClient.getService(MsgService.class).removeStickTopSession(accid, SessionTypeEnum.P2P, "").setCallback(
                new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        setTopTv.setSelected(false);
                        StickTopCache.recordStickTop(accid, SessionTypeEnum.P2P, false);
                        Toast.makeText(MessageSettingAct.this, "删除一个置顶会话成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(MessageSettingAct.this, "失败:" + code, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                }
        );

    }

    private void addTopMessage() {
        NIMClient.getService(MsgService.class).addStickTopSession(accid, SessionTypeEnum.P2P, "").setCallback(
                new RequestCallback<StickTopSessionInfo>() {
                    @Override
                    public void onSuccess(StickTopSessionInfo param) {
                        setTopTv.setSelected(true);
                        StickTopCache.recordStickTop(accid, SessionTypeEnum.P2P, true);
                        Toast.makeText(MessageSettingAct.this, "添加一个置顶会话成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(MessageSettingAct.this, "失败:" + code, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                }
        );

    }

    /**
     * 移除黑名单
     */
    private void removeBlack() {
        NIMClient.getService(FriendService.class).removeFromBlackList(accid)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        setBlackListTv.setSelected(false);
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }

    /**
     * 加入黑名单
     */
    private void addBlack() {
        NIMClient.getService(FriendService.class).addToBlackList(accid)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        setBlackListTv.setSelected(true);
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }


    private void setQuite(boolean isQuite) {
        NIMClient.getService(FriendService.class).setMessageNotify(accid, isQuite)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (isQuite) {
                            setQuiteTv.setSelected(false);
                        } else {
                            setQuiteTv.setSelected(true);
                        }
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }
}