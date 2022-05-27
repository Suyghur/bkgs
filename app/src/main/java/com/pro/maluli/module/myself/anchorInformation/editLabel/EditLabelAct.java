package com.pro.maluli.module.myself.anchorInformation.editLabel;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.anchorInformation.addLabel.AddLabelAct;
import com.pro.maluli.module.myself.anchorInformation.editLabel.adapter.EditLabelAdapter;
import com.pro.maluli.module.myself.anchorInformation.editLabel.presenter.EditLabelPresenter;
import com.pro.maluli.module.myself.anchorInformation.editLabel.presenter.IEditLabelContraction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class EditLabelAct extends BaseMvpActivity<IEditLabelContraction.View, EditLabelPresenter> implements IEditLabelContraction.View {

    EditLabelAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.right_tv)
    TextView right_tv;
    @BindView(R.id.addLabelTv)
    TextView addLabelTv;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    private boolean isEdit;
    private int deletePosition;
    private List<AnchorLabelEntity.TagsBean> listBeans = new ArrayList<>();

    @Override
    public EditLabelPresenter initPresenter() {
        return new EditLabelPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_edit_label;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("主播标签");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        right_tv.setText("编辑");
        watchListRl.setLayoutManager(new LinearLayoutManager(this));
        blackListAdapter = new EditLabelAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);
        blackListAdapter.addChildClickViewIds(R.id.deleteLabelIv);
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                presenter.deleteLabel(listBeans.get(position).getId());
                deletePosition = position;
            }
        });
    }

    @OnClick({R.id.right_tv, R.id.addLabelTv})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.right_tv:
                if (isEdit) {
                    isEdit = false;
                    right_tv.setText("取消");
                    addLabelTv.setVisibility(View.VISIBLE);
                } else {
                    right_tv.setText("编辑");
                    addLabelTv.setVisibility(View.GONE);
                    isEdit = true;
                }
                blackListAdapter.setCanDelete(!isEdit);
                break;
            case R.id.addLabelTv:
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (listBeans.size() >= 3) {
                    ToastUtils.showShort("个人标签最多添加三个");
                    return;
                }
                gotoActivity(AddLabelAct.class);
                break;
        }
    }

    @Override
    public void doBusiness() {
    }


    @Override
    public void setLabelSuccess(AnchorLabelEntity data) {
        right_tv.setText("编辑");
        addLabelTv.setVisibility(View.GONE);
        isEdit = true;
        blackListAdapter.setCanDelete(false);
        if (data.getTags().size() > 0) {
            watchListRl.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
            listBeans.clear();
            listBeans.addAll(data.getTags());
            blackListAdapter.notifyDataSetChanged();
        } else {
            watchListRl.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getLabel();

    }

    @Override
    public void setDeleteSuccess() {

        listBeans.remove(deletePosition);
        blackListAdapter.notifyItemRemoved(deletePosition);
//必须调用这行代码
        blackListAdapter.notifyItemRangeChanged(deletePosition, listBeans.size());
    }
}