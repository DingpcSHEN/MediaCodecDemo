package com.shen.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

public final class ActivityProxy {

    protected boolean  isDestory=false;
    private ActivityInterface iActivity;

    public ActivityProxy(ActivityInterface activity){
        iActivity=activity;
    }

    /**
     * Activity被创建
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState){
        isDestory=false;
        iActivity.initConfig();
        if(iActivity instanceof Activity) {                                                         //接收传递过来的参数
            iActivity.initParm(((Activity) iActivity).getIntent());
        }
        if(savedInstanceState!=null){                                                               //创建之前进行数据恢复
            iActivity.onCreateParm(savedInstanceState);
        }
        iActivity.initView();                                                                       //视图初始化
        iActivity.initData();                                                                       //数据初始化
        iActivity.initBiz();                                                                        //业务初始化
    }

    /**
     * Activity被销毁
     */
    public void onDestroy() {
        isDestory=true;
    }

    /**
     * Activity异常销毁数据存储
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        iActivity.onSaveParm(outState);
    }

    /**
     * Activity创建后进行数据恢复 也可以在创建的时候进行恢复 看具体场景
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState){
        iActivity.onRestorParm(savedInstanceState);
    }

}
