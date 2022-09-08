package com.pro.maluli.module.home.base.fragment.rechild;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.module.home.base.fragment.rechild.adapter.HomeLiveAdapter;
import com.pro.maluli.module.home.base.fragment.rechild.presenter.ChildLiveListPresenter;
import com.pro.maluli.module.home.base.fragment.rechild.presenter.IChildLiveListContraction;
import com.pro.maluli.module.home.previewLive.PreviewLiveAct;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页信息
 *
 * @author 23203
 */
public class ChildLiveListFrag extends BaseMvpFragment<IChildLiveListContraction.View, ChildLiveListPresenter> implements IChildLiveListContraction.View {

    private static final String ARG_COLUMN_COUNT = "column-count";

    @BindView(R.id.liveRlv)
    RecyclerView liveRlv;
    @BindView(R.id.nodataView)
    View nodataView;
    String category_id;
    HomeLiveAdapter homeLiveAdapter;
    private List<LiveListEntity.DataBean.ListBean> dataBeans = new ArrayList<>();

    public static Fragment newInstance(String category_id) {
        ChildLiveListFrag treasureGameFrag = new ChildLiveListFrag();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_COLUMN_COUNT, category_id);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    @Override
    public ChildLiveListPresenter initPresenter() {
        return new ChildLiveListPresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
        category_id = getArguments().getString(ARG_COLUMN_COUNT);
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home_video_list;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        liveRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeLiveAdapter = new HomeLiveAdapter(dataBeans, getActivity());
        liveRlv.setAdapter(homeLiveAdapter);

        homeLiveAdapter.addChildClickViewIds(R.id.mainLiveItemLl);
        homeLiveAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                // type 直播间类型 (1:一对一, 2:一对多,3:接受预约,4:闲置中)
                Bundle bundle = new Bundle();
                bundle.putString("ANCHOR_ID", String.valueOf(dataBeans.get(position).getAnchor_id()));
                bundle.putString("ROOM_ID", String.valueOf(dataBeans.get(position).getRoom_id()));
                gotoActivity(PreviewLiveAct.class, false, bundle);
            }
        });

        // 打开或关闭加载更多功能（默认为true）
        homeLiveAdapter.getLoadMoreModule().setEnableLoadMore(true);
        // 是否自定加载下一页（默认为true）
        homeLiveAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 当数据不满一页时，是否继续自动加载（默认为true）
        homeLiveAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(true);
        // 所有数据加载完成后，是否允许点击（默认为false）
        homeLiveAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(true);
        // 设置加载更多监听事件
        homeLiveAdapter.getLoadMoreModule().setOnLoadMoreListener(new com.chad.library.adapter.base.listener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.getHomeLiveList(category_id);
            }
        });
    }

    @Override
    public void onError(int code, String msg) {
        super.onError(code, msg);
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showShort(msg);
        }
        if (homeLiveAdapter != null) {
            homeLiveAdapter.getLoadMoreModule().loadMoreFail();
        }
    }

    @Override
    public void doBusiness() {
        presenter.page = 1;
        presenter.getHomeLiveList(category_id);
    }

//    private void lazyLoad() {
//        if (getUserVisibleHint() && isPrepared) {
//            startVideo();
//            try {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setMuteStatus();
//                    }
//                }, 300);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            isLazyLoaded = true;
//        } else {
//            //当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
//            if (isLazyLoaded) {
//                if (isVideo()) {
//                    GSYVideoManager.releaseAllVideos();
//                }
//            }
//        }
//    }

    @Override
    public void setHomeLiveListData(LiveListEntity entity) {
        // 第一页直接删除数据然后加载数据
        if (presenter.page == 1) {
            if (entity.getData().getList().size() != 0) {
                dataBeans.clear();
                dataBeans.addAll(entity.getData().getList());
                presenter.page++;
                liveRlv.setVisibility(View.VISIBLE);
                nodataView.setVisibility(View.GONE);
                homeLiveAdapter.notifyDataSetChanged();
            } else {
                nodataView.setVisibility(View.VISIBLE);
                liveRlv.setVisibility(View.GONE);
            }
            homeLiveAdapter.getLoadMoreModule().loadMoreComplete();
        } else {
            // 不是第一页直接添加数据更新列表
            if (entity.getData().getList().size() == 0) {
                homeLiveAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                homeLiveAdapter.getLoadMoreModule().loadMoreComplete();
                presenter.page++;
                dataBeans.addAll(entity.getData().getList());
                homeLiveAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}