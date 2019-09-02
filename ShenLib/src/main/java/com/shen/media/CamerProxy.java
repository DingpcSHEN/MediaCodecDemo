package com.shen.media;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Range;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CamerProxy {


    //摄像头真正实例
    private CameraDevice cameraDevice;
    //摄像头数据读取类
    private ImageReader captureReader;
    //采集数据通信会话
    private CameraCaptureSession captureSession;
    //采集数据线程
    private HandlerThread captureHandler;

    public CamerProxy(CameraDevice device){
        this.cameraDevice=device;
        this.captureHandler=new HandlerThread("capture");
    }


    /**
     * 关闭摄像头
     */
    public void close(){
        //停止会话的重复请求
        if(captureSession!=null){
            try{
                captureSession.stopRepeating();
            }catch (Exception e){}
            try{
                captureSession.close();
            }catch (Exception e){}
            captureSession=null;
        }
        //停止采集线程
        try{
            captureHandler.quitSafely();
        }catch (Exception e){}
        if(captureReader!=null){
            try{
                captureReader.close();
            }catch (Exception e){}
        }
        //关闭device
        if(cameraDevice!=null){
            try{
                cameraDevice.close();
            }catch (Exception e){}
            cameraDevice=null;
        }
    }

    /**
     * 采集数据
     * @param configer
     * @param preview
     * @param callBack
     */
    public void capture(@NonNull final MediaConfiger configer, final SurfaceHolder preview, final CallBack callBack){
        //开启采集线程
        captureHandler.start();
        //创建采集读取器
        captureReader = ImageReader.newInstance(configer.getWidth(),configer.getHeight(), configer.getCameraImageFormat(), 2);
        captureReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage ();
                //我们可以将这帧数据转成字节数组
                byte[][] yuv=new byte[3][];
                for(int i=0;i<image.getPlanes().length;i++){
                    ByteBuffer buffer=image.getPlanes()[i].getBuffer();
                    yuv[i] = new byte[buffer.remaining()];
                    buffer.get(yuv[i]);
                }
                if(callBack!=null) callBack.onPreviewFrame(yuv);
                image.close();
            }
        }, new Handler(captureHandler.getLooper()));
        try {
            //创建请求 TEMPLATE_PREVIEW表示预览
            CaptureRequest.Builder request = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaceList=new ArrayList<>();
            //添加Surface 不加的话就会导致ImageReader的onImageAvailable()方法不会回调
            if(captureReader!=null){
                request.addTarget(captureReader.getSurface());
                surfaceList.add(captureReader.getSurface());
            }
            //添加Surface 不加的话就会导致预览界面没有图像
            if(preview!=null) {
                request.addTarget(preview.getSurface());
                surfaceList.add(preview.getSurface());
            }
            //设置帧率范围
            request.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range<>(configer.getFps(),configer.getFps()));
            //创建一个会话和请求 用来请求预览数据
            final CaptureRequest captureRequest = request.build();
            cameraDevice.createCaptureSession(surfaceList,new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    close();
                    if(callBack!=null) callBack.onErrorCreateSession("createCaptureSession onError:onConfigureFailed");
                }
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        captureSession = session;
                        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                        captureSession.setRepeatingRequest(captureRequest, null, null);
                    }catch (Exception e){
                        close();
                        if(callBack!=null) callBack.onErrorRequest("setRepeatingRequest onError:"+e.toString());
                    }
                }
            },null);
        }catch (Exception e){
            close();
            if(callBack!=null) callBack.onErrorCreateSession("createCaptureSession onError:"+e.toString());
        }
    }










    /**
     * 摄像头回调接口
     */
    public interface CallBack{
        //摄像头打开成功
        void onOpened(CamerProxy camer);
        //与摄像头连接断开
        void onDisconnected();
        //打开摄像头错误
        void onErrorOpenCamera(String msg);
        //创建会话错误
        void onErrorCreateSession(String msg);
        //请求数据错误
        void onErrorRequest(String msg);
        //返回帧数据
        void onPreviewFrame(byte[][] yuv);
    }
}
