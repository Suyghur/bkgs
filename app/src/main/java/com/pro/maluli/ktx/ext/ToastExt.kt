package com.pro.maluli.toolkit

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

/**
 * @author #Suyghur.
 * Created on 2022/5/26
 */

fun Context.showToast(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    val layout = View.inflate(this, resources.getIdentifier("toast_tips", "layout", packageName), null)
    val tvContent = layout.findViewById(resources.getIdentifier("bkgs_tv_content", "id", packageName)) as TextView
    // 设置toast文本，把设置好的布局传进来
    // 设置toast文本，把设置好的布局传进来
    tvContent.text = msg
    toast.view = layout
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}