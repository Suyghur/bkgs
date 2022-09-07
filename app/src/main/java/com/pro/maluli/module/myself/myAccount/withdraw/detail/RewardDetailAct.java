package com.pro.maluli.module.myself.myAccount.withdraw.detail;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.RewardDetailEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.view.dialogview.SelectTimeDialog;
import com.pro.maluli.common.view.popwindow.PayTypeAllPopupWindow;
import com.pro.maluli.module.myself.myAccount.withdraw.detail.adapter.RewardDetailAdapter;
import com.pro.maluli.module.myself.myAccount.withdraw.detail.presenter.IRewardDetailContraction;
import com.pro.maluli.module.myself.myAccount.withdraw.detail.presenter.RewardDetailPresenter;
import com.pro.maluli.module.myself.myAccount.withdraw.search.searchReward.SearchRewardAct;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class RewardDetailAct extends BaseMvpActivity<IRewardDetailContraction.View, RewardDetailPresenter> implements IRewardDetailContraction.View {
    RewardDetailAdapter adapter;
    @BindView(R.id.bkDetailRv)
    RecyclerView bkDetailRv;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bkDetailSfl)
    SmartRefreshLayout bkDetailSfl;
    @BindView(R.id.topSelectLL)
    LinearLayout topSelectLL;
    @BindView(R.id.payoutTv)
    TextView payoutTv;
    @BindView(R.id.payInTv)
    TextView payInTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.seeTypeTv)
    TextView seeTypeTv;
    @BindView(R.id.iv_title)
    ImageView iv_title;
    private List<RewardDetailEntity.ListBean> entities = new ArrayList<>();

    @Override
    public RewardDetailPresenter initPresenter() {
        return new RewardDetailPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        presenter.date = TimeUtils.getNowString(format);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_reward_detail;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("打赏收支");
        setBackPress();
        iv_title.setBackground(getResources().getDrawable(R.mipmap.ic_search_hui_bg));
        bkDetailSfl.setRefreshHeader(new ClassicsHeader(this));
        bkDetailSfl.setRefreshFooter(new ClassicsFooter(this));
        bkDetailSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                bkDetailSfl.finishLoadMore(1000);
                presenter.getBkDetail();
            }
        });
        bkDetailSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                bkDetailSfl.finishRefresh(1000);
            }
        });
        bkDetailRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RewardDetailAdapter(entities, this);
        bkDetailRv.setAdapter(adapter);

//        FreezeTipsDialog dialogFragment = new FreezeTipsDialog();
//        Bundle bundle = new Bundle();
//        bundle.putInt("ShowType", 0);
//        dialogFragment.setArguments(bundle);
//        dialogFragment.setCancelable(true);
//        dialogFragment.show(getSupportFragmentManager(), "SelectTimeDialog");
//        dialogFragment.setOnConfirmListener(new FreezeTipsDialog.OnFreezeTipsListener() {
//            @Override
//            public void gotoAppeal(int type) {
//
//            }
//        });

        timeTv.setText(presenter.date);

    }

    @OnClick({R.id.selectTimeTv, R.id.allLL, R.id.rl_right, R.id.iv_title})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.selectTimeTv:
                SelectTimeDialog dialogFragment = new SelectTimeDialog();
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "SelectTimeDialog");
                dialogFragment.setOnTimeListener(new SelectTimeDialog.OnSelectTimeListener() {
                    @Override
                    public void confirmSuccess(int timeType, String startTime1, String endTiem1) {
                        if (timeType == 0) {
                            presenter.date = startTime1;
                            presenter.start_time = "";
                            presenter.end_time = "";
                            timeTv.setText(startTime1.replace("-", "."));
                        } else {
                            presenter.date = "";
                            presenter.start_time = startTime1;
                            presenter.end_time = endTiem1;
                            timeTv.setText(startTime1.replace("-", ".") + "-" + endTiem1.replace("-", "."));
                        }
                        presenter.page = 1;
                        presenter.getBkDetail();
                    }
                });
                break;
            case R.id.allLL:
                PayTypeAllPopupWindow payTypeAllPopupWindow =
                        new PayTypeAllPopupWindow(this, true, topSelectLL, new PayTypeAllPopupWindow.PayTypeAllListener() {
                            @Override
                            public void onSelectItem(int position, String type) {
                                presenter.flow_type = String.valueOf(position);
                                presenter.page = 1;
                                seeTypeTv.setText(type);
                                presenter.getBkDetail();
                            }
                        });
//                initPop(topSelectLL);
                break;
            case R.id.iv_title:
                gotoActivity(SearchRewardAct.class);
                break;
        }
    }


    @Override
    public void doBusiness() {
        presenter.page = 1;
        presenter.getBkDetail();
    }


    @Override
    public void setBkDetailSuccess(RewardDetailEntity data) {
        bkDetailSfl.finishRefresh();
        bkDetailSfl.finishLoadMore();

        payInTv.setText(data.getPay_in() + "");
        payoutTv.setText(data.getPay_out() + "");
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