package com.pro.maluli.toolkit

import android.app.Application
import com.dolin.zap.Zap
import com.dolin.zap.entity.Config
import com.dolin.zap.entity.Level

/**
 * @author #Suyghur.
 * Created on 2022/5/25
 */
object Logger {

    private const val TAG: String = "bkgs_app"
    var DEBUG = true

    private var hasZapInit = false

    @JvmStatic
    fun initZap(application: Application) {
        val level = if (DEBUG) {
            Level.DEBUG
        } else {
            Level.INFO
        }

        //初始化Zap日志框架
        val config = Config.Builder()
            //logcat输出最低等级
            .setLogcatLevel(level)
            //是否开启缓存日志
            .setRecordEnable(true)
            //缓存日志最低等级
            .setRecordLevel(Level.DEBUG)
            //是否开启压缩缓存日志内容
            .setCompressEnable(true)
            //缓存文件的过期时间
            .setOverdueDay(3)
            //缓存文件大小限制，超过则会自动扩容新文件
            .setFileSizeLimitDay(10)
            .create()
        Zap.initialize(application, config)
        hasZapInit = true
    }

    @JvmStatic
    fun d(any: Any?) {
        d(TAG, any)
    }

    @JvmStatic
    fun d(tag: String, any: Any?) {
        if (hasZapInit) {
            Zap.d(tag, any)
        }
    }

    @JvmStatic
    fun i(any: Any?) {
        i(TAG, any)
    }

    @JvmStatic
    fun i(tag: String, any: Any?) {
        if (hasZapInit) {
            Zap.i(tag, any)
        }
    }

    @JvmStatic
    fun e(any: Any?) {
        e(TAG, any)
    }

    @JvmStatic
    fun e(tag: String, any: Any?) {
        if (hasZapInit) {
            Zap.e(tag, any)
        }
    }
}