package io.ztc.tools.cache

import android.content.Context
import io.ztc.tools.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.Timestamp

class CacheFile(ctx: Context) {

    private var ctx: Context? = null
    private var FILE_CACHE_DIR = "cacheFile"

    init {
        this.ctx = ctx
    }

    /**
     * 存数据
     */
    fun put(key: String, value: String): Boolean {
        val appDir = File(FileUtils.getSaveDir(ctx), FILE_CACHE_DIR)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val filePath = File(appDir, key).absolutePath
        return if (!fileIsExists(filePath)) {
            val file = File(filePath)
            saveText(file.absolutePath, value)
        } else {
            saveText(filePath, value)
        }
    }

    fun getString(key: String): String {
        val appDir = File(FileUtils.getSaveDir(ctx), FILE_CACHE_DIR)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val filePath = File(appDir, key).absolutePath
        return if (!fileIsExists(filePath)) {
            ""
        } else {
            openText(filePath)
        }
    }

    fun remove(key: String): Boolean {
        val appDir = File(FileUtils.getSaveDir(ctx), FILE_CACHE_DIR)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, key)
        val filePath = file.absolutePath
        return if (!fileIsExists(filePath)) {
            true
        } else {
            try {
                file.delete()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun fileIsExists(strFile: String): Boolean {
        return try {
            val f = File(strFile)
            f.exists()
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        val SECOND:Long = 1000
        val MINUTE:Long = 60 * 1000
        val HOUR:Long = 60 * 60 * 1000
        val DAY:Long = 24 * 60 * 60 * 1000
        val WEEK:Long = 7 * 24 * 60 * 60 * 1000
        val MONTH:Long = 30 * 24 * 60 * 60 * 1000

        fun saveText(path: String?, txt: String): Boolean {
            return try {
                val fos = FileOutputStream(path)
                fos.write(txt.toByteArray())
                fos.close()
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun openText(path: String?): String {
            var readStr = ""
            try {
                val fis = FileInputStream(path)
                val b = ByteArray(fis.available())
                fis.read(b)
                readStr = String(b)
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return readStr
        }
    }

    /**
     * 移除指定时间以上的所有缓存文件
     */
    fun removeByTime(time: Long){
        val files = getFile( File(FileUtils.getSaveDir(ctx), FILE_CACHE_DIR).absolutePath,time)
        if (files!=null && files.isNotEmpty()){
            for (x in files){
                try {
                    x?.delete()
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    // 获取指定时间以上的文件集合
    private fun getFile(path: String?,time:Long): ArrayList<File?>? {
        val list: List<File?>? = getFiles(path, ArrayList())
        val outList:ArrayList<File?>? = ArrayList()
        if (list!=null && list.isNotEmpty()){
            for ( x in list){
                if (System.currentTimeMillis() - (x!!.lastModified())> time){
                    outList?.add(x)
                }
            }
        }
        return outList
    }

    // 获取目录下所有文件
    private fun getFiles(realpath: String?, files: ArrayList<File>): List<File?>? {
        val realFile = File(realpath)

        if (realFile.isDirectory) {
            val subFiles = realFile.listFiles()
            for (file in subFiles) {
                if (file.isDirectory) {
                    getFiles(file.absolutePath, files)
                } else {
                    files.add(file)
                }
            }
        }
        return files
    }
}
