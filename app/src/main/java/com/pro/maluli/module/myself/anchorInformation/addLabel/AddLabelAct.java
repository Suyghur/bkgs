package com.pro.maluli.module.myself.anchorInformation.addLabel;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.anchorInformation.addLabel.presenter.AddLabelPresenter;
import com.pro.maluli.module.myself.anchorInformation.addLabel.presenter.IAddLabelContraction;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AddLabelAct extends BaseMvpActivity<IAddLabelContraction.View, AddLabelPresenter> implements IAddLabelContraction.View {


    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.inputLabelEt)
    EditText inputLabelEt;

    @Override
    public AddLabelPresenter initPresenter() {
        return new AddLabelPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_add_label;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("主播标签");
        setBackPress();
        rightTv.setText("保存");
        rightTv.setTextColor(getResources().getColor(R.color.c_8e1d77));
    }

    @OnClick({R.id.right_tv})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.right_tv:
                if (TextUtils.isEmpty(inputLabelEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入正确的标签");
                    return;
                }
                presenter.addLabel(inputLabelEt.getText().toString().trim());
                break;
        }
    }

    @Override
    public void doBusiness() {
    }


    @OnClick(R.id.right_tv)
    public void onClick() {
    }

    @Override
    public void addLabelSuccess() {
        ToastUtils.showShort("操作成功！");
        finish();
    }
}