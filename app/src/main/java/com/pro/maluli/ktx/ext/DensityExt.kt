package com.pro.maluli.ktx.ext

import android.content.Context

/**
 * @author #Suyghur.
 * Created on 2021/09/14
 */

/**
 * 根据手机的分辨率将dp转成为px。
 */
fun dp2px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp。
 */
fun px2dp(context: Context, px: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun sp2px(context: Context, sp: Float): Int {
    val scale = context.resources.displayMetrics.scaledDensity
    return (sp * scale + 0.5f).toInt()
}

fun px2sp(context: Context, px: Float): Int {
    val scale = context.resources.displayMetrics.scaledDensity
    return (px / scale + 0.5f).toInt()
}

/**
 * 获取屏幕宽值。
 */
fun getScreenWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高值。
 */
fun getScreenHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}

/**
 * 获取屏幕像素：对获取的宽高进行拼接。例：1080X2340。
 */
fun screenPixel(context: Context): String {
    context.resources.displayMetrics.run {
        return "${widthPixels}X${heightPixels}"
    }
}