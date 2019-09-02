package com.shen.tools;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.shen.ShenContext;

import java.util.Arrays;
import java.util.List;

public class ToolPermission {

    /**
     * 是否拒绝某权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean isJudgePermission(Context context,@NonNull String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否拥有某个权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean isHasPermission(Context context,@NonNull String permission){
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否拥有多个权限
     * @param context
     * @param permissionList
     * @param unPermissionList
     * @return
     */
    public static boolean isHasPermissionList(Context context,@NonNull String[] permissionList,@NonNull List<String> unPermissionList){
        unPermissionList.clear();
        for (int i = 0; i < permissionList.length; i++) {
            if(!isHasPermission(context,permissionList[i])){
                unPermissionList.add(permissionList[i]);
            }
        }
        if(unPermissionList.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 请求某个权限
     * @param activity
     * @param permission
     * @return
     */
    public static void requestPermission(@NonNull Activity activity,@NonNull String permission){
        PermissionActivityWrapper activityWrapper=new PermissionActivityWrapper(activity);
        activityWrapper.requestPermission(permission);
    }
    public static void requestPermissionList(@NonNull Activity activity,@NonNull String[] permissionList){
        PermissionActivityWrapper activityWrapper=new PermissionActivityWrapper(activity);
        activityWrapper.requestPermission(permissionList);
    }

//    public interface CallBack{
//        void onRequestPermissionsResult()
//    }

    /**
     * activity权限功能包装类
     * 注意：包装模式无法实现监听Activity的某个方法调用
     */
    public static class PermissionActivityWrapper extends Activity{
        private Activity activity;
        public PermissionActivityWrapper(Activity activity){
            this.activity=activity;
        }
        /**
         * 请求权限
         * @param permission
         */
        public void requestPermission(@NonNull String permission){
            ToolDebug.printLogE(this,"requestPermission-permission="+permission);
            ActivityCompat.requestPermissions(activity, new String[]{permission}, ShenContext.Constant.CODE_REQUEST_PERMISSION);
        }
        public void requestPermission(@NonNull String[] permissionList){
            ToolDebug.printLogE(this,"requestPermission-permissionList="+Arrays.toString(permissionList));
            ActivityCompat.requestPermissions(activity, permissionList, ShenContext.Constant.CODE_REQUEST_PERMISSION);
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            ToolDebug.printLogE(this,"onRequestPermissionsResult-requestCode="+requestCode);

            if(requestCode == ShenContext.Constant.CODE_REQUEST_PERMISSION){
                ToolDebug.printLogE(this,"permissions.size="+permissions.length+" grantResults.size="+grantResults.length);

                ToolDebug.printLogE(this,"permissions="+Arrays.toString(permissions));

                ToolDebug.printLogE(this,"grantResults="+Arrays.toString(grantResults));
            }
        }
    }
}
