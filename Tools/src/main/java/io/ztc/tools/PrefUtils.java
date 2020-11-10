package io.ztc.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 普通数据缓存 无时间系统
 */

public class PrefUtils {

    private static SharedPreferences getShare(Context c){
        return  c.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /**
     * 存储或更改布尔型数据
     * @param key [存储键]
     * @param value [存储值]
     * @param c [环境]
     */
    @SuppressLint("ApplySharedPref")
    public static void setBoolean(String key, boolean value, Context c){
        getShare(c).edit().putBoolean(key,value).commit();
    }
    /**
     * 获取存储的布尔型数据
     * @param key [查询键]
     * @param value [默认值]
     * @param c [环境]
     */
    public static boolean getBoolean(String key, boolean value, Context c){
         return  getShare(c).getBoolean(key,value);
    }
    /**
     * 存储或更改字符串数据
     * @param key [存储键]
     * @param value [存储值]
     * @param c [环境]
     */
    @SuppressLint("ApplySharedPref")
    public static void setString(String key, String value, Context c){
        getShare(c).edit().putString(key,value).commit();
    }
    /**
     * 获取存储的字符串数据
     * @param key [查询键]
     * @param value [默认值]
     * @param c [环境]
     */
    public static String getString(String key, String value, Context c){
        return  getShare(c).getString(key,value);
    }
    /**
     * 存储或更改整型数据
     * @param key [存储键]
     * @param value [存储值]
     * @param c [环境]
     */
    @SuppressLint("ApplySharedPref")
    public static void setInt(String key, int value, Context c){
        getShare(c).edit().putInt(key,value).commit();
    }
    /**
     * 获取存储的整形数据
     * @param key [查询键]
     * @param value [默认值]
     * @param c [环境]
     */
    public static int getInt(String key, int value, Context c){
        return  getShare(c).getInt(key,value);
    }
}
