package io.ztc.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * App目录检测工具
 */
public class AppCheckUtils {
    /**
     * 获取所有App目录
     * @param context 上下文环境
     * @return app目录
     */
    public static List<String> getApkList(Context context) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        for (int i = 0; i < packageInfos.size(); i++) {
            String packName = packageInfos.get(i).packageName;
            packageNames.add(packName);
        }
        return packageNames;
    }

    /**
     * 验证各种App是否安装
     * @param context 上下文环境
     * @param packageName 应用包名
     * @return 是否已安装
     */

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        for (int i = 0; i < packageInfos.size(); i++) {
            String packName = packageInfos.get(i).packageName;
            packageNames.add(packName);
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
