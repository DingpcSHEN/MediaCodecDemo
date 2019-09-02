package com.shen.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @name    Service守护 监听广播器
 * @author  SHEN
 * @version 1.0
 * 使用方法
 * step1:   继承此类并实现抽象方法
 *     @Override
 *     protected String getServiceName() {
 *         return GymSCLService.class.getName();
 *     }
 *     @Override
 *     protected void startService(Context context) {
 *         Intent inten = new Intent(context, GymSCLService.class);
 *         context.startService(inten);
 *     }
 * setp2:   将改广播进行动态注册，注意静态注册是不行的 在Application
 *     @Override
 *     public void onCreate() {
 *        GymSCLReceiver receiver = new GymSCLReceiver();
 *        receiver.registerReceiver(this);
 *     }
 */
public abstract class ServiceDaemonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            if(isKeepLive()){
                try {
                    boolean isRuning = LiveUtils.isServiceRunning(context, getServiceName());
                    if (!isRuning) {
                        startService(context);
                    }
                }catch (Exception e){
                }
            }
        }
    }
    /**
     * 注册广播
     * @param context
     */
    public void registerReceiver(Context context) {
        //开启系统时间广播(动态注册,不能静态注册)
        //部分机型会屏蔽时间广播
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);//系统时间，每分钟发送一次
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);//屏幕打开（解锁）
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);//屏幕关闭
            context.registerReceiver(this, intentFilter);
        }catch (Exception e){
        }
    }
    public void unRegisterReceiver(Context context){
        try {
            context.unregisterReceiver(this);
        }catch (Exception e){
        }
    }

    /**
     * 是否保活
     * @return
     */
    protected abstract boolean isKeepLive();

    /**
     * Service全名
     * @return
     */
    protected abstract String getServiceName();

    /**
     * 启动要保护的服务
     * @param context
     */
    protected abstract void startService(Context context);
}
