package com.shen.tools;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author      dingpc
 *              解析json工具
 * @version     v1.0.1
 * @time        2016.03.04
 * Created by jess on 2016/3/4.
 */
public class ToolJson{


    public static String    DEFAULT_STRING_VALUE="";                   //解析字符串的缺省值
    public static int       DEFAULT_INTEGER_VALUE=0;                   //解析int的缺省值
    public static float     DEFAULT_FLOAT_VALUE=0.0f;                  //解析float的缺省值
    public static boolean   DEFAULT_BOOLEAN_VALUE=false;               //解析boolean的缺省值




    /**
     * 将一个字符串解析成JSON
     * @param json
     * @return  null解析失败
     */
    public static JSONObject build(String json){
        JSONObject jsonObject = null;
        try {
            JSONTokener jsonTokener = new JSONTokener(json);
            jsonObject = (JSONObject) jsonTokener.nextValue();
        } catch (Exception e) {

        }
        return jsonObject;
    }
    /**
     * 将字符串转换成JSONObject
     * @param   json      被转换的字符串
     * @return  null     转换失败
     */
    public static JSONObject parse(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject;
        }catch (Exception e){

        }
        return null;
    }
    /**
     * 将一个字符串解析成JSONArray
     * @param   json    被解析的字符串
     * @return  null    解析失败
     */
    public static JSONArray parseArray(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            return jsonArray;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 讲一个字符串解析List<JSONObject>
     * @param   json    被解析的字符串
     * @return  解析失败返回一个空的List
     */
    public static ArrayList<JSONObject> parseList(String json) {
        JSONArray jsonArray=parseArray(json);
        ArrayList<JSONObject> jsonList=getJSONObjectListCheck(jsonArray);
        return jsonList;
    }





    /**
     *              带有校验的解析json得到一个String
     * @param json  被解析的对象
     * @param name  键名
     * @return      键值 如果没有此键返回空字符串
     * Created by jess on 2016/3/4.
     */
    public static String getStringCheck(JSONObject json,String name,String def){
        String val=def;
        if(json!=null) {
            try {
                if(json.has(name)&&(!json.isNull(name)))
                    val=json.getString(name);
            }catch (Exception e) {
            }
        }
        return val;
    }
    public static String getStringCheck(JSONObject json,String name){
        return getStringCheck(json,name,DEFAULT_STRING_VALUE);
    }
    /**
     *              带有校验的解析json得到一个Number
     * @param name  键名
     * @param type  类型
     * @return      如果没有此键返回null 否则返回Number子类
     * Created by jess on 2016/3/4.
     */
    public static Number getNumberCheck(JSONObject json,String name,String type){
        if(json!=null){
            try{
                if(json.has(name)&&(!json.isNull(name))) {
                    if(type.equals("int")) {
                        int val = json.getInt(name);
                        return new Integer(val);
                    }else if(type.equals("double")){
                        double val=json.getDouble(name);
                        return new Double(val);
                    }else if(type.equals("long")){
                        long val=json.getLong(name);
                        return new Long(val);
                    }
                }
            }catch (Exception e){

            }
        }
        return null;
    }
    /**
     *              带有校验的解析json得到一个INT
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null返回0
     * Created by jess on 2016/3/4.
     */
    public static int getIntCheck(JSONObject json,String name){
        return getIntCheck(json,name,DEFAULT_INTEGER_VALUE);
    }
    public static int getIntCheck(JSONObject json,String name,int def){
        int val=def;
        if(json!=null)
        {
            try
            {
                if(json.has(name)&&(!json.isNull(name))) {
                    val = json.getInt(name);
                }
            }catch (Exception e) {
            }
        }
        return val;
    }
    /**
     *              带有校验的解析json得到一个LONG
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null返回0
     * Created by jess on 2016/3/4.
     */
    public static long getLongCheck(JSONObject json,String name){
        return getLongCheck(json,name,DEFAULT_INTEGER_VALUE);
    }
    public static long getLongCheck(JSONObject json,String name,long def){
        long val=def;
        if(json!=null)
        {
            try
            {
                if(json.has(name)&&(!json.isNull(name))) {
                    val = json.getLong(name);
                }
            }catch (Exception e) {
            }
        }
        return val;
    }

    /**
     *              带有校验的解析json得到一个Double
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null返回0.0
     * Created by jess on 2016/3/4.
     */
    public static double getDoubleCheck(JSONObject json,String name){
        return getDoubleCheck(json,name,DEFAULT_FLOAT_VALUE);
    }
    public static double getDoubleCheck(JSONObject json,String name,double def){
        double val=DEFAULT_FLOAT_VALUE;
        if(json!=null)
        {
            try
            {
                if(json.has(name)&&(!json.isNull(name)))
                    val=json.getDouble(name);
            }catch (Exception e) {

            }
        }
        return val;
    }
    /**
     *              带有校验的解析json得到一个Boolean
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null返回0.0
     * Created by jess on 2016/3/4.
     */
    public static boolean getBooleanCheck(JSONObject json,String name){
        boolean val=DEFAULT_BOOLEAN_VALUE;
        if(json!=null)
        {
            try
            {
                if(json.has(name)&&(!json.isNull(name)))
                    val=json.getBoolean(name);
            }catch (Exception e) {

            }
        }
        return val;
    }


    /**
     *              带有校验的解析json得到一个JSONObject
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null或者解析错误返回null
     * Created by jess on 2016/3/4.
     */
    public static JSONObject getJSONObjectCheck(JSONObject json,String name){
        JSONObject jsonObject=null;
        if(json!=null)
        {
            try
            {
                if(json.has(name)&&(!json.isNull(name)))
                    jsonObject=json.getJSONObject(name);
                else
                    jsonObject=null;
            }catch (Exception e){
                jsonObject=null;
            }
        }
        return jsonObject;
    }
    /**
     *              带有校验的解析json得到一个JSONArray
     * @param name  键名
     * @return      键值 如果没有此键或者此键值为null返回一个空的JSONObject
     * Created by jess on 2016/3/7.
     */
    public static JSONArray getJSONArrayCheck(JSONObject json,String name){
        JSONArray jsonArray=null;
        if(json!=null)
        {
            try{
                if(json.has(name)&&(!json.isNull(name)))
                    jsonArray=json.getJSONArray(name);
                else
                    jsonArray=new JSONArray();
            }catch (Exception e){
                jsonArray=new JSONArray();
            }
        }
        if(jsonArray==null)
            jsonArray=new JSONArray();
        return  jsonArray;
    }
    /**
     *             带有校验的解析json得到一个ArrayList<JSONObject>
     * @param jsonArray
     * @return      键值 如果没有此键或者此键值为null返回一个空的ArrayList<JSONObject>
     * Created by jess on 2016/3/7.
     */
    public static ArrayList<JSONObject> getJSONObjectListCheck(JSONArray jsonArray){

        ArrayList<JSONObject> jsonObjects=new ArrayList<JSONObject>();

        if(jsonArray!=null)
        {
            int size=jsonArray.length();
            if(size>0){
                for(int i=0;i<size;i++){
                    try{
                        JSONObject obj=(JSONObject)jsonArray.get(i);
                        jsonObjects.add(obj);
                    }catch (Exception e){

                    }
                }
            }
        }
        return jsonObjects;

    }
    public static  ArrayList<JSONObject> getJSONObjectListCheck(JSONObject json,String name){

        JSONArray jsonArray=getJSONArrayCheck(json,name);
        ArrayList<JSONObject> jsonJSONObject=getJSONObjectListCheck(jsonArray);
        return  jsonJSONObject;
    }

    /**
     *              带有校验的解析json得到一个INT[]
     * @param jsonArray
     * @return      键值 如果没有此键或者此键值为null返回0
     * Created by jess on 2016/3/7.
     */
    public static ArrayList<Integer> getIntArrayCheck(JSONArray jsonArray){

        ArrayList<Integer> jsonIntegers=new ArrayList<Integer>();
        if(jsonArray!=null)
        {
            int size=jsonArray.length();
            if(size>0){
                for(int i=0;i<size;i++){
                    try {
                        jsonIntegers.add((Integer) jsonArray.get(i));
                    }catch (Exception e){

                    }
                }
            }
        }
        return jsonIntegers;
    }
    public static  ArrayList<Integer> getIntArrayCheck(JSONObject json,String name){

        JSONArray jsonArray=getJSONArrayCheck(json,name);
        ArrayList<Integer> jsonIntegers=getIntArrayCheck(jsonArray);
        return  jsonIntegers;
    }
    /**
     *              带有校验的解析json得到一个String[]
     * @param       jsonArray
     * @return      键值 如果没有此键或者此键值为null返回0
     * Created by jess on 2016/3/7.
     */
    public static ArrayList<String> getStringArrayCheck(JSONArray jsonArray){
        ArrayList<String> jsonString=new ArrayList<String>();
        if(jsonArray!=null)
        {
            int size=jsonArray.length();
            if(size>0){
                for(int i=0;i<size;i++){
                    try {
                        jsonString.add((String) jsonArray.get(i));
                    }catch (Exception e){

                    }
                }
            }
        }
        return jsonString;
    }
    public static  ArrayList<String> getStringArrayCheck(JSONObject json,String name){

        JSONArray jsonArray=getJSONArrayCheck(json,name);
        ArrayList<String> jsonString=getStringArrayCheck(jsonArray);
        return  jsonString;
    }

    /**
     *              带有校验的构建一个JSONObject
     * @param json
     * @param key   键名
     * @param val     键值
     * @return      返回json
     * Created by jess on 2016/3/4.
     */
    public static JSONObject put(JSONObject json,String key,String val){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key, val);
        } catch (Exception e){
        }
        return json;
    }

    /**
     *              带有校验的构建一个JSONObject
     * @param json
     * @param key   键名
     * @param val     键值
     * @return      返回json
     * Created by jess on 2016/3/4.
     */
    public static JSONObject put(JSONObject json,String key,int val){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key, val);
        } catch (Exception e){
        }
        return json;
    }
    /**
     * 带有校验的构造且加入一个long
     * @param json  被构造的JSON
     * @param key   键名
     * @param val   键值
     * @return
     */
    public static JSONObject put(JSONObject json,String key,long val){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key, val);
        } catch (Exception e){
        }
        return json;
    }

    /**
     * 带有校验的构造且加入一个float
     * @param json  被构造的JSON
     * @param key   键名
     * @param val   键值
     * @return
     */
    public static JSONObject put(JSONObject json,String key,float val){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key, val);
        } catch (Exception e){
        }
        return json;
    }

    /**
     *              带有校验的构建一个JSONObject
     * @param json
     * @param key   键名
     * @param val   键值为一个JSONObject
     * @return      返回json
     * Created by jess on 2016/3/4.
     */
    public static JSONObject put(JSONObject json,String key,JSONObject val){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key, val);
        } catch (Exception e){
        }
        return json;
    }

    /**
     * 带有校验构建一个JSONObject
     * @param json
     * @param key
     * @param value
     * @return
     */
    public static JSONObject put(JSONObject json,String key,ArrayList<Integer> value){
        if(json==null)
            json=new JSONObject();
        try {
            if(value!=null) {
                JSONArray array = new JSONArray();
                for (Integer integer : value) {
                    array.put(integer.intValue());
                }
                json.put(key, array);
            }
        }catch (Exception e){
        }
        return json;
    }
    public static JSONObject put(JSONObject json,String key,int[] value){
        if(json==null)
            json=new JSONObject();
        try {
            if(value!=null) {
                JSONArray array = new JSONArray();
                for (int temp : value)
                    array.put(temp);
                json.put(key, array);
            }
        }catch (Exception e){
        }
        return json;
    }
    /**
     * 带有校验为JSONObject添加JSONArray成员
     * @param json      被添加的JSONObject
     * @param key       被添加的成员key
     * @param value     被添加的成员value
     * @return
     */
    public static JSONObject put(JSONObject json,String key,JSONArray value){
        if(json==null)
            json=new JSONObject();
        try {
            json.put(key,value);
        }catch (Exception e){
        }
        return json;
    }

    /**
     * 带有校验为JSONArray添加JSONObject成员
     * @param json
     * @param value
     * @return
     */
    public static JSONArray put(JSONArray json,JSONObject value){
        try {
            if (json == null)
                json = new JSONArray();
            json.put(value);
        }catch (Exception e){

        }
        return json;
    }

    /**
     * 带有教研获取JSONObject的第一个键
     * @param   json  被解析的json
     * @return  null
     */
    public static String getFirstKey(JSONObject json){
        try{
            Iterator it = json.keys();
            if(it.hasNext()){
                return it.next().toString();
            }
        }catch (Exception e){
        }
        return null;
    }

}

