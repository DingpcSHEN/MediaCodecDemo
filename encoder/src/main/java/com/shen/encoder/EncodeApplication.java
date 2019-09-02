package com.shen.encoder;

import android.app.Application;

import com.shen.ShenContext;

public class EncodeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShenContext.set(this);
    }
}
