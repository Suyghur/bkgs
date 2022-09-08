package com.pro.maluli.module.home.base.fragment.child;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.cy.tablayoutniubility.FragPageAdapterVp;
import com.cy.tablayoutniubility.TabAdapter;
import com.cy.tablayoutniubility.TabLayoutScroll;
import com.cy.tablayoutniubility.TabMediatorVp;
import com.cy.tablayoutniubility.TabViewHolder;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.ktx.ext.ViewPagerExtKt;
import com.pro.maluli.module.home.base.fragment.child.presenter.HomeChildPresenter;
import com.pro.maluli.module.home.base.fragment.child.presenter.IHomeChildContraction;
import com.pro.maluli.module.home.base.fragment.rechild.ChildLiveListFrag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页信息
 *
 * @author 23203
 */
public class HomeChildFrag extends BaseMvpFragment<IHomeChildContraction.View, HomeChildPresenter> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.childVP)
    public ViewPager childVP;
    @BindView(R.id.childTbs)
    TabLayoutScroll childTbs;

    FragPageAdapterVp<HomeInfoEntity.CategoryBean.ListBean.ChildBean> fragmentPageAdapter;
    TabAdapter<HomeInfoEntity.CategoryBean.ListBean.ChildBean> tabAdapter;

    List<Fragment> fragments;
    List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> allBeanList;

    public static Fragment newInstance(List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> children) {
        HomeChildFrag treasureGameFrag = new HomeChildFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_COLUMN_COUNT, (Serializable) children);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    @Override
    public HomeChildPresenter initPresenter() {
        return new HomeChildPresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {
    }

    public void searchDuoBaoGoods(int searchContent) {
        try {
            childVP.setCurrentItem(searchContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
        allBeanList = (List<HomeInfoEntity.CategoryBean.ListBean.ChildBean>) requireArguments().getSerializable(ARG_COLUMN_COUNT);
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home_child;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        fragments = new ArrayList<>();
//        for (HomeInfoEntity.CategoryBean.ListBean.ChildBean bean : allBeanList) {
//            fragments.add(ChildLiveListFrag.newInstance(String.valueOf(bean.getId())));
//
//        }
        fragmentPageAdapter = new FragPageAdapterVp<HomeInfoEntity.CategoryBean.ListBean.ChildBean>(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment createFragment(HomeInfoEntity.CategoryBean.ListBean.ChildBean childBean, int i) {
                return ChildLiveListFrag.newInstance(String.valueOf(childBean.getId()));
            }

            @Override
            public void bindDataToTab(TabViewHolder holder, int i, HomeInfoEntity.CategoryBean.ListBean.ChildBean childBean, boolean isSelected) {
                TextView textView = holder.getView(R.id.tv_label);
                if (isSelected) {
                    textView.setTextColor(Color.parseColor("#8E1D77"));
                    textView.setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32_1));
                } else {
                    textView.setTextColor(Color.parseColor("#94959B"));
                    textView.setBackground(getResources().getDrawable(R.drawable.shape_ffffff_10));
                }
                textView.setPadding(20, 0, 20, 0);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setText(childBean.getTitle());
            }

            @Override
            public int getTabLayoutID(int i, HomeInfoEntity.CategoryBean.ListBean.ChildBean childBean) {
                return R.layout.item_home_child_label;
            }
        };
        tabAdapter = new TabMediatorVp<HomeInfoEntity.CategoryBean.ListBean.ChildBean>(childTbs, childVP).setAdapter(fragmentPageAdapter);
        fragmentPageAdapter.add(allBeanList);
        tabAdapter.add(allBeanList);
        if (allBeanList.get(0).getTitle().equalsIgnoreCase("全部")) {
            childTbs.setVisibility(View.GONE);
        }
//
        childVP.setCurrentItem(0);
        childVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = fragmentPageAdapter.getItem(position).getView();
                if (view != null) {
                    ViewPagerExtKt.updatePagerHeightForChild(view, childVP);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void doBusiness() {

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