package com.pro.maluli.module.other.getCode;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.other.getCode.presenter.GetCodePresenter;
import com.pro.maluli.module.other.getCode.presenter.IGetCodeContraction;
import com.pro.maluli.module.other.verificationCode.VerificationCodeAct;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class GetCodeAct extends BaseMvpActivity<IGetCodeContraction.View, GetCodePresenter> implements IGetCodeContraction.View {
    @BindView(R.id.inputMobileEt)
    EditText inputMobileEt;
    @BindView(R.id.submitMobileTv)
    TextView submitMobileTv;
    @BindView(R.id.tipsTopTv)
    TextView tipsTopTv;
    private String codeType;
    private String openId;
    private boolean isChangePwd;
    private UserInfoEntity userInfoEntity;

    @Override
    public GetCodePresenter initPresenter() {
        return new GetCodePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

        userInfoEntity = (UserInfoEntity) ACache.get(mContext).getAsObject(ACEConstant.USERINFO);
        codeType = getIntent().getStringExtra(ACEConstant.GET_CODE_TYPE);
        openId = getIntent().getStringExtra("OPENID");
        isChangePwd = getIntent().getBooleanExtra("changePwd", false);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_get_code;
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
        if ("1".equalsIgnoreCase(codeType)) {
            if (isChangePwd) {
                tipsTopTv.setText("修改密码");
                if (userInfoEntity != null) {
                    inputMobileEt.setText(userInfoEntity.getPhone());
                    inputMobileEt.setEnabled(false);
                    submitMobileTv.setSelected(true);
                }
            } else {
                tipsTopTv.setText("找回密码");
            }
        }
        if (!TextUtils.isEmpty(openId)) {
            tipsTopTv.setText("绑定手机");
        }
    }


    @Override
    public void doBusiness() {

    }

    @OnClick({R.id.submitMobileTv, R.id.leftImg_ly})
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
                String type = "";
                if (codeType.equals("1")) {
                    type = "2";
                } else {
                    type = "1";
                }
                presenter.getVerifiCationCode(inputMobileEt.getText().toString().trim(), type);
                break;
            case R.id.leftImg_ly:
                finish();
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
        if ("3".equalsIgnoreCase(codeType) || "4".equalsIgnoreCase(codeType)) {
            bundle.putString("OPENID", openId);
        }
        bundle.putString(ACEConstant.GET_CODE_TYPE, codeType);
        bundle.putString(ACEConstant.MOBILE, inputMobileEt.getText().toString().trim());
        gotoActivity(VerificationCodeAct.class, true, bundle);
    }
}