package com.pro.maluli.module.home.base.fragment.child;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.view.myselfView.CustomViewpager;
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
    CustomViewpager childVP;
    @BindView(R.id.childSPL)
    SlidingTabLayout childSPL;

    List<Fragment> fragments;
    MyPagerAdapter mAdapter;
    List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> allBeanList;
    private int selectPosition;
    private boolean isAll;

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
//        presenter.getUserInfo();
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
        allBeanList = (List<HomeInfoEntity.CategoryBean.ListBean.ChildBean>)
                getArguments().getSerializable(ARG_COLUMN_COUNT);
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home_child;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        fragments = new ArrayList<>();
//        if (allBeanList != null) {
        isAll = false;
        if (allBeanList.get(0).getTitle().equalsIgnoreCase("全部")) {
            childSPL.setVisibility(View.GONE);
        }
        for (int i = 0; i < allBeanList.size(); i++) {
            fragments.add(ChildLiveListFrag.newInstance(String.valueOf(allBeanList.get(i).getId())));
        }
//        } else {
//            isAll = true;
//            childSPL.setVisibility(View.GONE);
//            fragments.add(ChildLiveListFrag.newInstance(""));
//        }

        //new一个适配器
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        //设置ViewPager与适配器关联
        childVP.setAdapter(mAdapter);
        //设置Tab与ViewPager关联
        childSPL.setViewPager(childVP);
        setSelectPagerTab(selectPosition);
        childVP.setCurrentItem(0);   //һ��ʼ��ѡ��
        childVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition = position;
                setSelectPagerTab(selectPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void setSelectPagerTab(int selectPosition) {
        for (int i = 0; i < childSPL.getTabCount(); i++) {
            if (selectPosition == i) {
                childSPL.getTitleView(i).setTextColor(getResources().getColor(R.color.c_8e1d77));
                childSPL.getTitleView(i).setBackground(getResources().getDrawable(R.drawable.shape_8e1d77_32_1));
                childSPL.getTitleView(i).setPadding(20, 0, 20, 0);
            } else {
                childSPL.getTitleView(i).setTextColor(getResources().getColor(R.color.c_958f94));
                childSPL.getTitleView(i).setBackground(getResources().getDrawable(R.drawable.shape_ffffff_10));
                childSPL.getTitleView(i).setPadding(20, 0, 20, 0);
            }
        }
    }
//    @OnClick({R.id.iv})
//    public void onViewClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv:
//                break;
//        }
//    }

    @Override
    public void doBusiness() {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return allBeanList.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}