package com.pro.maluli.module.myself.watchlist;

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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.anchorInformation.base.AnchorInformationAct;
import com.pro.maluli.module.myself.watchlist.adapter.WatchListAdapter;
import com.pro.maluli.module.myself.watchlist.presenter.IWatchListContraction;
import com.pro.maluli.module.myself.watchlist.presenter.WatchListPresenter;
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
public class WatchListAct extends BaseMvpActivity<IWatchListContraction.View, WatchListPresenter> implements IWatchListContraction.View {

    WatchListAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.watchSfl)
    SmartRefreshLayout watchSfl;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    private int deletePosition;
    private List<WatchListEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public WatchListPresenter initPresenter() {
        return new WatchListPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_watch_list;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("????????????");
        setBackPress();
        watchSfl.setRefreshHeader(new ClassicsHeader(this));
        watchSfl.setRefreshFooter(new ClassicsFooter(this));

        nodataTipsTv.setText("????????????");
        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new WatchListAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);

//         ?????????????????????????????????id???????????????????????????convert????????????
        blackListAdapter.addChildClickViewIds(R.id.likeTv);
// ???????????????????????????
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.likeTv) {
                    deletePosition = position;
                    presenter.setRemoveLike(listBeans.get(position).getAnchor_id());
                }
            }
        });
        blackListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("AnchorID", String.valueOf(listBeans.get(position).getAnchor_id()));
                gotoActivity(AnchorInformationAct.class, false, bundle1);
            }
        });
        /**
         * ????????????
         */
        watchSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                watchSfl.finishLoadMore(1000);
                presenter.getWatchListInfo();
            }
        });
        /**
         * ????????????
         */
        watchSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                presenter.getWatchListInfo();
                watchSfl.finishRefresh(1000);
            }
        });
    }


    @Override
    public void doBusiness() {
        presenter.getWatchListInfo();
    }

    @Override
    public void setWatchInfo(WatchListEntity entity) {
        watchSfl.finishRefresh();
        watchSfl.finishLoadMore();
        //?????????????????????????????????????????????
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
        } else {//?????????????????????????????????????????????
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

    @Override
    public void setRemoveLikeSuccess(int id) {
        listBeans.remove(deletePosition);//??????????????????
        blackListAdapter.notifyItemRemoved(deletePosition);//??????????????????
        blackListAdapter.notifyItemRangeChanged(deletePosition, listBeans.size() - deletePosition);//??????????????????????????????????????????
    }
}