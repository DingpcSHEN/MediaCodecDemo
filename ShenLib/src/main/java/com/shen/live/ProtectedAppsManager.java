package com.shen.live;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * @name        应用保护管理器
 * @author      SHEN
 * @version     v2.0
 * 使用方法
 * step1:
 * 在androidmanifest.xml文件中配置
 *  <activity
 *      android:name="com.shen.live.PixelActivity"
 *      android:configChanges="keyboardHidden|orientation|screenSize|navigation|keyboard"
 *      android:excludeFromRecents="true"
 *      android:exported="false"
 *      android:finishOnTaskLaunch="false"
 *      android:launchMode="singleInstance"
 *      android:process=":live"
 *      android:theme="@style/PixelActivity"/>
 *  <style name="PixelActivity">
 *      <item name="android:windowBackground">@android:color/transparent</item>
 *      <item name="android:windowFrame">@null</item>
 *      <item name="android:windowNoTitle">true</item>
 *      <item name="android:windowIsFloating">true</item>
 *      <item name="android:windowIsTranslucent">true</item>
 *      <item name="android:windowContentOverlay">@null</item>
 *      <item name="android:backgroundDimEnabled">false</item>
 *      <item name="android:windowAnimationStyle">@null</item>
 *      <item name="android:windowDisablePreview">true</item>
 *      <item name="android:windowNoDisplay">false</item>
 *  </style>
 *  step2:
 *  在application的oncreate方法中进行初始化
 *  ProtectedAppsManager.getInstance().startProtectedApp(getContext());
 */
public class ProtectedAppsManager {

    private static ProtectedAppsManager manager=new ProtectedAppsManager();

    private ScreenBroadcastReceiver receiver=new ScreenBroadcastReceiver(){
        @Override
        protected void onScreenOn(Context context) {
            Log.e("SHEN_LOG","ProtectedAppsManager-onScreenOn");
        }
        @Override
        protected void onScreenOff(Context context) {
            Log.e("SHEN_LOG","ProtectedAppsManager-onScreenOff-启动一像素Activity");
            context.startActivity(new Intent(context,PixelActivity.class));
        }
        @Override
        protected void onUserPresent(Context context) {
            Log.e("SHEN_LOG","ProtectedAppsManager-onUserPresent");
        }
    };

    public static ProtectedAppsManager getInstance(){
        return manager;
    }
    private ProtectedAppsManager(){
    }


    /**
     * 开始应用保护 注册广播
     * @param context
     */
    public void startProtectedApp(Context context){
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            context.getApplicationContext().registerReceiver(receiver, filter);
        }catch (Exception e){
        }
    }
    public void stopProtectedApp(Context context){
        context.getApplicationContext().unregisterReceiver(receiver);
    }


    /**
     * 屏幕状态广播接收组者
     */
    public abstract class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {                               //屏幕开启事件
                onScreenOn(context);
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {                       //屏幕关闭事件
                onScreenOff(context);
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {                     //用户激活事件
                onUserPresent(context);
            }
        }
        protected abstract void onScreenOn(Context context);
        protected abstract void onScreenOff(Context context);
        protected abstract void onUserPresent(Context context);
    }
}
