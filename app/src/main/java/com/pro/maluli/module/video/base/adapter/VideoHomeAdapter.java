package com.pro.maluli.module.video.base.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.module.video.fragment.InfoFragment;
import com.pro.maluli.module.video.fragment.VideoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class VideoHomeAdapter extends FragmentStateAdapter {
    private List<VideoEntity> list = new ArrayList<>();

    public VideoHomeAdapter(@NotNull Fragment fragmentActivity, List<VideoEntity> listUrl) {
        super(fragmentActivity);
        this.list = listUrl;
    }

    public VideoHomeAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle, List<VideoEntity> list) {
        super(fragmentManager, lifecycle);
        this.list = list;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if ("101".equalsIgnoreCase(list.get(position).getStatus_code())) {//广告fragment
            return (Fragment) InfoFragment.getNewInstance(list.get(position).getAdvert());
        } else {
            return (Fragment) VideoFragment.getNewInstance(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
