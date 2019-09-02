package com.shen.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.support.annotation.NonNull;
import android.util.Log;

import com.shen.orm.ORMExternalStorageUtil;
import com.shen.tools.ThreadManager;
import com.shen.tools.ToolDebug;

import java.nio.ByteBuffer;


public class EncoderProxy implements Runnable{

    private static final String LOG_TAG="EncoderProxy";

    //编码器实现类
    private MediaCodec encoder;
    //编码数据池
    private MediaConfiger.EncoderDataPoll dataPoll;
    //帧率
    private int fps;
    //回调接口
    private CallBack callBack;
    //时间基数
    private long baseTimeUs=0;
    //帧数量
    private long countFrame=0;
    //播放时间搓：按照帧数时间正确设置 否则出现视频卡一卡的 帧时间=时间基数+帧数量（第几帧）*（帧率/60*1000）单位毫秒
    private long getPresentationTimeUs(long indexFrame){
        return  (long)(indexFrame*(fps/60f)*1000)+baseTimeUs;
    }

    private EncoderProxy(MediaCodec encoder,MediaConfiger.EncoderDataPoll poll,int fps){
        this.encoder=encoder;
        this.dataPoll=poll;
        this.fps=fps;
    }
    public static EncoderProxy createEncoder(@NonNull MediaConfiger configer){
        try {
            MediaCodec encoder = MediaCodec.createEncoderByType(configer.getCodecType());
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(configer.getCodecType(), configer.getWidth(), configer.getHeight());
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, configer.getBitRate());
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, configer.getFps());
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, configer.getEncodeColorFormat());
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);                    //关键帧间隔时间 单位s
            Log.d(LOG_TAG, "createEncoder mediaFormat --------->" + mediaFormat.toString());
            encoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            return new EncoderProxy(encoder,configer.getEncoderPoll(),configer.getFps());
        }catch (Exception e){
            Log.e(LOG_TAG, "createEncoder Exception--------->" + e.toString());
            return null;
        }
    }
    /**
     * 开始编解码
     */
    public void start(CallBack callBack){
        this.callBack=callBack;
        this.baseTimeUs=System.currentTimeMillis();
        this.countFrame=0;
        ThreadManager.getInstance().executeFixedRunnable(this);
    }
    /**
     * 停止编解码
     */
    public void stop(){
        dataPoll=null;
        encoder.stop();
        encoder.release();
    }

    @Override
    public void run() {
        baseTimeUs=System.currentTimeMillis();
        encoder.start();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while(true){
            if(dataPoll==null)
                break;
            if(!dataPoll.isEmpty()){
                byte[] inData=dataPoll.poll();
                if(inData!=null){
                    //输入
                    int indexIn = encoder.dequeueInputBuffer(0);
//                    ToolDebug.printLogD(this, "dequeueInputBuffer index="+indexIn);
                    if(indexIn>=0){
                        ByteBuffer buf = encoder.getInputBuffer(indexIn);
                        buf.clear();
                        buf.put(inData);
                        //这里的参数4必须为真实时间 如果是当前时间因为数据转换耗时不一致会导致视频的帧时间分布不均匀 最终出现视频跳一跳的就跟丢帧一样
                        encoder.queueInputBuffer(indexIn, 0, inData.length, getPresentationTimeUs(countFrame++), MediaCodec.BUFFER_FLAG_KEY_FRAME);
                    }
                    //输出
                    int indexOut = encoder.dequeueOutputBuffer(bufferInfo, 0);
                    ToolDebug.printLogD(this, "dequeueOutputBuffer index="+indexOut);
                    if(indexOut>=0){
                        ByteBuffer outputBuffer = encoder.getOutputBuffer(indexOut);
                        byte[] outData = new byte[bufferInfo.size];
                        outputBuffer.get(outData);
                        if(callBack!=null)
                            callBack.onOutFrame(outData);
//                    fileSize=fileSize+outData.length;
//                        ORMExternalStorageUtil.appendBytes("ENCODE_TEMP_H264",outData);
//                        ToolDebug.printLogD(this, "outData = size("+outData.length+") ### fileSize="+/*fileSize+*/"----->"/*+ToolMath.bytesToHexString(outData,true)*/);
                        encoder.releaseOutputBuffer(indexOut, false);
                    }
                }
            }
        }
    }

    /**
     * 摄像头回调接口
     */
    public interface CallBack{
        //编码后输出数据
        void onOutFrame(byte[] data);
    }
}
