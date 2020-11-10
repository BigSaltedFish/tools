package io.ztc.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * activity管理类
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;
    private List<Activity> list;

    private AppManager(){}
    /**
     * 单一实例
     */
    public static AppManager getAppManager(){
        if(instance==null){
            instance=new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<>();
        }
        activityStack.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity(){
        return activityStack.lastElement();
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 判断当前Activity是否在栈没有找到则返回null
     */
    public boolean isfindActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        if (activityStack!=null && !activityStack.isEmpty()){
            for (int i = 0, size = activityStack.size(); i < size; i++){
                if (null != activityStack.get(i)){
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        list = new ArrayList<>();
        for (Activity activity : activityStack) {
            if (!(activity.getClass().equals(cls))) {
                list.add(activity);
            }
        }
        for ( int i = 0; i<list.size(); i++){
            finishActivity(list.get(i));
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert activityMgr != null;
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception ignored) {

        }
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return
     */
    @Nullable
    public static Activity getPenultimateActivity(Activity currentActivity) {
        Activity activity = null;
        try {
            if (activityStack.size() > 1) {
                activity = activityStack.get(activityStack.size() - 2);

                if (currentActivity.equals(activity)) {
                    int index = activityStack.indexOf(currentActivity);
                    if (index > 0) {
                        // 处理内存泄漏或最后一个 Activity 正在 finishing 的情况
                        activity = activityStack.get(index - 1);
                    } else if (activityStack.size() == 2) {
                        // 处理屏幕旋转后 mActivityStack 中顺序错乱
                        activity = activityStack.lastElement();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return activity;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取类名
     */
    public static String getActivityName(Activity activity){
        ActivityManager activityManager=(ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            assert activityManager != null;
            return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        }else{
            return null;
        }
    }
}
