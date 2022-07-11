package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter.AnchorIntroPresenter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter.IAnchorIntroContraction;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AnchorIntroAct extends BaseMvpActivity<IAnchorIntroContraction.View, AnchorIntroPresenter> implements IAnchorIntroContraction.View {

    @BindView(R.id.editIntroEt)
    EditText editIntroEt;
    @BindView(R.id.inputMaxTv)
    TextView inputMaxTv;
    @BindView(R.id.subMitTv)
    TextView subMitTv;
    String desc;
    int maxDesc = 200;

    @Override
    public AnchorIntroPresenter initPresenter() {
        return new AnchorIntroPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        desc = getIntent().getStringExtra("JIAN_JIE");
        maxDesc = Integer.parseInt(getIntent().getStringExtra("max_desc"));
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_anchor_intro;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("主播简介");
        setBackPress();
        if (!TextUtils.isEmpty(desc)) {
            editIntroEt.setText(desc);
            inputMaxTv.setText(desc.length() + "/" + maxDesc);
        }
        editIntroEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxDesc)});
        editIntroEt.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                subMitTv.setSelected(!TextUtils.isEmpty(s.toString()));
                // TextView显示剩余字数
                inputMaxTv.setText(s.length() + "/" + maxDesc);
            }
        });
    }


    @Override
    public void doBusiness() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.subMitTv})
    public void onClick(View view) {
        if (view.getId() == R.id.subMitTv) {
            if (TextUtils.isEmpty(editIntroEt.getText().toString().trim())) {
                ToastUtils.showShort("请输入主播简介");
                return;
            }
            presenter.changeAnchorDesc(editIntroEt.getText().toString().trim());
        }
    }
}