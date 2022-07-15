package com.pro.maluli.module.message.systemNotification.detail;

import android.graphics.Color;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.SystemDetailEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.message.systemNotification.detail.presenter.ISystemDetailContraction;
import com.pro.maluli.module.message.systemNotification.detail.presenter.SystemDetailPresenter;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class SystemDetailAct extends BaseMvpActivity<ISystemDetailContraction.View, SystemDetailPresenter>
        implements ISystemDetailContraction.View {


    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.contentWb)
    WebView contentWb;

    @Override
    public SystemDetailPresenter initPresenter() {
        return new SystemDetailPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        presenter.id = getIntent().getStringExtra("SYSTEM_ID");

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_syetem_detail;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("通知详情");
        setBackPress();

    }

    @Override
    public void doBusiness() {
        presenter.getSystemDetail();
    }


    @Override
    public void setDetailSystem(SystemDetailEntity data) {
        titleTv.setText(data.getInfo().getTitle());
        timeTv.setText(data.getInfo().getCreated_at());
        contentWb.loadDataWithBaseURL(null, data.getInfo().getContent(), "text/html", "utf-8", null);
    }

}