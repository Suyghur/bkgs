package com.pro.maluli.module.video.videoact.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.module.video.videoact.fragment.VideoAnchorFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class VideoAnchorAdapter extends FragmentStateAdapter {
    private List<AnchorInfoEntity.VideoBean> list = new ArrayList<>();
    private String anchorId;

    public VideoAnchorAdapter(@NonNull @NotNull FragmentManager fragmentManager,
                              @NonNull @NotNull Lifecycle lifecycle, List<AnchorInfoEntity.VideoBean> list, String anchorId) {
        super(fragmentManager, lifecycle);
        this.list = list;
        this.anchorId = anchorId;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return (Fragment) VideoAnchorFragment.getNewInstance(list.get(position), anchorId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
