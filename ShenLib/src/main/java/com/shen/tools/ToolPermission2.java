package com.shen.tools;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ToolPermission2 {


    public static final int    CODE_PERMISSION_REQ=2001;
    public static final int    CODE_PERMISSION_SETING=2002;

    /**
     * 存储空间权限
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    public static final String PERMISSION_STORE=Manifest.permission.WRITE_EXTERNAL_STORAGE;
    /**
     * 短信权限
     * Manifest.permission.SEND_SMS
     * Manifest.permission.RECEIVE_SMS
     * Manifest.permission.READ_SMS
     * Manifest.permission.RECEIVE_WAP_PUSH
     * Manifest.permission.RECEIVE_MMS
     */
    public static final String PERMISSION_SMS=Manifest.permission.SEND_SMS;

    /**
     * 位置信息权限
     * Manifest.permission.ACCESS_FINE_LOCATION
     * Manifest.permission.ACCESS_COARSE_LOCATION
     */
    public static final String PERMISSION_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;




    public interface OnCheckAndRequestListener{
        void onRequestPermission();
        void onAuthorizationPermissions();                                                          //已经授予
        void onTurnedDownPermissions();                                                             //拒绝授权
        void onHasAlreadyTurnedDown(String permission);                                             //拒绝权限
        void onHasAlreadyTurnedDownAndDontAsk(String permission);                                   //已禁止再次询问权限
    }


    /**
     * 检测并申请多个权限
     */
    public static void checkAndRequestMorePermissions(Context context, String[] permissions, OnCheckAndRequestListener listener) {
        List<String> permissionList = checkMorePermissions(context, permissions);

        if (permissionList.size() == 0) {
            if(listener!=null) listener.onAuthorizationPermissions();
        } else {
            if(listener!=null) listener.onRequestPermission();
            requestMorePermissions(context, permissionList, CODE_PERMISSION_REQ);
        }
    }

    /**
     * 申请权限结果返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param listener
     */
    public static void onRequestPermissionsResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, OnCheckAndRequestListener listener){
        if(requestCode==CODE_PERMISSION_REQ){
            List<String> permissionList = checkMorePermissions(context, permissions);
            if (permissionList.size() == 0)
                listener.onAuthorizationPermissions();
            else {
                for (int i = 0; i < permissionList.size(); i++) {
                    boolean isBannend=judgePermission(context, permissionList.get(i));
                    if(isBannend){
                        listener.onHasAlreadyTurnedDownAndDontAsk(permissionList.get(i));
                    }else{
                        listener.onHasAlreadyTurnedDown(permissionList.get(i));
                    }
                }
                listener.onTurnedDownPermissions();
            }
        }
    }




    /**
     * 检测单个权限是否授权
     * @param context
     * @param permission
     * @return      true：已授权    false：未授权
     */
    public static boolean checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
    /**
     * 检测多个权限是否授权
     * @param context
     * @param permissions
     * @return  未授权的权限
     */
    public static List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(context, permissions[i]))
                permissionList.add(permissions[i]);
        }
        return permissionList;
    }


    /**
     * 请求权限
     * @param context
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }
    public static void requestMorePermissions(Context context, List permissionList, int requestCode) {
        String[] permissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
        requestMorePermissions(context, permissions, requestCode);
    }
    public static void requestMorePermissions(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }





    /**
     * 判断是否已拒绝过权限
     * @param context
     * @param permission
     * @return 如果应用之前请求过此权限但用户拒绝，此方法将返回 true
     *          如果应用第一次请求权限或 用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false
     */
    public static boolean judgePermission(Context context, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
            return true;
        else
            return false;
    }








    /**
     * 显示权限对话框并跳转到权限授权界面
     * @param context
     */
    public static void showAndGoAppSetting(final Activity context){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("需要所有权限，请去设置中开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int w) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 9) {
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                        } else if (Build.VERSION.SDK_INT <= 8) {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                        }
                        context.startActivityForResult(intent,CODE_PERMISSION_SETING);
                    }
                }).show();
    }

}
