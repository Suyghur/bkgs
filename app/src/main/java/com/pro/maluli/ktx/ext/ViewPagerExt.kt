package com.pro.maluli.ktx.ext

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * @author #Suyghur.
 * Created on 2022/9/7
 */

fun View.updatePagerHeightForChild(pager: ViewPager) {
    post {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        measure(wMeasureSpec, hMeasureSpec)
        if (pager.layoutParams.height != measuredHeight) {
            pager.layoutParams = (pager.layoutParams).also { lp ->
                lp.height = measuredHeight
            }
        }
    }
}