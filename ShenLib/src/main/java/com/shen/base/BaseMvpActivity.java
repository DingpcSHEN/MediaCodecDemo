package com.shen.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * MVP模式 普通的V
 */
public abstract class BaseMvpActivity<T extends BaseMvpContract.BasePresenter> extends Activity implements BaseMvpContract.BaseView ,ActivityInterface{

    private ActivityProxy   proxy=new ActivityProxy(this);                                          //代理
    protected T             mPresenter=createPresenter();                                           //创建P
    protected abstract T    createPresenter();

    private void detachView() {                                                                     //分离View
        if (mPresenter != null) mPresenter.detachView();
    }
    private void attachView() {                                                                     //贴上View
        if (mPresenter != null) mPresenter.attachView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            //被创建
        super.onCreate(savedInstanceState);
        attachView();
        proxy.onCreate(savedInstanceState);
    }
    @Override
    protected void onDestroy() {                                                                    //被销毁
        super.onDestroy();
        proxy.onDestroy();
        detachView();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {                                           //异常销毁数据存储  当activity有可能被系统回收的情况下，而且是在onStop()之前  onPause -> onSaveInstanceState -> onStop
        super.onSaveInstanceState(outState);
        proxy.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){                               //异常销毁重建数据恢复  只有在activity确实是被系统回收，重新创建activity的情况下才会被调用 onPause -> onSaveInstanceState -> onStop -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume
        super.onRestoreInstanceState(savedInstanceState);
        proxy.onRestoreInstanceState(savedInstanceState);
    }





    @Override
    public void initParm(Intent recIntent) {
        mPresenter.initParm(recIntent);
    }
    @Override
    public void onSaveParm(Bundle bundle) {
        mPresenter.onSaveParm(bundle);
    }
    @Override
    public void onRestorParm(Bundle bundle) {
        mPresenter.onRestorParm(bundle);
    }
    @Override
    public void onCreateParm(Bundle bundle) {
        mPresenter.onCreateParm(bundle);
    }
    @Override
    public String getActivityName() {
        return this.getClass().getName();
    }
    @Override
    public Activity getActivityThis() {
        return this;
    }
    @Override
    public void initConfig() {
        if(isFullScreenStyle()){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置永不休眠模式
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);          //隐藏系统工具栏方式一


            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {                             // lower api
                decorView.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    View decorView = getWindow().getDecorView();
                    int uiState=decorView.getSystemUiVisibility();
                    if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {                     // lower api
                        if(uiState!=View.GONE) decorView.setSystemUiVisibility(View.GONE);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        if(uiState!=(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN))
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    }
                }
            });
        }
    }

}
