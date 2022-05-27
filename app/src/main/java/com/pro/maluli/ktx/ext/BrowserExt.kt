package com.pro.maluli.ktx.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

/**
 * @author #Suyghur.
 * Created on 2022/5/27
 */

fun Context.openBrowser(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

fun Fragment.openBrowser(url: String) {
    requireActivity().openBrowser(url)
}