package com.pro.maluli.module.myself.anchorInformation.anchorMore;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.module.myself.anchorInformation.anchorMore.presenter.AnchorMorePresenter;
import com.pro.maluli.module.myself.anchorInformation.anchorMore.presenter.IAnchorMoreContraction;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AnchorMoreAct extends BaseMvpActivity<IAnchorMoreContraction.View, AnchorMorePresenter>
        implements IAnchorMoreContraction.View {


    @BindView(R.id.setBlackListTv)
    TextView setBlackListTv;
    private String isBlack;
    private String anchorID;
    private String accid;

    @Override
    public AnchorMorePresenter initPresenter() {
        return new AnchorMorePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        isBlack = getIntent().getStringExtra("IS_BLACK");
        anchorID = getIntent().getStringExtra("anchorID");
        accid = getIntent().getStringExtra("anchorAccuid");

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_anchor_more;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("更多");
        setBackPress();
        if ("1".equalsIgnoreCase(isBlack)){
            setBlackListTv.setSelected(true);
        }else {
            setBlackListTv.setSelected(false);

        }

    }

    @Override
    public void setAddBlackSuccess() {
        setBlackListTv.setSelected(true);
        addBlack();
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
    @Override
    public void removeBlackSuccess() {
        setBlackListTv.setSelected(false);
        removeBlack();
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
    @OnClick({R.id.reportLL, R.id.setBlackListTv})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.reportLL:
                Bundle bundle2 =new Bundle();
                bundle2.putString("SUB_TYPE","2");
                bundle2.putString("liveId",anchorID);
                gotoActivity(AppealAct.class,false,bundle2);
                break;
            case R.id.setBlackListTv:
                if (setBlackListTv.isSelected()){
                    presenter.removeBlack(anchorID);
                }else {
                    BaseTipsDialog baseTipsDialog =new BaseTipsDialog();
                    Bundle bundleBaseTipsDialog = new Bundle();
                    bundleBaseTipsDialog.putString("showContent", "加入黑名单后将不可进入主播直播间、预约一对一及私信聊天");
                    bundleBaseTipsDialog.putString("TITLE_DIO", "温馨提示");
                    bundleBaseTipsDialog.putString("comfirm", "确认");
                    bundleBaseTipsDialog.putString("cancel", "取消");
                    baseTipsDialog.setArguments(bundleBaseTipsDialog);
                    baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            presenter.addBlack(anchorID);
                        }
                    });


//                    presenter.addBlack(anchorID);

                }
                break;
        }
    }

    @Override
    public void doBusiness() {
    }


}