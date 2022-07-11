package com.pro.maluli.module.myself.setting.youthMode.YouthPassword;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.view.dialogview.ForgetPwdDialog;
import com.pro.maluli.common.view.myselfView.VerifyEditText;
import com.pro.maluli.ktx.bus.BusKey;
import com.pro.maluli.ktx.bus.LiveDataBus;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.app.BKGSApplication;
import com.pro.maluli.module.myself.setting.youthMode.YouthPassword.presenter.IYouthPasswordContraction;
import com.pro.maluli.module.myself.setting.youthMode.YouthPassword.presenter.YouthPasswordPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class YouthPasswordAct extends BaseMvpActivity<IYouthPasswordContraction.View, YouthPasswordPresenter> implements IYouthPasswordContraction.View {

    @BindView(R.id.verifyEditText)
    VerifyEditText verifyEditText;
    @BindView(R.id.topTipsTv)
    TextView topTipsTv;
    @BindView(R.id.forgetPwdTv)
    TextView forgetPwdTv;
    YouthEntity entity;

    private boolean isStartYouth;
    private String fristInput;
    private int inputNumber = 1;

    @Override
    public YouthPasswordPresenter initPresenter() {
        return new YouthPasswordPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

        entity = (YouthEntity) getIntent().getSerializableExtra("YouthPasswordAct_KEY");

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_youth_mode_pwd;
    }

    @Override
    public void viewInitialization() {
        setBackPress();
        if (entity.getMember().getIs_teenager() == 0) {
            isStartYouth = false;
            setTitleTx("开启模式");
            forgetPwdTv.setVisibility(View.GONE);
        } else {
            isStartYouth = true;
            setTitleTx("关闭模式");
            topTipsTv.setText("确认模式密码");
            forgetPwdTv.setVisibility(View.VISIBLE);
        }
        verifyEditText.setOnVerifyInputCompleteListener(input -> {
            //开启青少年模式
            if (!isStartYouth) {
                if (inputNumber == 1) {
                    fristInput = input;
                    inputNumber++;
                    topTipsTv.setText("确认模式密码");
                } else if (inputNumber == 2) {
                    if (fristInput.equalsIgnoreCase(input)) {
                        presenter.setYouthStatus(input);
                    } else {
                        inputNumber = 1;
                        topTipsTv.setText("设置模式密码");
                        ToastUtils.showShort("两次密码不一致，请重新输入");
                    }
                }
            } else {
                //关闭青少年模式
                presenter.stopYouth(input);
            }

            verifyEditText.clearEd();
        });
    }

    @OnClick({R.id.forgetPwdTv})
    public void onClick(View view) {
        if (view.getId() == R.id.forgetPwdTv) {
            ForgetPwdDialog dialogFragment = new ForgetPwdDialog();
            Bundle bundle = new Bundle();
            bundle.putString("Youth_find_password_tips", entity.getTop().getFind_password());
            dialogFragment.setArguments(bundle);
            dialogFragment.setCancelable(true);
            dialogFragment.show(getSupportFragmentManager(), "ForgetPwdDialog");
        }
    }

    @Override
    public void doBusiness() {
        presenter.getYouthModelInfo();
    }

    @Override
    public void setYouthSuccess(YouthEntity data) {
        Logger.d("setYouthSuccess");
        LiveDataBus.get().with(BusKey.EVENT_UPDATE_HOME_DATA).postValue(1);
    }

    @Override
    public void startSuccess() {
        BKGSApplication.youthModeStatus = 1;
        finish();
    }

    @Override
    public void stopSuccess() {
        BKGSApplication.youthModeStatus = 0;
        finish();
    }
}