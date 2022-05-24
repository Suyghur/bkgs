package com.pro.maluli.common.view.myselfView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.pro.maluli.R;


public class MaxHeightRecyclerView extends RecyclerView {
    int maxHeight;

    public MaxHeightRecyclerView(Context context) {
        this(context, null);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs, R.styleable.MaxRecyclerView);
        maxHeight= (int) typedArray.getDimension(R.styleable.MaxRecyclerView_limit_maxHeight, SizeUtils.dp2px(200));
        typedArray.recycle();

    }




    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        boolean needLimit = false;
        if (maxHeight >= 0 ) {
            needLimit = true;
        }
        if (needLimit) {
            int limitHeight = getMeasuredHeight();
            if (getMeasuredHeight() > maxHeight) {
                limitHeight = maxHeight;
            }
            setMeasuredDimension(getMeasuredWidth(), limitHeight);

        }
    }
}
