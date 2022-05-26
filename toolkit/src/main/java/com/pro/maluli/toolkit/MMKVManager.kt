package com.pro.maluli.toolkit

import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic
import java.io.File


/**
 * @author #Suyghur.
 * Created on 2021/06/25
 */
class MMKVManager private constructor() : MMKVHandler {

    companion object {
        @JvmStatic
        fun getInstance(): MMKVManager {
            return MMKVManagerHolder.INSTANCE
        }

        private object MMKVManagerHolder {
            val INSTANCE: MMKVManager = MMKVManager()
        }

        /**
         * 防止单例对象在反序列化时重新生成对象
         */
        private fun readResolve(): Any {
            return MMKVManagerHolder.INSTANCE
        }
    }

    lateinit var userKV: MMKV
    lateinit var orderKV: MMKV
    lateinit var eventKV: MMKV

    fun init(context: Context) {
        val dir = context.getExternalFilesDir("")!!.absolutePath + File.separator + "/sdk_info"
        MMKV.initialize(context, dir, MMKVLogLevel.LevelInfo)
        MMKV.registerHandler(this)
        userKV = MMKV.mmkvWithID("user_info")
        orderKV = MMKV.mmkvWithID("order_info")
        eventKV = MMKV.mmkvWithID("event_info")
    }

    fun release() {
        MMKV.unregisterHandler()
    }

    override fun onMMKVCRCCheckFail(mmapID: String?): MMKVRecoverStrategic {
        return MMKVRecoverStrategic.OnErrorDiscard
    }

    override fun onMMKVFileLengthError(mmapID: String?): MMKVRecoverStrategic {
        return MMKVRecoverStrategic.OnErrorDiscard
    }


    override fun wantLogRedirecting(): Boolean {
        return true
    }

    override fun mmkvLog(level: MMKVLogLevel?, file: String?, line: Int, function: String?, message: String?) {
        val log = "<${file.toString()}:${line}::${function}>${message}"
        Logger.d(log)
    }
}