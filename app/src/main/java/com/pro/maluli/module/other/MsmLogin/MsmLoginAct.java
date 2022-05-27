package com.pro.maluli.module.other.MsmLogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.LoginHelpDialogFragment;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
import com.pro.maluli.module.other.MsmLogin.presenter.IMsmLoginContraction;
import com.pro.maluli.module.other.MsmLogin.presenter.MsmLoginPresenter;
import com.pro.maluli.module.other.getCode.GetCodeAct;
import com.pro.maluli.module.other.verificationCode.VerificationCodeAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class MsmLoginAct extends BaseMvpActivity<IMsmLoginContraction.View, MsmLoginPresenter> implements IMsmLoginContraction.View {
    @BindView(R.id.inputMobileEt)
    EditText inputMobileEt;
    @BindView(R.id.submitMobileTv)
    TextView submitMobileTv;
    @BindView(R.id.successImg)
    ImageView successImg;
    @BindView(R.id.xieyiTv)
    TextView xieyiTv;
    @BindView(R.id.loginQQLL)
    LinearLayout loginQQLL;
    @BindView(R.id.loginWechatLL)
    LinearLayout loginWechatLL;
    //要用Handler回到主线程操作UI，否则会报错
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ登陆
                case 1:
                    String weChatData = (String) msg.obj;
                    QQWeChatBind("3", weChatData);
                    break;
                //微信登录
                case 2:
                    String weChatData1 = (String) msg.obj;
                    QQWeChatBind("4", weChatData1);
                    break;
            }
        }
    };
    private String codeType;

    @Override
    public MsmLoginPresenter initPresenter() {
        return new MsmLoginPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        codeType = getIntent().getStringExtra(ACEConstant.GET_CODE_TYPE);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_msm_login;
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
        final int start = str.indexOf("《") + 1;//第一个出现的位置
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

        xieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
        xieyiTv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void doBusiness() {

    }

    @OnClick({R.id.submitMobileTv, R.id.actFinishImg,
            R.id.loginQQLL, R.id.loginWechatLL, R.id.helpTv, R.id.successImg, R.id.pwdLoginTv})
    public void onClick(View v) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (v.getId()) {
            case R.id.submitMobileTv:
                if (!RegexUtils.isMobileExact(inputMobileEt.getText().toString().trim())) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请输入正确的手机号码");
                    return;
                }
                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请勾选并阅读用户协议");
                    return;
                }
                presenter.getVerifiCationCode(inputMobileEt.getText().toString().trim());
                break;
            case R.id.actFinishImg:
                finish();
                break;
            case R.id.loginQQLL://qq登录

                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请勾选并阅读用户协议");
                    return;
                }
                ToolUtils.loginQQ(handler, this);
                break;
            case R.id.loginWechatLL://微信登录
                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请勾选并阅读用户协议");
                    return;
                }
                ToolUtils.loginWeChat(handler, this);
                break;
            case R.id.helpTv:
                LoginHelpDialogFragment dialogFragment = new LoginHelpDialogFragment();
                Bundle args = new Bundle();
                dialogFragment.setArguments(args);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "AllReadDialog");
                break;
            case R.id.successImg:
                if (successImg.isSelected()) {
                    successImg.setSelected(false);
                } else {
                    successImg.setSelected(true);
                }
                break;
            case R.id.pwdLoginTv:
                finish();
                break;
        }

    }

    private void QQWeChatBind(String weixin, String weChatData) {
        presenter.login("", weixin, "", weChatData);
    }

    /**
     * 获取验证码成功
     */
    @Override
    public void getCodeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString(ACEConstant.GET_CODE_TYPE, "2");
        bundle.putString(ACEConstant.MOBILE, inputMobileEt.getText().toString().trim());
        gotoActivity(VerificationCodeAct.class, true, bundle);
    }


    @Override
    public void loginSuccess(String msg) {
        ToastUtils.showShort(msg);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 绑定手机
     *
     * @param openid
     */
    @Override
    public void gotoBindMobile(String openid, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ACEConstant.GET_CODE_TYPE, type);
        bundle.putString("OPENID", openid);
        gotoActivity(GetCodeAct.class, false, bundle);
    }
}