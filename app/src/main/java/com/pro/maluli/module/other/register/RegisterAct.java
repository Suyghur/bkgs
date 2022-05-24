package com.pro.maluli.module.other.register;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
import com.pro.maluli.module.other.register.presenter.RegisterPresenter;
import com.pro.maluli.module.other.register.presenter.IRegisterContraction;
import com.pro.maluli.module.other.verificationCode.VerificationCodeAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class RegisterAct extends BaseMvpActivity<IRegisterContraction.View, RegisterPresenter> implements IRegisterContraction.View {
    @BindView(R.id.inputMobileEt)
    EditText inputMobileEt;
    @BindView(R.id.submitMobileTv)
    TextView submitMobileTv;
    @BindView(R.id.registerXyTv)
    TextView registerXyTv;
    @BindView(R.id.successImg)
    ImageView successImg;

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_register;
    }

    @Override
    public void viewInitialization() {
        setBackPress();
        inputMobileEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(inputMobileEt.getText().toString().trim())) {
                    submitMobileTv.setSelected(true);
                } else {
                    submitMobileTv.setSelected(false);
                }

            }
        });

        String str = "已阅读并同意《用户注册协议》";

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《")+1;//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (AntiShake.check(widget.getId())) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("PROTOCOL_ID", "2");
                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.c_8e1d77));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);

        registerXyTv.setMovementMethod(LinkMovementMethod.getInstance());
        registerXyTv.setText(ssb, TextView.BufferType.SPANNABLE);
    }


    @Override
    public void doBusiness() {

    }

    @OnClick({R.id.submitMobileTv, R.id.leftImg_ly, R.id.successImg})
    public void onClick(View v) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (v.getId()) {
            case R.id.submitMobileTv:
                KeyboardUtils.hideSoftInput(RegisterAct.this);
                if (!RegexUtils.isMobileExact(inputMobileEt.getText().toString().trim())) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请输入正确的手机号码");

                    return;
                }
                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请勾选并阅读协议");

                    return;
                }
                presenter.getVerifiCationCode(inputMobileEt.getText().toString().trim());
                break;
            case R.id.leftImg_ly:
                finish();
                break;
            case R.id.successImg:
                successImg.setSelected(!successImg.isSelected());
                break;
        }

    }

    /**
     * 获取验证码成功
     * codetype=1 找回密码
     */
    @Override
    public void getCodeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString(ACEConstant.GET_CODE_TYPE, "注册");
        bundle.putString(ACEConstant.MOBILE, inputMobileEt.getText().toString().trim());
        gotoActivity(VerificationCodeAct.class, true, bundle);
    }
}