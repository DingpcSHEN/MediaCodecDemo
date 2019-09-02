package com.shen.media;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;

import com.shen.tools.ToolDebug;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecoderManager {
    private static final String LOG_TAG="DecoderManager";
    //支持的编码器列表
    private List<DecoderManager.DecoderInfo> supportDecoderList=new ArrayList<>();

    /**
     * 单例模式
     */
    private static DecoderManager instance =null;
    public static DecoderManager getInstance(){
        if(instance==null){
            synchronized (DecoderManager.class){
                if(instance==null) {
                    instance = new DecoderManager();
                }
            }
        }
        return instance;
    }
    private DecoderManager(){
        int numCodecs = MediaCodecList.getCodecCount();
        for(int i=0;i<numCodecs;i++){
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if(!codecInfo.isEncoder()) {
                supportDecoderList.add(new DecoderInfo(codecInfo));
            }
        }
    }

    /**
     * 判断配置参数是否支持 当前只判断H264
     * @param configer
     * @return
     */
    public boolean isSupportConfiger(@NonNull MediaConfiger configer){
        //遍历的编码器
        for (DecoderManager.DecoderInfo decoderInfo : supportDecoderList) {
            if(decoderInfo.isSupportH264()) {
                Log.d(LOG_TAG, "isSupportConfiger H264");
                if(decoderInfo.isSupportSizeH264(configer.getWidth(),configer.getHeight(),
                        configer.getFps(),configer.getBitRate())){
                    Log.d(LOG_TAG,"isSupportConfiger H264--------->configer.size("+configer.getWidth()+" x "+configer.getHeight()+")"+" fps("+configer.getFps()+") bitrate("+configer.getBitRate()+")");
                    return true;
                }
            }
        }
        Log.d(LOG_TAG,"isSupportConfiger return flase--------->not found");
        return false;
    }

    /**
     * 启动一个解码器进行解码
     * @param configer
     * @return
     */
    public DecoderProxy startDecode(@NonNull MediaConfiger configer, SurfaceHolder holder){
        DecoderProxy decoder = DecoderProxy.createDecoder(configer,holder);
        if(decoder!=null){
            decoder.start();
        }
        return decoder;
    }





    public class DecoderInfo{
        private MediaCodecInfo mediaCodecInfo;
        private String[] typeList;
        public DecoderInfo(@NonNull MediaCodecInfo codecInfo){
            this.mediaCodecInfo=codecInfo;
            this.typeList=codecInfo.getSupportedTypes();
        }

        /**
         * 判断是否支持H264
         * @return
         */
        public boolean isSupportH264(){
            for(String type:typeList){
                if(type.equals("video/avc")) {
                    return true;
                }
            }
            return false;
        }
        /**
         * 判断是否支持H264指定的大小
         * @param width
         * @param height
         * @param fps
         * @param bitrate
         * @return
         */
        public boolean isSupportSizeH264(int width,int height,int fps,int bitrate){
            MediaCodecInfo.CodecCapabilities cap = mediaCodecInfo.getCapabilitiesForType("video/avc");
            MediaCodecInfo.VideoCapabilities videoCap = cap.getVideoCapabilities();
            if (videoCap != null) {
                //判断是否支持视频像素宽高
                if (!videoCap.getSupportedWidths().contains(width)) {
                    Log.d(LOG_TAG, "isSupportSizeH264 return false--------->no contain width(" + width + ")");
                    return false;
                } else if (!videoCap.getSupportedHeightsFor(width).contains(height)) {
                    Log.d(LOG_TAG, "isSupportSizeH264 return false--------->no contain height(" + height + ")");
                    return false;
                }
                Log.d(LOG_TAG, "isSupportSizeH264 support size--------->" + width + "x" + height);
                //判断是否支持帧率
                if(fps>0) {
                    if (!videoCap.getSupportedFrameRatesFor(width, height).contains((double) fps)) {
                        Log.d(LOG_TAG, "isSupportSizeH264 return false--------->no contain fps(" + fps + ")");
                        return false;
                    }
                    Log.d(LOG_TAG, "isSupportSizeH264 support fps--------->" + fps);
                }
                //判断是否支持比特率
                if(bitrate>0) {
                    if (!videoCap.getBitrateRange().contains(bitrate)) {
                        Log.d(LOG_TAG, "isSupportSizeH264 return false--------->no contain bitrate(" + bitrate + ")");
                        return false;
                    }
                    Log.d(LOG_TAG, "isSupportSizeH264 support bitrate--------->" + bitrate);
                }
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("name",mediaCodecInfo.getName());
                jsonObject.put("encode",mediaCodecInfo.isEncoder());
                JSONArray jsonArray=new JSONArray();
                for (String type : typeList) {
                    MediaCodecInfo.CodecCapabilities cap = mediaCodecInfo.getCapabilitiesForType(type);
                    MediaCodecInfo.VideoCapabilities videoCap= cap.getVideoCapabilities();
                    JSONObject typeObj = new JSONObject();
                    typeObj.put("name",type);
                    typeObj.put("mineType",cap.getMimeType());
                    typeObj.put("color",Arrays.toString(cap.colorFormats));
                    typeObj.put("width",videoCap.getSupportedWidths().toString());
                    typeObj.put("height",videoCap.getSupportedHeights().toString());
                    typeObj.put("bitrate",videoCap.getBitrateRange());
                    typeObj.put("fps",videoCap.getSupportedFrameRates());
                    typeObj.put("widthAlignment",videoCap.getWidthAlignment());
                    typeObj.put("heightAlignment",videoCap.getHeightAlignment());
                    jsonArray.put(typeObj);
                }
                jsonObject.put("typeList",jsonArray);
            }catch (Exception e){
            }
            return jsonObject.toString();
        }
    }

    public void showDecoderList(){
        for(DecoderManager.DecoderInfo info:supportDecoderList){
            ToolDebug.printLogE(this,info.toString());
        }
    }

}
