package com.pro.maluli.module.home.homeSearch;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.myselfView.LabelsView;
import com.pro.maluli.module.home.homeSearch.adapter.SearchHomeAdapter;
import com.pro.maluli.module.home.homeSearch.presenter.ISearchHomeContraction;
import com.pro.maluli.module.home.homeSearch.presenter.SearchHomePresenter;
import com.pro.maluli.module.home.previewLive.PreviewLiveAct;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class SearchHomeAct extends BaseMvpActivity<ISearchHomeContraction.View, SearchHomePresenter> implements ISearchHomeContraction.View {
    SearchHomeAdapter adapter;
    @BindView(R.id.bkDetailRv)
    RecyclerView bkDetailRv;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.bkDetailSfl)
    SmartRefreshLayout bkDetailSfl;
    @BindView(R.id.searchHistoryLL)
    LinearLayout searchHistoryLL;
    @BindView(R.id.searchEt)
    EditText searchEt;
    @BindView(R.id.labeleLV)
    LabelsView labeleLV;
    @BindView(R.id.noSearchTv)
    TextView noSearchTv;
    KeyboardUtils.OnSoftInputChangedListener onSoftInputChangedListener = new KeyboardUtils.OnSoftInputChangedListener() {
        @Override
        public void onSoftInputChanged(int height) {
            if (height == 0) {
//                isSHowHistory(false);
            } else {
                isSHowHistory(true);

            }
        }
    };
    private List<LiveListEntity.DataBean.ListBean> entities = new ArrayList<>();
    private List<SearchEntity.ListBean> lableList = new ArrayList<>();

    @Override
    public SearchHomePresenter initPresenter() {
        return new SearchHomePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_search_home;
    }

    @Override
    public void viewInitialization() {
        bkDetailSfl.setRefreshHeader(new ClassicsHeader(this));
        bkDetailSfl.setRefreshFooter(new ClassicsFooter(this));
        bkDetailSfl.setEnableLoadMore(false);
        bkDetailSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                bkDetailSfl.finishRefresh(1000);
            }
        });
        bkDetailSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                bkDetailSfl.finishLoadMore(1000);
                presenter.getBkDetail();
            }
        });

        bkDetailRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchHomeAdapter(entities, this);
        bkDetailRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                //       type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
                Bundle bundle = new Bundle();
                bundle.putString("ANCHOR_ID", String.valueOf(entities.get(position).getAnchor_id()));
                bundle.putString("ROOM_ID", String.valueOf(entities.get(position).getRoom_id()));
                gotoActivity(PreviewLiveAct.class, false, bundle);
            }
        });
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
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    KeyboardUtils.hideSoftInput(searchEt);
                    presenter.keyword = searchEt.getText().toString().trim();
                    presenter.page = 1;
                    presenter.getBkDetail();
                    isSHowHistory(false);
                    //do something
                    //doSearch();
                    return true;
                }
                return false;
            }
        });
        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    Log.i(TAG, "获取焦点");
                    isSHowHistory(true);
                } else {
//                    Log.i(TAG, "失去焦点");
                }
            }
        });


        labeleLV.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                KeyboardUtils.hideSoftInput(searchEt);
                presenter.keyword = lableList.get(position).getText();
                presenter.page = 1;
                presenter.getBkDetail();
                isSHowHistory(false);
            }
        });
    }

    private void isSHowHistory(boolean isShow) {
        if (isShow) {
            searchHistoryLL.setVisibility(View.VISIBLE);
            bkDetailSfl.setVisibility(View.GONE);
        } else {
            searchHistoryLL.setVisibility(View.GONE);
            bkDetailSfl.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.finishTv, R.id.deleteTv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//
//            return;
//        }
        switch (view.getId()) {
            case R.id.finishTv:
                finish();
                break;
            case R.id.deleteTv:
//                presenter.
                BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
                Bundle bundle1 = new Bundle();
                bundle1.putString("showContent", "是否删除搜索历史数据？");
                baseTipsDialog.setArguments(bundle1);
                baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.deleteHistory();
                    }
                });
                break;
        }
    }

    @Override
    public void doBusiness() {
//        presenter.page = 1;
//        presenter.getBkDetail();
        presenter.getSearchHistory();

        KeyboardUtils.registerSoftInputChangedListener(getWindow(), onSoftInputChangedListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
    }

    @Override
    public void setBkDetailSuccess(LiveListEntity data) {
        bkDetailSfl.finishRefresh();
        bkDetailSfl.finishLoadMore();

        //第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (data.getData().getList().size() != 0) {
                entities.clear();
                entities.addAll(data.getData().getList());
                presenter.page++;
                bkDetailRv.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                bkDetailRv.setVisibility(View.GONE);
            }
        } else {//不是第一页直接添加数据更新列表
            if (data.getData().getList().size() == 0) {
                bkDetailSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bkDetailSfl.setNoMoreData(false);
                    }
                }, 1000);
            } else {
                presenter.page++;
                entities.addAll(data.getData().getList());
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void setHistorySearch(SearchEntity data) {
        lableList.clear();
        if (data.getList().size() == 0) {
            noSearchTv.setVisibility(View.VISIBLE);
            labeleLV.setVisibility(View.GONE);
            return;
        }
        noSearchTv.setVisibility(View.GONE);
        labeleLV.setVisibility(View.VISIBLE);
        lableList.addAll(data.getList());
        labeleLV.setSelectType(LabelsView.SelectType.NONE);
        //第一步：设置数据源
        labeleLV.setLabels(lableList, new LabelsView.LabelTextProvider<SearchEntity.ListBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, SearchEntity.ListBean data) {
                return data.getText();////根据data和position返回label需要显示的数据。
            }
        });

    }

    @Override
    public void deleteSuccess() {
        lableList.clear();
        noSearchTv.setVisibility(View.VISIBLE);
        labeleLV.setVisibility(View.GONE);
    }
}