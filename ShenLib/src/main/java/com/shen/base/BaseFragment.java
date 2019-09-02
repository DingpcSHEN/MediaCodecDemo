package com.shen.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/27 0027.
 */
public abstract class BaseFragment extends Fragment {

    protected View          _rootView;

    protected Handler       _rootHandler;

    protected Context       _rootContext;

    private Activity        _rootActivity;

    public BaseFragment() {
        super();
    }
    public BaseFragment(Handler handler){
        super();
        _rootHandler=handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _rootView=inflater.inflate(initLayoutRID(), container, false);
        initView(_rootView);
        return _rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        _rootHandler=null;
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _rootActivity=activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _rootContext=context;
    }

    @Override
    public Context getContext() {
        Context context=super.getContext();
        if(context==null)
            context=_rootContext;
        if(context==null)
            context=_rootActivity;
        return context;
    }
    public Activity getRootActivity(){
        Activity activity=getActivity();
        if(activity==null)
            activity=_rootActivity;
        return activity;
    }

    public void onNewIntent(Intent intent){}                                                        //singleTask模式意图传递

    public void onActivityResult(int requestCode, int resultCode, Intent data){}                    //跳转返回

    protected abstract int initLayoutRID();                                                         //初始化视图

    protected abstract void initView(View view);                                                    //初始化控件

    protected abstract void initData();                                                             //初始化数据

    public void setData(){}                                                                         //设置数据


}
