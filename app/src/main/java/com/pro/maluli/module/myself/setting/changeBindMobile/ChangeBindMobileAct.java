package com.pro.maluli.module.myself.setting.changeBindMobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.myself.setting.changeBindMobile.presenter.ChangeBindMobilePresenter;
import com.pro.maluli.module.myself.setting.changeBindMobile.presenter.IChangeBindMobileContraction;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class ChangeBindMobileAct extends BaseMvpActivity<IChangeBindMobileContraction.View,
        ChangeBindMobilePresenter> implements IChangeBindMobileContraction.View {

    @BindView(R.id.leftImg_ly)
    RelativeLayout leftImgLy;
    @BindView(R.id.inputPwdEt)
    EditText inputPwdEt;
    @BindView(R.id.inputCodeEt)
    EditText inputCodeEt;
    @BindView(R.id.submitPwdTv)
    TextView submitPwdTv;
    @BindView(R.id.getCodeTv)
    TextView getCodeTv;
    private String type = "1";
    private String mobile;


    @Override
    public void getCodeSuccess() {
        mTimer.start();
    }

    /**
     * 设置密码成功
     *
     * @param msg 服务器返回提示
     */
    @Override
    public void setPwdSuccess(String msg) {
        inputPwdEt.setText("");
        inputPwdEt.setEnabled(true);
        inputCodeEt.setText("");
        submitPwdTv.setSelected(false);
        inputPwdEt.setHint("输入新手机号");
        type = "2";
        getCodeTv.setText("获取验证码");
        getCodeTv.setEnabled(true);
        mTimer.cancel();
    }

    @Override
    public ChangeBindMobilePresenter initPresenter() {
        return new ChangeBindMobilePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        mobile = getIntent().getStringExtra("Mobile");
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_change_bind_mobile;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("更换绑定");
        inputCodeEt.addTextChangedListener(textWatcher);
        inputPwdEt.addTextChangedListener(textWatcher);
        if (!TextUtils.isEmpty(mobile)) {
            inputPwdEt.setText(mobile);
            inputPwdEt.setEnabled(false);
        }

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
            if (!TextUtils.isEmpty(inputCodeEt.getText().toString().trim())
                    && !TextUtils.isEmpty(inputPwdEt.getText().toString().trim())) {
                submitPwdTv.setSelected(true);
            } else {
                submitPwdTv.setSelected(false);

            }

        }
    };

    @OnClick({R.id.submitPwdTv, R.id.leftImg_ly, R.id.getCodeTv})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.submitPwdTv:
                String mobile = inputPwdEt.getText().toString().trim();
                String code = inputCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入正确的密码");
                    return;
                }
                presenter.changeBindmobile(mobile, code, type);
                break;
            case R.id.leftImg_ly:
                finish();
                break;
            case R.id.getCodeTv:
                if (!RegexUtils.isMobileExact(inputPwdEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入正确的手机号码");
                    return;
                }
                presenter.getVerifiCationCode(inputPwdEt.getText().toString().trim(), "3");

                break;
        }

    }

    CountDownTimer mTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            getCodeTv.setText(String.valueOf(l / 1000) + "S后重新获取");
            getCodeTv.setEnabled(false);
            getCodeTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            getCodeTv.setText("获取验证码");
            getCodeTv.setEnabled(true);
        }
    };

    @Override
    public void doBusiness() {

    }
}