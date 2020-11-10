package io.ztc.tools

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File
import java.math.BigDecimal
import java.util.*

/**
 * 获取缓存大小并清理缓存
 */
object ClearCheckTools {
    /**
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    /**
     * 获取缓存值
     */
    fun getTotalCacheSize(context: Context): String {
        //音频数据目录
        var audioSavePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.getExternalFilesDir(null)!!.absolutePath
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        audioSavePath += "/SoundRecorder/"
        var cacheSize = getFolderSize(File(audioSavePath))

        var photoSavePath = context.getExternalFilesDir(null)!!.absolutePath
        photoSavePath += "/Pictures/"

        cacheSize += getFolderSize(File(photoSavePath))

        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除所有缓存
     */
    fun clearAllCache(context: Context) {
        //deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
            //TODO 有网页清理时注意排错，是否存在/data/data/应用package目录下找不到database文件夹的问题
            context.deleteDatabase("webview.db")
            context.deleteDatabase("webviewCache.db")
        }
    }

    /**
     * 清除音频缓存
     */
    fun clearAudioCache(context: Context):Boolean {
        var savePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.getExternalFilesDir(null)!!.absolutePath
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        savePath += "/SoundRecorder/"
        return deleteDirIn(File(savePath))
    }

    /**
     * 清除照片缓存
     */
    fun clearPhotoCache(context: Context):Boolean {
        var savePath = context.getExternalFilesDir(null)!!.absolutePath
        savePath += "/Pictures/"
        return deleteDirIn(File(savePath))
    }

    /**
     * 删除某个文件
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                val success =
                    deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        }
        return dir?.delete() ?: false
    }

    /**
     * 删除某个文件夹下的文件
     */
    private fun deleteDirIn(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()!!
            for (child in children) {
                val success = deleteDir(File(dir, child))
                if (!success) {
                    return false
                }
            }
            true
        } else {
            false
        }
    }

    /**
     * 获取文件
     */
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList = file.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    size = if (fileList[i].isDirectory) {
                        size + getFolderSize(fileList[i])
                    } else {
                        size + fileList[i].length()
                    }
                }
            }
        }
        return size
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 =
                BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 =
                BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal.valueOf(teraBytes)
        return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB")
    }
}