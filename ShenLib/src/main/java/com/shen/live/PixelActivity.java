package com.shen.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
/**
 * 防止APP后台被杀
 * Created by jess on 2017/3/4.
 * 配置方式
 <activity
     android:name="com.shen.live.PixelActivity"
     android:configChanges="keyboardHidden|orientation|screenSize|navigation|keyboard"
     android:excludeFromRecents="true"
     android:exported="false"
     android:finishOnTaskLaunch="false"
     android:launchMode="singleInstance"
     android:process=":live"
     android:theme="@style/PixelActivity"/>
 <style name="PixelActivity">
     <item name="android:windowBackground">@android:color/transparent</item>
     <item name="android:windowFrame">@null</item>
     <item name="android:windowNoTitle">true</item>
     <item name="android:windowIsFloating">true</item>
     <item name="android:windowIsTranslucent">true</item>
     <item name="android:windowContentOverlay">@null</item>
     <item name="android:backgroundDimEnabled">false</item>
     <item name="android:windowAnimationStyle">@null</item>
     <item name="android:windowDisablePreview">true</item>
     <item name="android:windowNoDisplay">false</item>
 </style>
 */
public class PixelActivity extends Activity {

    private ProtectedAppsManager.ScreenBroadcastReceiver receiver=ProtectedAppsManager.getInstance().new ScreenBroadcastReceiver(){
        @Override
        protected void onScreenOn(Context context) {
            Log.e("SHEN_LOG","PixelActivity-onScreenOn-关闭自己");
            PixelActivity.this.finish();
        }
        @Override
        protected void onScreenOff(Context context) {
            Log.e("SHEN_LOG","PixelActivity-onScreenOff");
        }
        @Override
        protected void onUserPresent(Context context) {
            Log.e("SHEN_LOG","PixelActivity-onUserPresent-关闭自己");
            PixelActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);

        Window window=getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        WindowManager.LayoutParams params=window.getAttributes();
        params.x=0;
        params.y=0;
        params.height=1;
        params.width=1;
        window.setAttributes(params);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
