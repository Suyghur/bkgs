package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.common.ToastHelper;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter.IAnchorIntroContraction;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.presenter.AnchorIntroPresenter;
import com.pro.maluli.module.myself.setting.youthMode.YouthPassword.YouthPasswordAct;

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
    int maxDesc =200;

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
        editIntroEt.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    subMitTv.setSelected(false);
                } else {
                    subMitTv.setSelected(true);
                }
                //TextView显示剩余字数
                inputMaxTv.setText(s.length() + "/" + maxDesc);
                selectionStart = editIntroEt.getSelectionStart();
                selectionEnd = editIntroEt.getSelectionEnd();
                if (wordNum.length() > maxDesc) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    editIntroEt.setText(s);
                    editIntroEt.setSelection(tempSelection);//设置光标在最后
                }

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
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.subMitTv:
                if (TextUtils.isEmpty(editIntroEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入主播简介");
                    return;
                }
                presenter.changeAnchorDesc(editIntroEt.getText().toString().trim());
                break;
        }

    }

}