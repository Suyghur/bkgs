package com.pro.maluli.module.myself.myAccount.withdraw.base;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.MyAccountEntity;
import com.pro.maluli.common.utils.CashierInputFilter;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.myself.myAccount.withdraw.base.presenter.IWithdrawContraction;
import com.pro.maluli.module.myself.myAccount.withdraw.base.presenter.WithdrawPresenter;
import com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.WithDrawRecordAct;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class WithdrawAct extends BaseMvpActivity<IWithdrawContraction.View, WithdrawPresenter> implements IWithdrawContraction.View {


    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.avatarWithdrawCiv)
    CircleImageView avatarWithdrawCiv;
    @BindView(R.id.wdUsrrNamtTv)
    TextView wdUsrrNamtTv;
    @BindView(R.id.canWithdrawTv)
    TextView canWithdrawTv;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.allWithDrawTv)
    TextView allWithDrawTv;
    @BindView(R.id.mobileEt)
    EditText mobileEt;
    @BindView(R.id.inputWithdrawTv)
    EditText inputWithdrawTv;
    @BindView(R.id.getCodeTv)
    TextView getCodeTv;
    @BindView(R.id.alipayTv)
    TextView alipayTv;
    @BindView(R.id.subTiXianTv)
    TextView subTiXianTv;
    String numberBk;

    @Override
    public WithdrawPresenter initPresenter() {
        return new WithdrawPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_withdraw;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("提现");
        rightTv.setText("提现记录");
        rightTv.setTextColor(getResources().getColor(R.color.c_8e1d77));
        setBackPress();
        InputFilter[] filters = {new CashierInputFilter(1)};
        inputWithdrawTv.setFilters(filters);
    }


    @Override
    public void doBusiness() {
        presenter.getMyAccount();
    }

    @Override
    public void setAccountInfo(MyAccountEntity data) {
        GlideUtils.loadImage(WithdrawAct.this, data.getAvatar(), avatarWithdrawCiv);
        wdUsrrNamtTv.setText(data.getNickname());
        canWithdrawTv.setText(data.getMoney() + "");
        alipayTv.setText(data.getAlipay_account());
        numberBk = data.getMoney();
    }

    @Override
    public void getCodeSuccess() {
        mTimer.start();
    }

    CountDownTimer mTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            getCodeTv.setText(String.valueOf(l / 1000) + "S后重新获取");
            getCodeTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            getCodeTv.setText("获取验证码");
            getCodeTv.setEnabled(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.allWithDrawTv, R.id.getCodeTv, R.id.subTiXianTv, R.id.right_tv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.allWithDrawTv://全部提现
                inputWithdrawTv.setText(numberBk + "");
                inputWithdrawTv.setSelection(inputWithdrawTv.getText().toString().trim().length());
                break;
            case R.id.right_tv://提现记录
                gotoActivity(WithDrawRecordAct.class);
                break;
            case R.id.subTiXianTv://提现
                if (TextUtils.isEmpty(inputWithdrawTv.getText().toString().trim())) {
                    ToastUtils.showShort("请输入提现数额");
                    return;
                }
                if (StringUtils.compareTo(inputWithdrawTv.getText().toString().trim(), numberBk) == 1) {
                    ToastUtils.showShort("高手币不足");
                }
                if (TextUtils.isEmpty(mobileEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                presenter.code = mobileEt.getText().toString().trim();
                presenter.money = inputWithdrawTv.getText().toString().trim();
                presenter.subWithdraw();
                break;
            case R.id.getCodeTv://获取验证码
//                if (!RegexUtils.isMobileExact(mobileEt.getText().toString().trim())) {
//                    ToastUtils.showShort("请输入正确的手机号码");
//                    return;
//                }

                presenter.getVerifiCationCode("", "4");
                break;
        }
    }
}