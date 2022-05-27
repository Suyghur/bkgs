package com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.WithDrawRecordEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.adapter.WithDrawRecordAdapter;
import com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.presenter.IWithDrawRecordContraction;
import com.pro.maluli.module.myself.myAccount.withdraw.withDrawRecord.presenter.WithDrawRecordPresenter;
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
public class WithDrawRecordAct extends BaseMvpActivity<IWithDrawRecordContraction.View, WithDrawRecordPresenter> implements IWithDrawRecordContraction.View {
    WithDrawRecordAdapter adapter;
    @BindView(R.id.bkDetailRv)
    RecyclerView bkDetailRv;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.bkDetailSfl)
    SmartRefreshLayout bkDetailSfl;
    private List<WithDrawRecordEntity.ListBean> entities = new ArrayList<>();


    @Override
    public WithDrawRecordPresenter initPresenter() {
        return new WithDrawRecordPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_withdraw_record;
    }

    @Override
    public void viewInitialization() {
        setBackPress();
        setTitleTx("提现记录");
        bkDetailSfl.setRefreshHeader(new ClassicsHeader(this));
        bkDetailSfl.setRefreshFooter(new ClassicsFooter(this));
        /**
         * 加载更多
         */
        bkDetailSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                bkDetailSfl.finishLoadMore(1000);
                presenter.getSearchHistory();
            }
        });
        /**
         * 下拉刷新
         */
        bkDetailSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                bkDetailSfl.finishRefresh(1000);
                presenter.getSearchHistory();
            }
        });
        bkDetailRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WithDrawRecordAdapter(entities, this);
        bkDetailRv.setAdapter(adapter);


    }


//    @OnClick({R.id.finishTv, R.id.deleteTv})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.finishTv:
//                finish();
//                break;
//        }
//    }


    @Override
    public void doBusiness() {
        presenter.page = 1;
        presenter.getSearchHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void setBkDetailSuccess(RewardDetailEntity data) {
//        bkDetailSfl.finishRefresh();
//        bkDetailSfl.finishLoadMore();
//
//        //第一页直接删除数据然后加载数据
//        if (presenter.page == 1) {
//            if (data.getList().size() != 0) {
//                entities.clear();
//                entities.addAll(data.getList());
//                presenter.page++;
//                bkDetailRv.setVisibility(View.VISIBLE);
//                nodataView.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
//            } else {
//                nodataView.setVisibility(View.VISIBLE);
//                bkDetailRv.setVisibility(View.GONE);
//            }
//        } else {//不是第一页直接添加数据更新列表
//            if (data.getList().size() == 0) {
//                bkDetailSfl.finishLoadMoreWithNoMoreData();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        bkDetailSfl.setNoMoreData(false);
//                    }
//                }, 1000);
//            } else {
//                presenter.page++;
//                entities.addAll(data.getList());
//                adapter.notifyDataSetChanged();
//            }
//
//        }
//    }


    @Override
    public void setWithDrawRecordList(WithDrawRecordEntity data) {
        bkDetailSfl.finishRefresh();
        bkDetailSfl.finishLoadMore();

        //第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (data.getList().size() != 0) {
                entities.clear();
                entities.addAll(data.getList());
                presenter.page++;
                bkDetailRv.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                bkDetailRv.setVisibility(View.GONE);
            }
        } else {//不是第一页直接添加数据更新列表
            if (data.getList().size() == 0) {
                bkDetailSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bkDetailSfl.setNoMoreData(false);
                    }
                }, 1000);
            } else {
                presenter.page++;
                entities.addAll(data.getList());
                adapter.notifyDataSetChanged();
            }

        }
    }
}