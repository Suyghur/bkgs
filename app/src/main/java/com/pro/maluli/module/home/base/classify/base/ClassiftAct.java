package com.pro.maluli.module.home.base.classify.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.home.base.classify.adapter.ClassiftAdapter;
import com.pro.maluli.module.home.base.classify.adapter.ClassiftChildAdapter;
import com.pro.maluli.toolkit.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class ClassiftAct extends BaseMvpActivity<IClassiftContraction.View, ClassiftPresenter> implements IClassiftContraction.View {

    @BindView(R.id.lv_category_list)
    RecyclerView lvCategoryList;
    @BindView(R.id.lv_chlid_list)
    RecyclerView lvChlidList;
    private HomeInfoEntity homeInfoEntity;
    private ClassiftAdapter adapter;
    private ClassiftChildAdapter classiftChildAdapter;
    private List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> childBeans = new ArrayList<>();
    private int selectPosition = 0;

    @Override
    public ClassiftPresenter initPresenter() {
        return new ClassiftPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        homeInfoEntity = (HomeInfoEntity) getIntent().getSerializableExtra("Home_data");
        for (HomeInfoEntity.CategoryBean.ListBean bean : homeInfoEntity.getCategory().getList()) {
            Logger.e(bean.toString());
        }
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_classift;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("分类列表");
        setBackPress();
        lvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        lvChlidList.setLayoutManager(new LinearLayoutManager(this));
        //移除自己添加的全部选项
        if (homeInfoEntity.getCategory().getList().get(0).getId() == 0) {
            // 移除全部
            homeInfoEntity.getCategory().getList().remove(0);
        }
        homeInfoEntity.getCategory().getList().get(0).setSelect(true);
//        homeInfoEntity.getCategory().getList().get(0).getChildren().remove(0);
        adapter = new ClassiftAdapter(homeInfoEntity.getCategory().getList(), ClassiftAct.this);
        lvCategoryList.setAdapter(adapter);

        classiftChildAdapter = new ClassiftChildAdapter(childBeans, this);
        childBeans.clear();
        childBeans.addAll(homeInfoEntity.getCategory().getList().get(0).getChildren());
        lvChlidList.setAdapter(classiftChildAdapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {

                homeInfoEntity.getCategory().getList().get(selectPosition).setSelect(false);
                selectPosition = position;
                homeInfoEntity.getCategory().getList().get(selectPosition).setSelect(true);
                adapter.notifyDataSetChanged();
                childBeans.clear();
                childBeans.addAll(homeInfoEntity.getCategory().getList().get(selectPosition).getChildren());
                classiftChildAdapter.notifyDataSetChanged();

            }
        });

        classiftChildAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("ClassiftOne", selectPosition);
                intent.putExtra("ClassiftTwo", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


    }


    @Override
    public void doBusiness() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}