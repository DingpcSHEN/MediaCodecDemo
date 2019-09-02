package com.shen.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数学算法工具类
 * Created by jess on 2016/11/10.
 */
public class ToolMath {


    /**
     * 获取一个随机数
     * @param start     随机数范围
     * @param end       随机数范围
     * @return
     */
    public static int getRandom(int start,int end){
        int offset=(int)((end-start)*Math.random());
        return start+offset;
    }


    /**
     * 四舍五入转换
     * @param value
     * @param bit       保留多少位小数点
     * @return
     */
    public static float getRoundMath(float value,int bit){
        double temp=Math.pow(10f,bit);
        value=(float)(Math.round(value*temp)/temp);                                                 //如果要求精确4位就*10000然后/10000
        return value;
    }
    public static int getRoundMath(float value){
        try {
            int result = new BigDecimal(String.valueOf(value)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            return result;
        }catch (Exception e){
        }
        return (int)value;
    }

    public static String getRoundMathString(float value){
        return String.valueOf(getRoundMath(value));
    }
    /**
     * 向上转换
     * @param value
     * @return
     */
    public static int getRoundMathUp(float value){
        try {
            int result = new BigDecimal(String.valueOf(value)).setScale(0, BigDecimal.ROUND_UP).intValue();
            return result;
        }catch (Exception e){
        }
        return (int)value;
    }

    /**
     * float转换成两位小数
     * @param dec
     * @return
     */
    public static String getDecimalFloat(float dec){
        DecimalFormat decimalFormat=new DecimalFormat(".00");                                       //构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(dec);                                                         //format 返回的是字符串
        return p;
    }


    /**
     * byte转换成int
     * @param   srcM  高位
     * @param   srcL  低位
     * @return
     */
    public static int ByteToInt(byte srcM,byte srcL){
        int desM=(int)srcM;
        if(desM<0)desM=desM+256;
        int desL=(int)srcL;
        if(desL<0)desL=desL+256;
        return desM*256+desL;
    }
    public static int ByteToInt(byte src){
        int des=(int)src;
        if(des<0)des=des+256;
        return des;
    }

    /**
     * 将String编码成byte数组 默认注意char占两个字节因此使用UTF-8格式
     * @param src 被编码的字符串
     * @return    编码后的byte数组
     */
    public static byte[] StringEncodeByte(String src){
        try {
            return src.getBytes("UTF-8");
        }catch (Exception e){
            int length=src.length();
            byte []des=new byte[length];
            for(int i=0;i<length;i++) {
                des[i] = (byte) src.charAt(i);
            }
            return des;
        }
    }

    /**
     * 将byte数组解码成字符串
     * @param src   被解码的byte数组
     * @return      解码后的字符串
     */
    public static String ByteDecodeString(byte[] src){
        try {
            String des = new String(src, "UTF-8");
            return des;
        }catch (Exception e){
            return null;
        }
    }
    public static String ByteDecodeString(byte[] src, int offset, int byteCount){
        try {
            String des = new String(src,offset,byteCount,"UTF-8");
            return des;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 将byte数组转换成String  [12,20,40]无符号
     * @param src           被转换的byte数组
     * @param isUnsigned    是否无符号 true无符号 flase有符合
     * @return
     */
    public static String ByteToString(byte[] src,boolean isUnsigned){
        StringBuilder des=new StringBuilder();
        des.append('[');
        for(int i=0;i<src.length;i++){
            int temp=(int)src[i];
            if(isUnsigned)
                if(temp<0)temp=256+temp;
            des.append(temp);
            if((i+1)==src.length)
                des.append(']');
            else
                des.append(',');
        }
        return des.toString();
    }
    /**
     * 将Int数组转换成String  [12,20,40]无符号
     * @param src           被转换的byte数组
     * @return
     */
    public static String IntToString(int[] src,int startIndex,int endIndex){
        StringBuilder des=new StringBuilder();
        des.append('[');
        for(int i=startIndex;i<endIndex;i++){
            int temp=src[i];
            des.append(temp);
            if((i+1)==endIndex)
                des.append(']');
            else
                des.append(',');
        }
        return des.toString();
    }
    public static String IntToString(int[] src){
        StringBuilder des=new StringBuilder();
        des.append('[');
        for(int i=0;i<src.length;i++){
            int temp=src[i];
            des.append(temp);
            if((i+1)==src.length)
                des.append(']');
            else
                des.append(',');
        }
        return des.toString();
    }
    /**
     * 将Int数组转换成String  [12,20,40]无符号
     * @param src           被转换的byte数组
     * @return
     */
    public static String FloatToString(List<Float> src){
        StringBuilder des=new StringBuilder();
        des.append('[');
        for(int i=0;i<src.size();i++){
            float temp=src.get(i);
            des.append(temp);
            if((i+1)==src.size())
                des.append(']');
            else
                des.append(',');
        }
        return des.toString();
    }
    public static String FloatToString(float[] src){
        StringBuilder des=new StringBuilder();
        des.append('[');
        for(int i=0;i<src.length;i++){
            float temp=src[i];
            des.append(temp);
            if((i+1)==src.length)
                des.append(']');
            else
                des.append(',');
        }
        return des.toString();
    }
    /**
     * 判断byte素组是否有效数据
     * @param src
     * @param inValid   无效数据
     * @return  true 有效数据  flase 无效数据
     */
    public static boolean isByteHasValid(byte[] src,byte inValid){
        for(byte data:src){
            if(data!=inValid)
                return true;
        }
        return false;
    }
    /**
     * 将byte数组转换成Int数组
     * @param src           被转换的Byte数组
     * @param isUnsigned    是否无符号 true无符号 flase有符合
     * @return
     */
    public static int []ByteToIntArray(byte[] src,boolean isUnsigned){
        int []des=new int[src.length];
        for(int i=0;i<src.length;i++){
            des[i]=src[i];
            if(isUnsigned)
                if(des[i]<0)des[i]=des[i]+256;
        }
        return des;
    }
    /**
     * 大端模式转换成小端
     * @param   src   被转换的整形数据
     * @param isSwap  是否大小端转换 true交换顺序
     * @return
     */
    public static byte[] IntCoverByte(int src,boolean isSwap) {
        byte[] targets = new byte[4];
        if(isSwap){
            targets[3] = (byte) (src & 0xff);                                                       // 最低位
            targets[2] = (byte) ((src >> 8) & 0xff);                                                // 次低位
            targets[1] = (byte) ((src >> 16) & 0xff);                                               // 次高位
            targets[0] = (byte) (src >>> 24);                                                       // 最高位,无符号右移
        }else {
            targets[0] = (byte) (src & 0xff);                                                       // 最低位
            targets[1] = (byte) ((src >> 8) & 0xff);                                                // 次低位
            targets[2] = (byte) ((src >> 16) & 0xff);                                               // 次高位
            targets[3] = (byte) (src >>> 24);                                                       // 最高位,无符号右移
        }
        return targets;
    }
    /**
     * Byte数组转换成Int
     * @param src       被转换的数组
     * @param isSwap    是否交换顺序
     * @return
     */
    public static int ByteCoverInt(byte[] src,boolean isSwap) {
        int targets;
        if(isSwap) {
            targets = (src[3] & 0xff)                                                               // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
                    | ((src[2] << 8) & 0xff00)
                    | ((src[1] << 24) >>> 8)
                    | (src[0] << 24);
        }else{
            targets = (src[0] & 0xff)                                                               // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
                    | ((src[1] << 8) & 0xff00)
                    | ((src[2] << 24) >>> 8)
                    | (src[3] << 24);
        }
        return targets;
    }
    /**
     * 大端模式转换成小端
     * @param   src   被转换的整形数据
     * @param isSwap  是否大小端转换 true交换顺序
     * @return
     */
    public static byte[] LongCoverByte(long src,boolean isSwap) {
        byte[] targets = new byte[8];
        if(isSwap){
            targets[7] = (byte) (src & 0xff);                                                       // 最低位
            targets[6] = (byte) ((src >> 8) & 0xff);                                                // 次低位
            targets[5] = (byte) ((src >> 16) & 0xff);                                               // 次高位
            targets[4] = (byte) ((src >>> 24)&(0xff));                                              // 最高位,无符号右移
            targets[3] = (byte) ((src >>> 32)&(0xff));                                              // 最高位,无符号右移
            targets[2] = (byte) ((src >>> 40)&(0xff));                                              // 最高位,无符号右移
            targets[1] = (byte) ((src >>> 48)&(0xff));                                              // 最高位,无符号右移
            targets[0] = (byte) ((src >>> 56)&(0xff));                                              // 最高位,无符号右移
        }else {
            targets[0] = (byte) (src & 0xff);                                                       // 最低位
            targets[1] = (byte) ((src >> 8) & 0xff);                                                // 次低位
            targets[2] = (byte) ((src >> 16) & 0xff);                                               // 次高位
            targets[3] = (byte) ((src >>> 24)&(0xff));                                              // 最高位,无符号右移
            targets[4] = (byte) ((src >>> 32)&(0xff));                                              // 最高位,无符号右移
            targets[5] = (byte) ((src >>> 40)&(0xff));                                              // 最高位,无符号右移
            targets[6] = (byte) ((src >>> 48)&(0xff));                                              // 最高位,无符号右移
            targets[7] = (byte) ((src >>> 56)&(0xff));                                              // 最高位,无符号右移
        }
        return targets;
    }
    /**
     * 交换Int高低位顺序
     * @param src
     * @return
     */
    public static int IntSwapPort(int src){
        byte temp[]=IntCoverByte(src, true);
        int targts=ByteCoverInt(temp,false);
        return targts;
    }


    /**
     * 将List转换成数组
     * @param list
     * @return
     */
    public static int[] IntListToArray(ArrayList<Integer> list){

        int [] values=new int[list.size()];
        for(int i=0;i<list.size();i++)
            values[i]=list.get(i);
        return values;
    }

    /**
     * 将List转换成数组
     * @param list
     * @return
     */
    public static byte[] ByteListToArray(ArrayList<Byte> list){
        byte [] values=new byte[list.size()];
        for(int i=0;i<list.size();i++)
            values[i]=list.get(i);
        return values;
    }


    /**
     * 获取列表中最大心率值
     * @param list  不为NULL获取列表的最大值  否则获取最大上限心率值
     * @return
     */
    public static int getMaxList(ArrayList<Integer> list) {
        int max=0;
        if(list!=null){
            for(Integer value:list){
                int temp=value.intValue();
                if(temp>=max){
                    max=temp;
                }
            }
        }
        return max;
    }
    /**
     * 从List计算平均值
     * @param list
     * @return
     */
    public static float getListAvgFloat(List<Float> list){

        if(list==null||list.size()==0) return 0;

        float avg=0;
        for(Float f:list){
            avg=avg+f;
        }
        return avg/list.size();
    }














    /**
     * 将byte数组转换为16进制格式的字符串
     * @param bytes byte数组
     * @param bSpace  是否在每两个数组中间添加空格
     * @return 返回16进制格式的字符串
     */
    public static String bytesToHexString(byte[] bytes, boolean bSpace) {
        if(bytes == null || bytes.length <= 0)
            return null;
        StringBuffer stringBuffer = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                stringBuffer.append(0);
            stringBuffer.append(sTemp);
            if(bSpace)
                stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }
    /**
     * 将字符串转换为byte数组
     * @param hexString 16进制格式的字符串（仅包含0-9，a-f,A-F,且长度为偶数)
     * @return 返回转换后的byte数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if(hexString == null)
            return null;
        hexString = hexString.replace(" ", "");
        hexString = hexString.toUpperCase();
        int len = (hexString.length() / 2);
        if(len <= 0)
            return null;
        byte[] result = new byte[len];
        char[] achar = hexString.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 截取数组指定长度部分
     * @param array
     * @param length
     * @return
     */
    public static byte[] subByteArray(byte []array,int length){
        byte []sub=new byte[length];
        for(int i=0;i<length;i++)
            sub[i]=array[i];
        return sub;
    }

    /**
     * 将一个数组拷贝到另一个数组上
     * @param dst 目标数组
     * @param dstOffset 目标数组偏移
     * @param src 源数组
     * @param srcOffset 目标数组偏移
     * @param length 拷贝的长度
     * @return 成功返回true，否则false
     */
    public static boolean cpyBytes(byte[] dst, int dstOffset, byte[] src, int srcOffset, int length) {
        if(dst == null || src == null ||
                dstOffset > dst.length || srcOffset > src.length ||
                length > (dst.length-dstOffset) || length > (src.length-srcOffset))
            return false;
        for (int i = 0; i < length; i++)
            dst[i+dstOffset] = src[i+srcOffset];
        return true;
    }
    /**
     * 两个数组比较
     * @param data1 数组1
     * @param data2 数组2
     * @return 相等返回true，否则返回false
     */
    public static boolean cmpBytes(byte[] data1, byte[] data2) {
        if (data1 == null && data2 == null)
            return true;
        if (data1 == null || data2 == null)
            return false;
        if (data1 == data2)
            return true;
        if(data1.length != data2.length)
            return false;
        int len = data1.length;
        for (int i = 0; i < len; i++){
            if(data1[i] != data2[i])
                return false;
        }
        return true;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 判断数组是否存在某个元素
     * @param src
     * @param val
     * @param <T>
     * @return
     */
    public static <T> boolean containsArray(T[] src,T val){
        for(T t:src){
            if(t.equals(val))return true;
        }
        return false;
    }
    public static boolean containsArray(int[] src,int val){
        for(int i:src){
            if(i==val)return true;
        }
        return false;
    }
}
