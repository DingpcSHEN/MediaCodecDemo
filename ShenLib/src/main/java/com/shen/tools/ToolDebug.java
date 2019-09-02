package com.shen.tools;

import android.util.Log;


/**调试工具类
 * Created by jess on 2016/8/17.
 */
public class ToolDebug {

    public static final String DEBUG_TAG="SHEN_LOG";
    public static final String RECYCL_TAG="SHEN_RECYCL";
    public static final String SOCKET_TAG="SHEN_SOCKET";
    public static final String COUR_TAG="SHEN_COUR";
    public static final String ROUTER_TAG="SHEN_ROUTER";
    public static final String VIP_TAG="SHEN_VIP";
    public static final String PK_TAG="SHEN_PK";

    public static final String BLUE_TAG="SHEN_BLUE";             //调试BLUE
    public static final String TEST_TAG="SHEN_TEST";          //调试TEST
    public static final String MOVE_TAG="SHEN_MOVE";        //调试主题下载
    public static final String SKIP_TAG="SHEN_SKIP";       //调试跳绳
    public static final String ANTEN_TAG="SHEN_ANTEN";     //蓝牙天线

    public static final String MVP_TAG="SHEN_MVP";

    public static final String TV_HELPER="HELPER";                                               //TV helper



    public static void printException(Object object,String method,Exception e){
        Log.e(DEBUG_TAG,object.getClass().getName()+"."+method+"<----抛出异常--->"+e.toString());
    }

    public static void printLogV(String msg) {
        Log.v(DEBUG_TAG, msg);
    }
    public static void printLogV(String tag,String msg) {
        Log.v(tag, msg);
    }
    public static void printLogI(String msg) {
        Log.i(DEBUG_TAG, msg);
    }
    public static void printLogD(String tag,String msg) {
        Log.d(tag, msg);
    }
    public static void printLogD(String msg) {
        Log.d(DEBUG_TAG, msg);
    }
    public static void printLogW(String msg){
        Log.w(DEBUG_TAG, msg);
    }
    public static void printLogW(String tag,String msg){
        Log.w(tag, msg);
    }
    public static void printLogE(String msg) {
        Log.e(DEBUG_TAG, "\n"+msg);
    }
    public static void printLogE(String tag,String msg) {
        Log.e(tag, msg);
    }


    public static void printLogV(Object object,String msg){
        Log.v(DEBUG_TAG,object.getClass().getName()+"<------------>"+msg);
    }
    public static void printLogV(String tag,Object object,String msg){
        Log.v(tag,object.getClass().getName()+"<------------>"+msg);
    }
    public static void printLogD(Object object,String msg){
        Log.d(DEBUG_TAG,object.getClass().getName()+"<------------>"+msg);
    }
    public static void printLogD(String tag,Object object,String msg){
        Log.d(tag,object.getClass().getName()+"<------------>"+msg);
    }
    public static void printLogE(Object object,String msg){
        Log.e(DEBUG_TAG,object.getClass().getName()+"<------------>"+msg);
    }
    public static void printLogE(String tag,Object object,String msg){
        Log.e(tag,object.getClass().getName()+"<------------>"+msg);
    }

    public static String toString(Object... args){
        StringBuilder s=new StringBuilder();
        s.append("args=");
        for(Object arg:args){
            s.append(arg.toString()+"|");
        }
        return s.toString();
    }



}
