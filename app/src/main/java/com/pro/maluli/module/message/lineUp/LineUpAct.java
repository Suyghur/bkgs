package com.pro.maluli.module.message.lineUp;

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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.LineUpEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.message.lineUp.adapter.LineUpAdapter;
import com.pro.maluli.module.message.lineUp.presenter.ILineUpContraction;
import com.pro.maluli.module.message.lineUp.presenter.LineUpPresenter;
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
public class LineUpAct extends BaseMvpActivity<ILineUpContraction.View, LineUpPresenter> implements ILineUpContraction.View {

    LineUpAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.watchSfl)
    SmartRefreshLayout watchSfl;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    private List<LineUpEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public LineUpPresenter initPresenter() {
        return new LineUpPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_line_up;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("预约排队");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        watchSfl.setRefreshHeader(new ClassicsHeader(this));
        watchSfl.setRefreshFooter(new ClassicsFooter(this));
        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new LineUpAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);
        blackListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                presenter.readLineUpmsg(listBeans.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putString("ANCHOR_ID", String.valueOf(listBeans.get(position).getAnchor_id()));
                gotoActivity(OneToOneQueueAct.class, false, bundle);
            }
        });

        /**
         * 加载更多
         */
        watchSfl.setEnableLoadMore(false);
        watchSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                watchSfl.finishLoadMore(1000);
                presenter.getReserveMsg();
            }
        });
        /**
         * 下拉刷新
         */
        watchSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                presenter.getReserveMsg();
                watchSfl.finishRefresh(1000);
            }
        });
    }


    @Override
    public void doBusiness() {
        presenter.getReserveMsg();
    }


    @Override
    public void setLineUplist(LineUpEntity data) {
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

    @Override
    public void readSuccess() {

    }
}