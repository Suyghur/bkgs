package com.pro.maluli.module.home.announcement;

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
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.home.announcement.adapter.AnnouncementAdapter;
import com.pro.maluli.module.home.announcement.detail.AnnouncementDetailAct;
import com.pro.maluli.module.home.announcement.presenter.AnnouncementPresenter;
import com.pro.maluli.module.home.announcement.presenter.IAnnouncementContraction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AnnouncementAct extends BaseMvpActivity<IAnnouncementContraction.View, AnnouncementPresenter> implements IAnnouncementContraction.View {

    AnnouncementAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.watchSfl)
    SmartRefreshLayout watchSfl;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    private List<NoticeEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public AnnouncementPresenter initPresenter() {
        return new AnnouncementPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_announcement;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("公告列表");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        watchSfl.setRefreshHeader(new ClassicsHeader(this));
        watchSfl.setRefreshFooter(new ClassicsFooter(this));
        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new AnnouncementAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);
        blackListAdapter.addChildClickViewIds(R.id.itemRl);
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("NOTICE_ID", String.valueOf(listBeans.get(position).getId()));
                gotoActivity(AnnouncementDetailAct.class, false, bundle);
            }
        });


        /**
         * 加载更多
         */
        watchSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                watchSfl.finishLoadMore(1000);
            }
        });
        /**
         * 下拉刷新
         */
        watchSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                watchSfl.finishRefresh(1000);
            }
        });
    }


    @Override
    public void doBusiness() {
        presenter.getFansListInfo();
    }


    @Override
    public void setNoticeInfo(NoticeEntity data) {
        watchSfl.finishRefresh();
        watchSfl.finishLoadMore();
        //第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (data.getList().size() != 0) {
                listBeans.clear();
                listBeans.addAll(data.getList());
                presenter.page++;
                watchListRl.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                blackListAdapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                watchListRl.setVisibility(View.GONE);
            }
        } else {//不是第一页直接添加数据更新列表
            if (data.getList().size() == 0) {
                watchSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        watchSfl.setNoMoreData(false);
                    }
                }, 1000);
            } else {
                presenter.page++;
                listBeans.addAll(data.getList());
                blackListAdapter.notifyDataSetChanged();
            }

        }
    }
}