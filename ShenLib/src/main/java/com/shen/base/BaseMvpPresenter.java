package com.shen.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * MVP模式 P
 */
public class BaseMvpPresenter<T extends BaseMvpContract.BaseView> implements BaseMvpContract.BasePresenter<T> {
    protected T         mView;

    protected Bundle    mBundle=new Bundle();

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }


    @Override
    public void initParm(Intent recIntent) {
    }

    @Override
    public void onSaveParm(Bundle bundle) {
    }

    @Override
    public void onRestorParm(Bundle bundle) {
    }

    @Override
    public void onCreateParm(Bundle bundle) {
    }
}