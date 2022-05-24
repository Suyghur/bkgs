package com.pro.maluli.module.home.announcement.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.home.announcement.detail.presenter.AnnouncementDetailPresenter;
import com.pro.maluli.module.home.announcement.detail.presenter.IAnnouncementDetailContraction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AnnouncementDetailAct extends BaseMvpActivity<IAnnouncementDetailContraction.View, AnnouncementDetailPresenter> implements IAnnouncementDetailContraction.View {

    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.noticeWb)
    WebView noticeWb;
    private List<NoticeEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public AnnouncementDetailPresenter initPresenter() {
        return new AnnouncementDetailPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        presenter.id = getIntent().getStringExtra("NOTICE_ID");
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_announcement_detail;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("公告详情");
        setBackPress();
    }


    @Override
    public void doBusiness() {
        presenter.getNoticDetailInfo();
    }


    @Override
    public void setDetailInfo(NoticeEntity.ListBean data) {
        noticeWb.loadDataWithBaseURL(null, data.getContent(), "text/html", "utf-8", null);
        timeTv.setText(data.getCreated_at());
        titleTv.setText(data.getTitle());
    }
}