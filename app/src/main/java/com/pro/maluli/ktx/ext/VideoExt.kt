package com.pro.maluli.ktx.ext

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream

/**
 * @author #Suyghur.
 * Created on 2022/7/14
 */

private fun File.insertVideo(context: Context): Uri {
    val paramLong = System.currentTimeMillis()
    val localContentValues = ContentValues()
    localContentValues.put(MediaStore.Audio.Media.TITLE, name)
    localContentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, name)
    localContentValues.put(MediaStore.Audio.Media.MIME_TYPE, "video/mp4")
    localContentValues.put(MediaStore.Audio.Media.DATE_ADDED, paramLong)
    localContentValues.put(MediaStore.Audio.Media.DATA, absolutePath)
    localContentValues.put(MediaStore.Audio.Media.SIZE, length())

    return context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues)!!

}

fun String.copyVideo(context: Context) {
    try {
        val file = File(this)
        val uri = file.insertVideo(context)
        // 因为只需要向URI指向的目录里写文件，因此只要设置为w的写入模式
        val descriptor = context.contentResolver.openAssetFileDescriptor(uri, "w")
        descriptor?.apply {

            val inputStream = FileInputStream(this@copyVideo)
            val outputStream = createOutputStream()
            var byteRead = 0;
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer).also { byteRead = it } != -1) {
                outputStream.write(buffer, 0, byteRead)
            }
            inputStream.close()
            outputStream.close()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}