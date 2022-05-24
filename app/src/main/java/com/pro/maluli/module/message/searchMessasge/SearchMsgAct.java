package com.pro.maluli.module.message.searchMessasge;

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
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MsgSearchOption;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.myselfView.LabelsView;
import com.pro.maluli.module.home.previewLive.PreviewLiveAct;
import com.pro.maluli.module.message.searchMessasge.adapter.SearchMsgAdapter;
import com.pro.maluli.module.message.searchMessasge.presenter.ISearchMsgContraction;
import com.pro.maluli.module.message.searchMessasge.presenter.SearchMsgPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class SearchMsgAct extends BaseMvpActivity<ISearchMsgContraction.View, SearchMsgPresenter> implements ISearchMsgContraction.View {
    SearchMsgAdapter adapter;
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
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    private List<IMMessage> entities = new ArrayList<>();
    private List<SearchEntity.ListBean> lableList = new ArrayList<>();

    private List<SearchContentEntity> searchContentEntities = new ArrayList<>();

    @Override
    public SearchMsgPresenter initPresenter() {
        return new SearchMsgPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_search_msg;
    }

    @Override
    public void viewInitialization() {
        nodataTipsTv.setText("抱歉，未查找到相关结果");
        bkDetailSfl.setRefreshHeader(new ClassicsHeader(this));
        bkDetailSfl.setRefreshFooter(new ClassicsFooter(this));
        bkDetailSfl.setEnableLoadMore(false);
        /**
         * 加载更多
         */
        bkDetailSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                bkDetailSfl.finishLoadMore(1000);
//                presenter.getBkDetail();
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
                presenter.getBkDetail();
            }
        });
        bkDetailRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchMsgAdapter();
        bkDetailRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                NimUIKit.startP2PSession(SearchMsgAct.this, entities.get(position).getSessionId(), entities.get(position));
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
    public void setBkDetailSuccess(List<IMMessage> data) {
        bkDetailSfl.finishRefresh();
        bkDetailSfl.finishLoadMore();
        if (searchContentEntities != null) {
            searchContentEntities.clear();
        }
//        //第一页直接删除数据然后加载数据
//        if (presenter.page == 1) {
//        if (data.size() != 0) {
            entities.clear();
            entities.addAll(data);
            SearchContentEntity entity = new SearchContentEntity();
            entity.setTypee(1);
            entity.setMessage(entities);
            searchContentEntities.add(entity);
            presenter.page++;
//            bkDetailRv.setVisibility(View.VISIBLE);
//            nodataView.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged();
//        } else {
//            nodataView.setVisibility(View.VISIBLE);
//            bkDetailRv.setVisibility(View.GONE);
//        }
        presenter.searchUser();
//        } else {//不是第一页直接添加数据更新列表
//            if (data.getData().getList().size() == 0) {
//                bkDetailSfl.finishLoadMoreWithNoMoreData();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        bkDetailSfl.setNoMoreData(false);
//                    }
//                }, 1000);
//            } else {
//                presenter.page++;
//                entities.addAll(data.getData().getList());
//                adapter.notifyDataSetChanged();
//            }
//
//        }
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

    @Override
    public void setUserNameInfo(List<NimUserInfo> param) {
//        if (param.size() > 0) {
            SearchContentEntity entity = new SearchContentEntity();
            entity.setTypee(2);
            entity.setUserInfo(param);
            searchContentEntities.add(entity);
//        }
        if (searchContentEntities.get(0).getMessage().size()<=0&&searchContentEntities.get(1).getUserInfo().size()<=0){
            bkDetailRv.setVisibility(View.GONE);
            nodataView.setVisibility(View.VISIBLE);
        }else {
            bkDetailRv.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
            Collections.reverse(searchContentEntities);
            adapter.setList(searchContentEntities);
        }

    }
}