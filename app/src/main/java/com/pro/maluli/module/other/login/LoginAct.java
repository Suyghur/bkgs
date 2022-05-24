package com.pro.maluli.module.other.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mob.MobSDK;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.LoginHelpDialogFragment;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
import com.pro.maluli.module.other.MsmLogin.MsmLoginAct;
import com.pro.maluli.module.other.getCode.GetCodeAct;
import com.pro.maluli.module.other.login.presenter.ILoginContraction;
import com.pro.maluli.module.other.login.presenter.LoginPresenter;
import com.pro.maluli.module.other.register.RegisterAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class LoginAct extends BaseMvpActivity<ILoginContraction.View, LoginPresenter> implements ILoginContraction.View {
    @BindView(R.id.findPasswordTv)
    TextView findPasswordTv;
    @BindView(R.id.actFinishImg)
    ImageView actFinishImg;
    @BindView(R.id.helpTv)
    TextView helpTv;
    @BindView(R.id.inputPhoneEt)
    EditText inputPhoneEt;
    @BindView(R.id.inputPwdEt)
    EditText inputPwdEt;
    @BindView(R.id.checkPwdImg)
    ImageView checkPwdImg;
    @BindView(R.id.loginTv)
    TextView loginTv;
    @BindView(R.id.codeLoginTv)
    TextView codeLoginTv;
    @BindView(R.id.registerTv)
    TextView registerTv;
    @BindView(R.id.xieyiTv)
    TextView xieyiTv;
    @BindView(R.id.successImg)
    ImageView successImg;


    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_login;
    }

    @Override
    public void viewInitialization() {
        inputPhoneEt.addTextChangedListener(textWatcher);
        inputPwdEt.addTextChangedListener(textWatcher);
//        MobSDK.submitPolicyGrantResult(true, null);

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

        xieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
        xieyiTv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(inputPhoneEt.getText().toString().trim())
                    && !TextUtils.isEmpty(inputPwdEt.getText().toString().trim())) {
                loginTv.setSelected(true);
            } else {
                loginTv.setSelected(false);

            }

        }
    };

    @Override
    public void doBusiness() {
        presenter.getWelcomInfo();
    }

    @OnClick({R.id.findPasswordTv, R.id.codeLoginTv, R.id.actFinishImg, R.id.helpTv,
            R.id.checkPwdImg, R.id.loginTv, R.id.registerTv, R.id.successImg,
            R.id.loginQQLL, R.id.loginWechatLL})
    public void onClick(View v) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        Bundle bundle;
        switch (v.getId()) {
            case R.id.findPasswordTv:
                bundle = new Bundle();
                bundle.putString(ACEConstant.GET_CODE_TYPE, "1");
                gotoActivity(GetCodeAct.class, false, bundle);
                break;
            case R.id.codeLoginTv:
                gotoActivity(MsmLoginAct.class, false);
                break;
            case R.id.actFinishImg:
                finish();
                break;
            case R.id.helpTv:
                LoginHelpDialogFragment dialogFragment = new LoginHelpDialogFragment();
                Bundle args = new Bundle();
                dialogFragment.setArguments(args);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "AllReadDialog");
                break;
            case R.id.checkPwdImg:
                if (checkPwdImg.isSelected()) {
                    checkPwdImg.setSelected(false);
                    inputPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    inputPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkPwdImg.setSelected(true);
                }
                inputPwdEt.setSelection(inputPwdEt.length());
                break;
            case R.id.loginTv:
                KeyboardUtils.hideSoftInput(LoginAct.this);
                if (!RegexUtils.isMobileExact(inputPhoneEt.getText().toString().trim())) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请输入正确的手机号码");
                    return;
                }
                if (!StringUtils.validatePhonePass(inputPwdEt.getText().toString().trim())) {
//                    ToastUtils.showShort("请输入正确的密码");
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请输入正确的密码");
                    return;
                }
                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请勾选并阅读用户协议");
                    return;
                }
                presenter.login(inputPhoneEt.getText().toString().trim(),
                        "1", inputPwdEt.getText().toString().trim(), "");
                break;
            case R.id.registerTv:
                gotoActivity(RegisterAct.class);
                break;
            case R.id.loginQQLL://qq登录

                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请勾选并阅读用户协议");
                    return;
                }
                ToolUtils.loginQQ(handler, this);
                break;
            case R.id.loginWechatLL://微信登录
                if (!successImg.isSelected()) {
                    ToastUtils.make().setGravity(Gravity.CENTER,0,0).show("请勾选并阅读用户协议");
                    return;
                }
                ToolUtils.loginWeChat(handler, this);
                break;
            case R.id.successImg:
                if (successImg.isSelected()) {
                    successImg.setSelected(false);
                } else {
                    successImg.setSelected(true);
                }
                break;
        }
    }

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

    private void QQWeChatBind(String weixin, String weChatData) {
        presenter.login("", weixin, "", weChatData);
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