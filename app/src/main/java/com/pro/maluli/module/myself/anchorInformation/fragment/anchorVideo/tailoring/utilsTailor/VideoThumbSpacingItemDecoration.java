package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.tailoring.utilsTailor;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @author LLhon
 * @Date 2018/4/20 16:51
 * @description 视频缩略图横向RecyclerView
 */
public class VideoThumbSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace; //间距
    private int mThumbnailsCount; //缩略图item总数量

    public VideoThumbSpacingItemDecoration(int space, int thumbnailsCount) {
        mSpace = space;
        mThumbnailsCount = thumbnailsCount;
    }


    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        //第一个与最后一个添加空白间距
        if (position == 0) {
            outRect.left = mSpace;
            outRect.right = 0;
        } else if (mThumbnailsCount > 10 && position == mThumbnailsCount - 1) {
            outRect.left = 0;
            outRect.right = mSpace;
        } else {
            outRect.left = 0;
            outRect.right = 0;
        }
    }
}
