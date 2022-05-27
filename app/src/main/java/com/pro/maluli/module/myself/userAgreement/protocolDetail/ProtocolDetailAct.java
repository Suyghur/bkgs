package com.pro.maluli.module.myself.userAgreement.protocolDetail;

import android.graphics.Color;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.ProtocolDetailEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.presenter.IProtocolDetailContraction;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.presenter.ProtocolDetailPresenter;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class ProtocolDetailAct extends BaseMvpActivity<IProtocolDetailContraction.View,
        ProtocolDetailPresenter> implements IProtocolDetailContraction.View {
    @BindView(R.id.protocolDetailLLWb)
    WebView protocolDetailLLWb;
    @BindView(R.id.title)
    TextView title;
    private String id;

    @Override
    public ProtocolDetailPresenter initPresenter() {
        return new ProtocolDetailPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        id = getIntent().getStringExtra("PROTOCOL_ID");
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_protocol_detail;
    }

    @Override
    public void viewInitialization() {
//        setTitleTx("协议详情");
        setBackPress();

    }


    @Override
    public void doBusiness() {
        presenter.getProtocoList(id);
    }

    @Override
    public void setProtocolDetail(ProtocolDetailEntity data) {
        protocolDetailLLWb.loadDataWithBaseURL(null, data.getList().getContent(), "text/html", "utf-8", null);
        title.setText(data.getList().getTitle());

    }
}