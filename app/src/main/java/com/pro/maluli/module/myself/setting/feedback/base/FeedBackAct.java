package com.pro.maluli.module.myself.setting.feedback.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.FeedbackEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.myAccount.appeal.AppealAct;
import com.pro.maluli.module.myself.setting.feedback.base.adapter.FeedBackAdapter;
import com.pro.maluli.module.myself.setting.feedback.base.presenter.FeedBackPresenter;
import com.pro.maluli.module.myself.setting.feedback.base.presenter.IFeedBackContraction;
import com.pro.maluli.module.myself.setting.feedback.feedBackDetail.FeedBackDetailAct;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class FeedBackAct extends BaseMvpActivity<IFeedBackContraction.View, FeedBackPresenter> implements IFeedBackContraction.View {

    @BindView(R.id.feedBackXrl)
    RecyclerView feedBackXrl;
    @BindView(R.id.fbSfl)
    SmartRefreshLayout fbSfl;
    @BindView(R.id.right_tv)
    TextView right_tv;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.nodataView)
    View nodataView;
    FeedBackAdapter blackListAdapter;
    private List<FeedbackEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public FeedBackPresenter initPresenter() {
        return new FeedBackPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_feed_back;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("意见反馈");
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText("去反馈");
        right_tv.setTextColor(getResources().getColor(R.color.c_8e1d77));
        nodataTipsTv.setText("暂无数据");
        setBackPress();
        feedBackXrl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new FeedBackAdapter(listBeans);
        feedBackXrl.setAdapter(blackListAdapter);

        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        blackListAdapter.addChildClickViewIds(R.id.itemfeedbackLl);
        // 设置子控件点击监听
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("FeedBackID", String.valueOf(listBeans.get(position).getId()));
                gotoActivity(FeedBackDetailAct.class, false, bundle);
            }
        });
        fbSfl.setRefreshHeader(new ClassicsHeader(this));
        fbSfl.setRefreshFooter(new ClassicsFooter(this));
        fbSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                fbSfl.finishLoadMore(1000);
                presenter.getFeedBackList();
            }
        });

        fbSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                presenter.getFeedBackList();
                fbSfl.finishRefresh(1000);
            }
        });
    }

    @OnClick({R.id.right_tv})
    public void OnclickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.right_tv:
                Bundle bundle2 = new Bundle();
                bundle2.putString("SUB_TYPE", "3");
                gotoActivity(AppealAct.class, false, bundle2);
                break;
        }
    }

    @Override
    public void doBusiness() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.page = 1;
        presenter.getFeedBackList();
    }

    @Override
    public void setFeedBackList(FeedbackEntity entity) {
        fbSfl.finishRefresh();
        fbSfl.finishLoadMore();
        //第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (entity.getList().size() != 0) {
                listBeans.clear();
                listBeans.addAll(entity.getList());
                presenter.page++;
                feedBackXrl.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                blackListAdapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                feedBackXrl.setVisibility(View.GONE);
            }
        } else {//不是第一页直接添加数据更新列表
            if (entity.getList().size() == 0) {
                fbSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fbSfl.setNoMoreData(false);
                    }
                }, 1000);
            } else {
                presenter.page++;
                listBeans.addAll(entity.getList());
                blackListAdapter.notifyDataSetChanged();
            }

        }
    }
}