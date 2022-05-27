package com.pro.maluli.module.myself.setting.blacklist;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.BlackListEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.setting.blacklist.adapter.BlackListAdapter;
import com.pro.maluli.module.myself.setting.blacklist.presenter.BlackListPresenter;
import com.pro.maluli.module.myself.setting.blacklist.presenter.IBlackListContraction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class BlackListAct extends BaseMvpActivity<IBlackListContraction.View, BlackListPresenter> implements IBlackListContraction.View {

    @BindView(R.id.blackListRl)
    RecyclerView blackListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    BlackListAdapter blackListAdapter;
    private List<BlackListEntity.ListBean> listBeans;
    private int deletePosition;

    @Override
    public BlackListPresenter initPresenter() {
        return new BlackListPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_black_list;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("黑名单");
        setBackPress();
        listBeans = new ArrayList<>();
        blackListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new BlackListAdapter(listBeans, this);
        blackListRl.setAdapter(blackListAdapter);

//         先注册需要点击的子控件id（注意，请不要写在convert方法里）
        blackListAdapter.addChildClickViewIds(R.id.deleteBlackTv);
// 设置子控件点击监听
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.deleteBlackTv) {
                    presenter.deleteBlack(listBeans.get(position).getId());
                    deletePosition = position;
                }
            }
        });
        nodataTipsTv.setText("暂无数据");

    }

    /**
     * 移除黑名单
     */
    private void removeBlack(String accid) {
        NIMClient.getService(FriendService.class).removeFromBlackList(accid)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {

                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }

    @Override
    public void doBusiness() {
        presenter.getBlackList();
    }

    @Override
    public void setBlackLsit(BlackListEntity data) {
        if (data.getList().size() > 0) {
            nodataView.setVisibility(View.GONE);
            blackListRl.setVisibility(View.VISIBLE);
            listBeans.clear();
            listBeans.addAll(data.getList());
            blackListAdapter.notifyDataSetChanged();
        } else {
            nodataView.setVisibility(View.VISIBLE);
            blackListRl.setVisibility(View.GONE);

        }

    }

    @Override
    public void setDeleteSuccess() {
        removeBlack(listBeans.get(deletePosition).getAccid());
        listBeans.remove(deletePosition);
        blackListAdapter.notifyItemRemoved(deletePosition);//通知移除该条
        blackListAdapter.notifyItemRangeChanged(deletePosition, listBeans.size() - deletePosition);//更新适配器这条后面列表的变化
        if (listBeans.size() == 0) {
            nodataView.setVisibility(View.VISIBLE);
            blackListRl.setVisibility(View.GONE);
        } else {
            nodataView.setVisibility(View.GONE);
            blackListRl.setVisibility(View.VISIBLE);
        }


    }
}