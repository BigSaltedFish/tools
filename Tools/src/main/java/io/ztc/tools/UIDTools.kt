package io.ztc.tools

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat

import java.io.InputStreamReader
import java.io.LineNumberReader
import java.math.BigInteger
import java.net.NetworkInterface
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.math.roundToLong

object UIDTools {
    /**
     * 获取DEVICE_ID
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getDEVICE_ID(context: Context): String {
        return try {
            val tm = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            val deviceID = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                "null"
            }else{
                tm.deviceId
            }
            deviceID!!
        }catch (e:java.lang.Exception){
            "null"
        }
    }

    /**
     * 获取Android ID
     * @param context
     * @return
     */
    fun getANDROID_ID(context: Context): String {
        return try {
            Settings.System.getString(
                context.contentResolver,
                Settings.System.ANDROID_ID
            )
        }catch (e:java.lang.Exception){
            "null"
        }

    }

    /**
     * 获取一个随机字符串
     * @param length
     * @return
     */
    fun getXCode(length: Int): String {
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number = random.nextInt(3)
            var result: Long
            when (number) {
                0 -> {
                    result = (Math.random() * 25 + 65).roundToLong()
                    sb.append(result.toString())
                }
                1 -> {
                    result = (Math.random() * 25 + 97).roundToLong()
                    sb.append(result.toString())
                }
                2 -> sb.append(Random().nextInt(10).toString())
            }
        }
        return sb.toString()
    }

    /**
     * 获取复合绑定设备的UID
     * @param context
     * @return
     */
    fun getUID(context: Context, xCode: String): String {
        val xUID =
            getDEVICE_ID(context) + getANDROID_ID(context) + xCode
        return stringToMD5(xUID)
    }

    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context * @return
     */
    fun getMacDefault(context: Context?): String? {
        var mac = ""
        if (context == null) {
            return mac
        }
        val wifi = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        var info: WifiInfo? = null
        try {
            info = wifi.connectionInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (info == null) {
            return null
        }
        mac = info.macAddress
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH)
        }
        return mac
    }// 赋予默认值//去空格

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    val macAddress: String?
        get() {
            var macSerial: String? = null
            var str = ""
            try {
                val pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address")
                val ir = InputStreamReader(pp.inputStream)
                val input = LineNumberReader(ir)
                while (null != str) {
                    str = input.readLine()
                    if (str != null) {
                        macSerial = str.trim { it <= ' ' } //去空格
                        break
                    }
                }
            } catch (ex: java.lang.Exception) {
                // 赋予默认值
                ex.printStackTrace()
            }
            return macSerial
        }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    fun getMacFromHardware():String {
        try {
            val all: ArrayList<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true))
                    continue;
                val macBytes: ByteArray? = nif.hardwareAddress ?: return ""
                val res1 = StringBuilder();
                for (b in macBytes!!) {
                    res1.append(String.format("%02X:", b))
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        }catch (e:java.lang.Exception){

        }
        return ""
    }

    /**
     * 获取mac地址（适配所有Android版本）
     * @return
     */
    fun getMac(context: Context): String? {
        var mac: String? = ""
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = macAddress
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware()
        }
        return mac
    }

    /**
     * MD5
     * @param plainText
     * @return
     */
    fun stringToMD5(plainText: String): String {
        val secretBytes: ByteArray? = try {
            MessageDigest.getInstance("md5").digest(
                plainText.toByteArray()
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("没有这个md5算法！")
        }
        var md5code = BigInteger(1, secretBytes).toString(16)
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }

    /**
     * 获取设备UID
     */
    fun getUID(context: Context):String{
        return stringToMD5("${getDEVICE_ID(context)}${getANDROID_ID(context)}")
    }
}