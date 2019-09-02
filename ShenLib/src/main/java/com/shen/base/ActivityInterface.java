package com.shen.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface ActivityInterface {

    /** 获取类名*/
    String getActivityName();

    /** 获取实例*/
    Activity getActivityThis();



    /** 是否全屏模式*/
    boolean isFullScreenStyle();


    /**
     * Activity之间传递过来的参数
     * @param recIntent
     */
    void initParm(Intent recIntent);

    /**
     * Activity异常销毁的时候数据存储
     * @param bundle
     */
    void onSaveParm(Bundle bundle);

    /**
     * Activity创建后进行数据恢复
     * @param bundle
     */
    void onRestorParm(Bundle bundle);

    /**
     * Activity在创建的时候进行数据恢复
     * @param bundle
     */
    void onCreateParm(Bundle bundle);

    /**配置初始化*/
    void initConfig();
    /** 视图初始化*/
    void initView();
    /** 数据初始化*/
    void initData();
    /** 业务初始化*/
    void initBiz();
}
