package com.shen.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * MVP模式 P与V的接口
 */
public interface BaseMvpContract {

    interface BasePresenter<T extends BaseMvpContract.BaseView> {
        void attachView(T view);
        void detachView();


        void initParm(Intent recIntent);                                                            //Activity之间传递过来的参数
        void onSaveParm(Bundle bundle);                                                             //Activity异常销毁的时候数据存储
        void onRestorParm(Bundle bundle);                                                           //Activity创建后进行数据恢复
        void onCreateParm(Bundle bundle);                                                           //Activity在创建的时候进行数据恢复
    }
    interface BaseView {
    }
}
