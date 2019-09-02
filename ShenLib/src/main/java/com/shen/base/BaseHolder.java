package com.shen.base;

import android.app.Activity;
import android.os.Handler;
import android.view.View;


/**
 * Created by Administrator on 2017/7/25 0025.
 */
public abstract class BaseHolder {

    protected Activity      _rootActivity;                                                          //承载Activity
    protected View          _rootView;
    protected Handler       _rootHandler;


    public BaseHolder(Activity activity) {
        this(activity,null);
    }
    public BaseHolder(Activity activity,Handler handler){
        _rootHandler=handler;
        _rootActivity = activity;
        _rootView=View.inflate(_rootActivity, initLayoutRID(), null);
        initView(_rootView);
        _rootView.setTag(this);
        initData();
    }

    public View getView(){
        return _rootView;
    }

    protected abstract int initLayoutRID();

    protected abstract void initView(View view);

    protected abstract void initData();
}
