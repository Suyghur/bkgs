package com.pro.maluli.module.myself.setting.youthMode.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.YouthEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.myself.setting.blacklist.presenter.BlackListPresenter;
import com.pro.maluli.module.myself.setting.blacklist.presenter.IBlackListContraction;
import com.pro.maluli.module.myself.setting.youthMode.YouthPassword.YouthPasswordAct;
import com.pro.maluli.module.myself.setting.youthMode.base.presenter.IYouthModeContraction;
import com.pro.maluli.module.myself.setting.youthMode.base.presenter.YouthModePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class YouthModeAct extends BaseMvpActivity<IYouthModeContraction.View, YouthModePresenter> implements IYouthModeContraction.View {


    @BindView(R.id.youthWv)
    WebView youthWv;
    @BindView(R.id.startYouthTv)
    TextView startYouthTv;

    YouthEntity entity;

    @Override
    public YouthModePresenter initPresenter() {
        return new YouthModePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_youth_mode;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("青少年模式");
        setBackPress();
    }


    @Override
    public void doBusiness() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getYouthModelInfo();
    }

    @OnClick({R.id.startYouthTv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.startYouthTv:
                Bundle bundle = new Bundle();
                bundle.putSerializable("YouthPasswordAct_KEY", entity);
                gotoActivity(YouthPasswordAct.class, false, bundle);
                break;
        }

    }

    @Override
    public void setYouthSuccess(YouthEntity data) {
        entity = data;
        youthWv.loadDataWithBaseURL(null, data.getTop().getInfo(), "text/html", "utf-8", null);
        if (data.getMember().getIs_teenager() == 0) {
            startYouthTv.setSelected(false);
            startYouthTv.setText("开启模式");
        } else {
            startYouthTv.setSelected(true);
            startYouthTv.setText("关闭模式");
        }
    }
}