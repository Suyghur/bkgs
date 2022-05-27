package com.pro.maluli.module.other.findPassword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.myself.personalCenter.PersonalCenterAct;
import com.pro.maluli.module.other.findPassword.presenter.FindPasswordPresenter;
import com.pro.maluli.module.other.findPassword.presenter.IFindPasswordContraction;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class FindPasswordAct extends BaseMvpActivity<IFindPasswordContraction.View,
        FindPasswordPresenter> implements IFindPasswordContraction.View {

    @BindView(R.id.leftImg_ly)
    RelativeLayout leftImgLy;
    @BindView(R.id.inputPwdEt)
    EditText inputPwdEt;
    @BindView(R.id.inputRePwdEt)
    EditText inputRePwdEt;
    @BindView(R.id.submitPwdTv)
    TextView submitPwdTv;
    @BindView(R.id.checkPwdImg)
    ImageView checkPwdImg;
    @BindView(R.id.reCheckPwdImg)
    ImageView reCheckPwdImg;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(inputRePwdEt.getText().toString().trim())
                    && !TextUtils.isEmpty(inputPwdEt.getText().toString().trim())) {
                submitPwdTv.setSelected(true);
            } else {
                submitPwdTv.setSelected(false);

            }

        }
    };
    private String mobile;
    private String code;

    @Override
    public void setCommission(String commission) {

    }

    /**
     * 设置密码成功
     *
     * @param msg      服务器返回提示
     * @param is_first
     */
    @Override
    public void setPwdSuccess(String msg, int is_first) {
        ToastUtils.showShort(msg);
        if (is_first == 1) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFrist", true);
            gotoActivity(PersonalCenterAct.class, false, bundle);
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public FindPasswordPresenter initPresenter() {
        return new FindPasswordPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

        mobile = getIntent().getStringExtra(ACEConstant.MOBILE);
        code = getIntent().getStringExtra(ACEConstant.VERIFICATION_CODE);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_find_password;
    }

    @Override
    public void viewInitialization() {
        inputRePwdEt.addTextChangedListener(textWatcher);
        inputPwdEt.addTextChangedListener(textWatcher);

    }

    @OnClick({R.id.submitPwdTv, R.id.leftImg_ly, R.id.checkPwdImg, R.id.reCheckPwdImg})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.submitPwdTv:
                KeyboardUtils.hideSoftInput(FindPasswordAct.this);
                String pwd = inputPwdEt.getText().toString().trim();
                String repwd = inputRePwdEt.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("请输入正确的密码");

                    return;
                }
                if (!pwd.equals(repwd)) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("密码不一致，请重新输入");

                    return;
                }
                if (!StringUtils.validatePhonePass(pwd)) {
                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show("密码必须是8-16位数字和字母混合");

                    return;
                }
                presenter.subMitPwd(mobile, code, pwd);
                break;
            case R.id.leftImg_ly:
                finish();
                break;
            case R.id.checkPwdImg:
                if (checkPwdImg.isSelected()) {
                    checkPwdImg.setSelected(false);
                    inputPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    inputPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkPwdImg.setSelected(true);
                }
                try {
                    if (inputPwdEt.getText().toString().trim().length() > 0) {
                        inputPwdEt.setSelection(inputPwdEt.length());
                    }
                } catch (Exception e) {

                }

                break;
            case R.id.reCheckPwdImg:
                if (reCheckPwdImg.isSelected()) {
                    reCheckPwdImg.setSelected(false);
                    inputRePwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    inputRePwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    reCheckPwdImg.setSelected(true);
                }
                //防止部分手机闪退
                try {
                    if (inputRePwdEt.getText().toString().trim().length() > 0) {
                        inputRePwdEt.setSelection(inputPwdEt.length());
                    }
                } catch (Exception e) {

                }
                break;
        }

    }

    @Override
    public void doBusiness() {

    }
}