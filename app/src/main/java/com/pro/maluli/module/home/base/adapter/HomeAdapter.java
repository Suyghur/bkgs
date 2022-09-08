package com.pro.maluli.module.home.base.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author #Suyghur.
 * Created on 2022/9/8
 */
public class HomeAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles.isEmpty()) {
            return null;
        }
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.isEmpty()) {
            return null;
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments.isEmpty()) {
            return 0;
        }
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return fragments.get(position).hashCode();
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments.clear();
        this.fragments.addAll(fragments);
    }

    public void setTitles(List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
    }
}
