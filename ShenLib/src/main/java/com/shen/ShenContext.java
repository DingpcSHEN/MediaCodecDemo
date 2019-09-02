package com.shen;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

/**
 * Created by jess on 2017/2/10.
 */
public class ShenContext {

    private static Context mContext;

    public static void set(Context context) {
        mContext = context;
    }

    public static Context get() {
        return mContext;
    }


    /**
     * 获取Application标签的String参数
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataFromAppication(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取Application标签的int参数
     * @param context
     * @param key
     * @return
     */
    public static int getMetaDataIntFromAppication(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getInt(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 获取Application标签的boolean参数
     * @param context
     * @param key
     * @return
     */
    public static boolean getMetaDataBooleanFromAppication(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getBoolean(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取Activityity标签的String类型参数值
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataFromActivity(Activity context, String key) {
        try {
            ActivityInfo info = context.getPackageManager().getActivityInfo(context.getComponentName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取Service标签的String类型参数值
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataFromService(Context context, Class<? extends Service> clazz, String key) {
        try {
            ComponentName cn = new ComponentName(context, clazz);
            ServiceInfo info = context.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取Receiver标签的String类型参数值
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataFromReceiver(Context context, Class<? extends BroadcastReceiver> clazz, String key) {
        try {
            ComponentName cn = new ComponentName(context, clazz);
            ActivityInfo info = context.getPackageManager().getReceiverInfo(cn, PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static class Constant{


        //权限申请请求码
        public final static int CODE_REQUEST_PERMISSION = 50000;
    }
}
