package com.shen.media;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.SurfaceHolder;

import com.shen.ShenContext;
import com.shen.tools.ToolDebug;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CamerManager {
    private static final String LOG_TAG="CamerManager";

    private CameraManager cameraManager;
    //支持的摄像头列表
    private List<CameraInfo> supportCamerList=new ArrayList<>();

    /**
     * 单例模式
     */
    private static CamerManager instance =null;
    public static CamerManager getInstance(){
        if(instance==null){
            synchronized (CamerManager.class){
                if(instance==null) {
                    instance = new CamerManager();
                }
            }
        }
        return instance;
    }
    private CamerManager(){
        cameraManager = (CameraManager) ShenContext.get().getSystemService(Context.CAMERA_SERVICE);
        try{
            for(String cameraId:cameraManager.getCameraIdList()){
                CameraInfo cameraInfo=new CameraInfo(cameraId,cameraManager.getCameraCharacteristics(cameraId));
                supportCamerList.add(cameraInfo);
            }
        }catch (Exception e){
            Log.e(LOG_TAG, "CamerManager Exception--------->" + e.toString());
        }
    }
    /**
     * 获取默认支持YUV420_888格式的摄像头
     * 使用Cmaera2的方式基本上都支持YUV_420_888
     * @return
     */
    public CamerManager.CameraInfo getCamerYUV420_888(){
        for(CameraInfo camer:supportCamerList){
            if(camer.isSupportYUV420_888()){
                return camer;
            }
        }
        return null;
    }
    /**
     * 判断配置参数是否支持
     * @param configer
     * @return
     */
    public boolean isSupportConfiger(@NonNull MediaConfiger configer){
        //遍历所有的摄像头
        for(CameraInfo info:supportCamerList){
            if(info.isSupporFormat(configer.getCameraImageFormat(),configer.getWidth(),configer.getHeight())){
                Log.d(LOG_TAG,"isSupportConfiger cameraID("+info.getId()+") support format--------->"+"colorFormat("+configer.getCameraImageFormat()+") w("+configer.getWidth()+"） h("+configer.getHeight()+")");
                if(info.isSupportFps(configer.getFps() * 1000)){
                    Log.d(LOG_TAG,"isSupportConfiger cameraID("+info.getId()+") support fps--------->"+configer.getFps());
                    return true;
                }
            }
        }
        Log.d(LOG_TAG,"isSupportConfiger return flase--------->not found");
        return false;
    }



    /**
     * 启动一个摄像头并采集数据
     * @param configer
     * @param preview
     * @param callBack
     * SecurityException:Lacking privileges to access camera service 原因1：手机不支持Camera2 原因2：摄像头权限被拒绝
     */
    public void openCamer(@NonNull final MediaConfiger configer,final SurfaceHolder preview,final CamerProxy.CallBack callBack){
        try {
            if (ActivityCompat.checkSelfPermission(ShenContext.get(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if(callBack!=null){
                    callBack.onErrorOpenCamera("openCamera onError:has SecurityException");
                }
                return;
            }
            cameraManager.openCamera(configer.getCameraID(), new CameraDevice.StateCallback() {
                private CamerProxy camer;
                @Override
                public void onOpened(CameraDevice cameraDevice) {
                    camer=new CamerProxy(cameraDevice);
                    if(callBack!=null)
                        callBack.onOpened(camer);
                    camer.capture(configer,preview,callBack);
                }
                @Override
                public void onDisconnected(CameraDevice cameraDevice) {
                    if(callBack!=null){
                        callBack.onDisconnected();
                    }
                    if(camer!=null){
                        camer.close();
                    }
                }
                @Override
                public void onError(CameraDevice cameraDevice, int error) {
                    if(callBack!=null){
                        callBack.onErrorOpenCamera("openCamera onError:code="+error);
                    }
                    if(camer!=null){
                        camer.close();
                    }
                }
            }, null);
        }catch (Exception e){
            ToolDebug.printLogE("openCamer-4");
            if(callBack!=null) callBack.onErrorOpenCamera("openCamera onError:"+e.toString());
        }
    }










    /**
     * 支持的摄像头信息
     */
    public class CameraInfo{
        //摄像头ID
        private String id;
        //摄像头特征
        private CameraCharacteristics character;

        public CameraInfo(String id,CameraCharacteristics character){
            this.id=id;
            this.character=character;
        }
        public String getId(){
            return id;
        }
        /**
         * 是否后置摄像头
         * @return
         */
        public boolean isLensFront(){
            return (character.get(CameraCharacteristics.LENS_FACING)==CameraCharacteristics.LENS_FACING_FRONT);
        }
        /**
         * 是否前置摄像头
         * @return
         */
        public boolean isLensBack(){
            return (character.get(CameraCharacteristics.LENS_FACING)==CameraCharacteristics.LENS_FACING_BACK);
        }
        /**
         * 是否支持该帧率
         * @param fps 帧率*1000 例如帧率30 那么参数应该为30*1000
         * @return
         */
        public boolean isSupportFps(int fps){
            Range<Integer>[] fpsList=character.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            for(Range<Integer> range:fpsList){
                if(range.contains(fps))
                    return true;
            }
            return false;
        }

        /**
         * 是否支持输出格式
         * @param colorFormat 颜色格式
         * @param width       宽
         * @param height      高
         * @return
         */
        public boolean isSupporFormat(int colorFormat,int width,int height){
            StreamConfigurationMap streamMap = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int[] outFormats = streamMap.getOutputFormats();
            for(int itemFormat:outFormats){
                if(colorFormat==itemFormat){
                    Size[] sizeList=streamMap.getOutputSizes(colorFormat);
                    for(Size itemSize:sizeList){
                        if((itemSize.getWidth()==width)&&(itemSize.getHeight()==height))
                            return true;
                    }
                }
            }
            return false;
        }

        /**
         * 判断是否支持YUV_420_888格式
         * 使用Cmaera2基本上都支持YUV_420_888
         * @return
         */
        public boolean isSupportYUV420_888(){
            StreamConfigurationMap streamMap = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int[] outFormats = streamMap.getOutputFormats();
            for(int itemFormat:outFormats){
                if(itemFormat==ImageFormat.YUV_420_888){
                    return true;
                }
            }
            return false;
        }

        /**
         * 获取支持YUV_420_888格式支持的所有大小
         * @return
         */
        public Size[] getSupportSizesYUV420_888(){
            StreamConfigurationMap streamMap = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int[] outFormats = streamMap.getOutputFormats();
            for(int itemFormat:outFormats){
                if(itemFormat==ImageFormat.YUV_420_888){
                    return streamMap.getOutputSizes(itemFormat);
                }
            }
            return null;
        }
        @Override
        public String toString() {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id",id);
                jsonObject.put("lens", character.get(CameraCharacteristics.LENS_FACING));
                jsonObject.put("fps",Arrays.toString(character.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)));
                JSONArray jsonArray=new JSONArray();
                int[] outFormats = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputFormats();
                for(int itemFormat:outFormats){
                    Size[] sizeList=character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(itemFormat);
                    JSONObject jsonFormat=new JSONObject();
                    jsonFormat.put("colorID",itemFormat);
                    jsonFormat.put("size",Arrays.toString(sizeList));
                    jsonArray.put(jsonFormat);
                }
                jsonObject.put("color",jsonArray);
            }catch (Exception e){
            }
            return jsonObject.toString();
        }
    }



    public void showCamerList(){
        for(CameraInfo info:supportCamerList){
            ToolDebug.printLogE(this,info.toString());
        }
    }
}
