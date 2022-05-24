package com.pro.maluli.module.other.verificationCode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.myselfView.VerifyEditText;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.other.findPassword.FindPasswordAct;
import com.pro.maluli.module.other.verificationCode.presenter.IVerificationCodeContraction;
import com.pro.maluli.module.other.verificationCode.presenter.VerificationCodePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class VerificationCodeAct extends BaseMvpActivity<IVerificationCodeContraction.View, VerificationCodePresenter>
        implements IVerificationCodeContraction.View {
    private String mobile;
    private String codeType;

    @BindView(R.id.countdownTV)
    TextView countdownTV;
    @BindView(R.id.reGetCodeTv)
    TextView reGetCodeTv;
    @BindView(R.id.verifyEditText)
    VerifyEditText verifyEditText;
    private String openid,getCodeType;


    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 手机号没有设置密码
     *
     * @param mobile 手机号码
     * @param code   手机验证码
     */
    @Override
    public void setGoSetPassWord(String mobile, String code) {
        Bundle bundle = new Bundle();
        bundle.putString(ACEConstant.MOBILE, mobile);
        bundle.putString(ACEConstant.VERIFICATION_CODE, code);
        gotoActivity(FindPasswordAct.class, false, bundle);
    }

    @Override
    public void getCodeSuccess() {
        reGetCodeTv.setVisibility(View.GONE);
        countdownTV.setVisibility(View.VISIBLE);
        mTimer.start();
    }


    @Override
    public VerificationCodePresenter initPresenter() {
        return new VerificationCodePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        mobile = getIntent().getStringExtra(ACEConstant.MOBILE);
        codeType = getIntent().getStringExtra(ACEConstant.GET_CODE_TYPE);
        openid = getIntent().getStringExtra("OPENID");
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_verification_code;
    }

    @Override
    public void viewInitialization() {
        if ("3".equalsIgnoreCase(codeType) || "4".equalsIgnoreCase(codeType)) {
            setTitleTx("绑定手机");
            getCodeType="1";
        } else {
            setTitleTx("登录");
            getCodeType="1";
        }

        setBackPress();
        verifyEditText = (VerifyEditText) findViewById(R.id.verifyEditText);
        verifyEditText.setOnVerifyInputCompleteListener(inputCode -> {
            verifyEditText.clearEd();
            //验证码登录
            if ("2".equalsIgnoreCase(codeType)) {
                getCodeType="1";
                presenter.codeLogin(mobile, "2", inputCode);
                return;
            }
            //绑定手机号
            if ("3".equalsIgnoreCase(codeType) || "4".equalsIgnoreCase(codeType)) {
                String type = codeType.equalsIgnoreCase("3") ? "1" : "2";
                presenter.bindMobile(mobile, openid, type, inputCode);
                getCodeType="1";
                return;
            }
            if ("注册".equalsIgnoreCase(codeType)){
                presenter.register(mobile,inputCode);
                getCodeType="1";
                return;
            }
            presenter.checkCode(mobile, codeType, inputCode);
//            verifyEditText.clearEd();
        });
//        verifyEditText.clearEd();
    }

    CountDownTimer mTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            countdownTV.setText(String.valueOf(l / 1000) + "S后重新获取");
            reGetCodeTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            countdownTV.setText("获取验证码");
            reGetCodeTv.setEnabled(true);
            countdownTV.setVisibility(View.GONE);
            reGetCodeTv.setVisibility(View.VISIBLE);
        }
    };

    @OnClick({R.id.reGetCodeTv})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.reGetCodeTv:
                presenter.getVerifiCationCode(mobile);
                break;

        }

    }

    @Override
    public void doBusiness() {
        countdownTV.setVisibility(View.VISIBLE);
        mTimer.start();
    }
}