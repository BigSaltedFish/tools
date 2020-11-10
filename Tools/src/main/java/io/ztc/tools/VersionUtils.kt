package io.ztc.tools

import android.content.Context
import android.content.pm.PackageManager

object VersionUtils {
    /**
     * 获取版本号
     * @throws
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getVersionName(context: Context): String {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packInfo.versionName
    }

    /**
     * 版本号比较
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     * @param version1
     * @param version2
     * @return
     */
    fun compareVersion(version1: String, version2: String): Int {
        if (version1 == version2) {
            return 0
        }
        val version1Array = version1.split("\\.".toRegex()).toTypedArray()
        val version2Array = version2.split("\\.".toRegex()).toTypedArray()
        var index = 0
        // 获取最小长度值
        val minLen = Math.min(version1Array.size, version2Array.size)
        var diff = 0
        // 循环判断每位的大小
        while (index < minLen
                && version1Array[index].toInt() - version2Array[index].toInt().also { diff = it } == 0) {
            index++
        }
        return if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (i in index until version1Array.size) {
                if (version1Array[i].toInt() > 0) {
                    return 1
                }
            }
            for (i in index until version2Array.size) {
                if (version2Array[i].toInt() > 0) {
                    return -1
                }
            }
            0
        } else {
            if (diff > 0) 1 else -1
        }
    }
}