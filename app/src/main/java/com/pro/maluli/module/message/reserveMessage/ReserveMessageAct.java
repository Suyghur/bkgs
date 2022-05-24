package com.pro.maluli.module.message.reserveMessage;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.ReserveDetailEntity;
import com.pro.maluli.common.entity.ReserveMessageEntity;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.SystemMsgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.view.dialogview.checkMsg.CheckMsgDialog;
import com.pro.maluli.common.view.popwindow.PopupWindowList;
import com.pro.maluli.module.message.reserveMessage.adapter.ReserveMessageAdapter;
import com.pro.maluli.module.message.reserveMessage.presenter.IReserveMessageContraction;
import com.pro.maluli.module.message.reserveMessage.presenter.ReserveMessagePresenter;
import com.pro.maluli.module.message.systemNotification.SystemNotificationAct;
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
public class ReserveMessageAct extends BaseMvpActivity<IReserveMessageContraction.View, ReserveMessagePresenter> implements IReserveMessageContraction.View {

    ReserveMessageAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.watchSfl)
    SmartRefreshLayout watchSfl;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.scorLLView)
    NestedScrollView scorLLView;
    @BindView(R.id.nodataView)
    View nodataView;
    private int deletePosition;
    private List<ReserveMessageEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public ReserveMessagePresenter initPresenter() {
        return new ReserveMessagePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_reserve_message;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mPopupWindowList != null) {
        SystemNotificationAct.getX = (int) ev.getX();
        SystemNotificationAct.getY = (int) ev.getY();
//        }

        return super.dispatchTouchEvent(ev);
    }


    private PopupWindowList mPopupWindowList;

    @Override
    public void viewInitialization() {
        setTitleTx("预约通知");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        watchSfl.setRefreshHeader(new ClassicsHeader(this));
        watchSfl.setRefreshFooter(new ClassicsFooter(this));


        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new ReserveMessageAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);
        blackListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                showPopWindows(view, position, listBeans.get(position).getIs_top());
                return false;
            }
        });
        blackListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                presenter.getReserveDetail(listBeans.get(position).getId());
            }
        });

        /**
         * 加载更多
         */
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

        scorLLView.setNestedScrollingEnabled(false);
    }

    private void showPopWindows(View view, int itemPosition, int type) {
        final List<String> dataList = new ArrayList<>();
        if (type == 1) {
            dataList.add("取消置顶");
        } else {
            dataList.add("置顶聊天");
        }
        dataList.add("删除聊天");
        if (mPopupWindowList == null) {
            mPopupWindowList = new PopupWindowList(view.getContext());
        }
        mPopupWindowList.setAnchorView(view);
        mPopupWindowList.setItemData(dataList);
        mPopupWindowList.setModal(true);
        mPopupWindowList.show();
        mPopupWindowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(mContext, dataList.get(position) + "", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        presenter.setReserveTop(listBeans.get(itemPosition).getId());
                        break;
                    case 1:
                        deletePosition=itemPosition;
                        presenter.setDeleteReserve(listBeans.get(itemPosition).getId());
                        break;
                }
                mPopupWindowList.hide();
            }
        });
    }

    @Override
    public void doBusiness() {
        presenter.getReserveMsg();
    }

    @Override
    public void setReserveNotic(ReserveMessageEntity data) {
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
    public void setReserveDetail(ReserveDetailEntity data) {
        SeeLiveUserEntity.AppointBean appointBean = new SeeLiveUserEntity.AppointBean();
        appointBean.setImages(data.getInfo().getImages());
        appointBean.setAccid(data.getInfo().getAccid());
        appointBean.setAvatar(data.getInfo().getAvatar());
        appointBean.setContent(data.getInfo().getContent());
        appointBean.setNickname(data.getInfo().getNickname());
        appointBean.setCreated_at(data.getInfo().getCreated_at());
        appointBean.setStatus(data.getInfo().getStatus());

        CheckMsgDialog checkMsgDialog = new CheckMsgDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Message", appointBean);
        checkMsgDialog.setArguments(bundle);
        checkMsgDialog.show(getSupportFragmentManager(), "CheckMsgDialog");
    }

    @Override
    public void deleteSuccess() {
        listBeans.remove(deletePosition);
        blackListAdapter.notifyItemRemoved(deletePosition);
//必须调用这行代码
        blackListAdapter.notifyItemRangeChanged(deletePosition, listBeans.size());
    }

//    @Override
//    public void setWatchInfo(WatchListEntity entity) {
//        watchSfl.finishRefresh();
//        watchSfl.finishLoadMore();
//        //第一页直接删除数据然后加载数据
//        if (presenter.page == 1) {
//            if (entity.getList().size() != 0) {
//                listBeans.clear();
//                listBeans.addAll(entity.getList());
//                presenter.page++;
//                watchListRl.setVisibility(View.VISIBLE);
//                nodataView.setVisibility(View.GONE);
//                blackListAdapter.notifyDataSetChanged();
//            } else {
//                nodataView.setVisibility(View.VISIBLE);
//                watchListRl.setVisibility(View.GONE);
//            }
//        } else {//不是第一页直接添加数据更新列表
//            if (entity.getList().size() == 0) {
//                watchSfl.finishLoadMoreWithNoMoreData();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        watchSfl.setNoMoreData(false);
//                    }
//                }, 1000);
//            } else {
//                presenter.page++;
//                listBeans.addAll(entity.getList());
//                blackListAdapter.notifyDataSetChanged();
//            }
//
//        }
//    }

}