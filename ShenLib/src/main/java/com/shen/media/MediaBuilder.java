package com.shen.media;

import android.media.MediaCodecInfo;
import android.util.Log;
import android.util.Size;
import java.util.ArrayList;
import java.util.List;

public class MediaBuilder {
    private static final String LOG_TAG="MediaBuilder";

    private MediaConfiger configer;

    public MediaBuilder(){
        configer = new MediaConfiger();
    }

    public MediaConfiger build(){
        return configer;
    }

    /**
     * 设置像素宽高
     * @param width
     * @param height
     * @return
     */
    public MediaBuilder setSize(int width, int height){
        configer.setWidth(width);
        configer.setHeight(height);
        return this;
    }
    /**
     * 设置比特率
     * @param bit
     * @return
     */
    public MediaBuilder setBitrate(int bit){
        configer.setBitrate(bit);
        return this;
    }
    /**
     * 设置帧率
     * @return
     */
    public MediaBuilder setFps(int fps){
        configer.setFps(fps);
        return this;
    }
    /**
     * 设置编码器颜色格式
     * @param colorFormat
     * @return
     */
    public MediaBuilder setEncodeColorFormat(int colorFormat){
        configer.setEncodeColorFormat(colorFormat);
        return this;
    }

    public static MediaConfiger getLocalDefaultConfiger(){
        MediaBuilder builder=new MediaBuilder();
        EncoderManager.EncoderInfo encoder = EncoderManager.getInstance().getEncoderH264();
        if(encoder==null){
            Log.d(LOG_TAG,"getLocalDefaultConfiger--------->EncoderManager is not support H264");
            return null;
        }
        Log.d(LOG_TAG,"getLocalDefaultConfiger--------->EncoderManager is support H264");
        //判断是否支持YUV420P
        if(encoder.isSupportH264(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar)){
            builder.setEncodeColorFormat(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
            CamerManager.CameraInfo camer = CamerManager.getInstance().getCamerYUV420_888();
            Size[] sizeList=camer.getSupportSizesYUV420_888();
            List<Size> supportList=new ArrayList<>();
            for(Size size:sizeList){
                boolean isSupport=encoder.isSupportSizeH264(size.getWidth(),size.getHeight(),builder.build().getFps(),builder.build().getBitRate());
                if(isSupport){
                    Log.d(LOG_TAG,"supportList--------->size:"+size.toString());
                    supportList.add(size);
                }
            }
            //取中间值作为支持大小
            Size size=supportList.get(supportList.size()/2);
            builder.setSize(size.getWidth(),size.getHeight());
            Log.d(LOG_TAG,"getLocalDefaultConfiger--------->find configer:"+builder.build().toString());
            return builder.build();
        }
        //判断是否支持YUV420SP
        if(encoder.isSupportH264(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)){
            builder.setEncodeColorFormat(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
            CamerManager.CameraInfo camer = CamerManager.getInstance().getCamerYUV420_888();
            Size[] sizeList=camer.getSupportSizesYUV420_888();
            List<Size> supportList=new ArrayList<>();
            for(Size size:sizeList){
                boolean isSupport=encoder.isSupportSizeH264(size.getWidth(),size.getHeight(),builder.build().getFps(),builder.build().getBitRate());
                if(isSupport){
                    Log.d(LOG_TAG,"supportList--------->size:"+size.toString());
                    supportList.add(size);
                }
            }
            //取中间值作为支持大小
            Size size=supportList.get(supportList.size()/2);
            builder.setSize(size.getWidth(),size.getHeight());
            Log.d(LOG_TAG,"getLocalDefaultConfiger--------->find configer:"+builder.build().toString());
            return builder.build();
        }
        return null;
    }
}
