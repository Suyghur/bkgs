package com.pro.maluli.module.myself.myAccount.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.FreezeTipsDialog;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;
import com.pro.maluli.module.myself.myAccount.base.presenter.IMyAccountContraction;
import com.pro.maluli.module.myself.myAccount.base.presenter.MyAccountPresenter;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;
import com.pro.maluli.module.myself.myAccount.recharge.detail.BkDetailAct;
import com.pro.maluli.module.myself.myAccount.withdraw.base.WithdrawAct;
import com.pro.maluli.module.myself.myAccount.withdraw.detail.RewardDetailAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class MyAccountAct extends BaseMvpActivity<IMyAccountContraction.View, MyAccountPresenter> implements IMyAccountContraction.View {


    @BindView(R.id.mybkbTV)
    TextView mybkbTV;
    @BindView(R.id.myBkImg)
    ImageView myBkImg;
    @BindView(R.id.gotoRechargeTv)
    TextView gotoRechargeTv;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.detailBkbLL)
    LinearLayout detailBkbLL;
    @BindView(R.id.getbkbTV)
    TextView getbkbTV;
    @BindView(R.id.gotoWithdrawTv)
    TextView gotoWithdrawTv;
    @BindView(R.id.detailGetBkbLL)
    LinearLayout detailGetBkbLL;
    @BindView(R.id.dashangLL)
    LinearLayout dashangLL;
    UserInfoEntity userInfoEntity;

    @Override
    public MyAccountPresenter initPresenter() {
        return new MyAccountPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        userInfoEntity = (UserInfoEntity) ACache.get(this).getAsObject(ACEConstant.USERINFO);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_my_account;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("我的账户");
        setBackPress();
    }


    @Override
    public void doBusiness() {
//        presenter.getMyAccount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getUserInfo();
//        presenter.getCashOutInfo();
    }

    @OnClick({R.id.gotoRechargeTv, R.id.gotoWithdrawTv, R.id.detailBkbLL, R.id.detailGetBkbLL})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.gotoRechargeTv:
                gotoActivity(RechargeAct.class);
                break;
            case R.id.gotoWithdrawTv:

                if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity.getBind_alipay())) {
                    presenter.getMyAccount();

                } else {
                    ToastUtils.showShort("绑定支付宝方能提现");
                }
                break;
            case R.id.detailBkbLL:
                gotoActivity(BkDetailAct.class);
                break;
            case R.id.detailGetBkbLL://打赏收支
                gotoActivity(RewardDetailAct.class);
                break;
        }
    }


    @Override
    public void setUserInfoSuccess(UserInfoEntity data) {
        mybkbTV.setText(data.getMoney() + "");
        getbkbTV.setText(data.getAnchor_money() + "");
        if (data.getIs_anchor() == 1) {
            dashangLL.setVisibility(View.VISIBLE);
        } else {
            dashangLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void setAccountInfo(MyAccountEntity data) {
        if (!TextUtils.isEmpty(data.getCode())) {
            if (data.getCode().equalsIgnoreCase("3001")) {
                FreezeTipsDialog dialogFragment = new FreezeTipsDialog();
                Bundle bundle = new Bundle();
                bundle.putString("showTips", data.getReason());
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "SelectTimeDialog");
                dialogFragment.setOnConfirmListener(new FreezeTipsDialog.OnFreezeTipsListener() {
                    @Override
                    public void gotoAppeal(int type) {
                        Bundle bundle2 =new Bundle();
                        bundle2.putString("SUB_TYPE","4");
                        gotoActivity(AppealAct.class,false,bundle2);
                    }
                });

            } else if (data.getCode().equalsIgnoreCase("3003")){//待审核
                FreezeTipsDialog dialogFragment = new FreezeTipsDialog();
                Bundle bundle = new Bundle();
                bundle.putString("showTips", "申诉已提交,请耐心等待审核");
                bundle.putBoolean("goAppeal", false);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "SelectTimeDialog");
                dialogFragment.setOnConfirmListener(new FreezeTipsDialog.OnFreezeTipsListener() {
                    @Override
                    public void gotoAppeal(int type) {
                        Bundle bundle2 =new Bundle();
                        bundle2.putString("SUB_TYPE","4");
                        gotoActivity(AppealAct.class,false,bundle2);
                    }
                });
            }else if (data.getCode().equalsIgnoreCase("3004")){//审核回复
                FreezeTipsDialog dialogFragment = new FreezeTipsDialog();
                Bundle bundle = new Bundle();
                bundle.putString("showTips", "审核回复："+data.getReason());
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "SelectTimeDialog");
                dialogFragment.setOnConfirmListener(new FreezeTipsDialog.OnFreezeTipsListener() {
                    @Override
                    public void gotoAppeal(int type) {
                        Bundle bundle2 =new Bundle();
                        bundle2.putString("SUB_TYPE","4");
                        gotoActivity(AppealAct.class,false,bundle2);
                    }
                });
            }
        } else {
            gotoActivity(WithdrawAct.class);
        }
    }
}