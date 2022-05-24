package com.pro.maluli.module.myself.fans;

import android.graphics.Color;
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
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.fans.adapter.MyFansAdapter;
import com.pro.maluli.module.myself.fans.presenter.IMyFansContraction;
import com.pro.maluli.module.myself.fans.presenter.MyFansPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class MyFansAct extends BaseMvpActivity<IMyFansContraction.View, MyFansPresenter> implements IMyFansContraction.View {

    MyFansAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.watchSfl)
    SmartRefreshLayout watchSfl;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    private List<WatchListEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public MyFansPresenter initPresenter() {
        return new MyFansPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_my_fans_list;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("粉丝列表");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        watchSfl.setRefreshHeader(new ClassicsHeader(this));
        watchSfl.setRefreshFooter(new ClassicsFooter(this));
        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new MyFansAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);

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
    public void setWatchInfo(WatchListEntity entity) {
        watchSfl.finishRefresh();
        watchSfl.finishLoadMore();
        //第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (entity.getList().size() != 0) {
                listBeans.clear();
                listBeans.addAll(entity.getList());
                presenter.page++;
                watchListRl.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                blackListAdapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                watchListRl.setVisibility(View.GONE);
            }
        } else {//不是第一页直接添加数据更新列表
            if (entity.getList().size() == 0) {
                watchSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        watchSfl.setNoMoreData(false);
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