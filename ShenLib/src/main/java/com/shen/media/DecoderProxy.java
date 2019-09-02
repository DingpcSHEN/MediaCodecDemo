package com.shen.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;

import com.shen.tools.ThreadManager;
import com.shen.tools.ToolDebug;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DecoderProxy implements Runnable{

    private static final String LOG_TAG="DecoderProxy";

    //编码器实现类
    private MediaCodec decoder;
    //编码数据池
    private MediaConfiger.DecoderDataPoll dataPoll;
    //帧率
    private int fps;
    //时间基数
    private long baseTimeUs=0;
    //帧数量
    private long countFrame=0;
    //播放时间搓：按照帧数时间正确设置 否则出现视频卡一卡的 帧时间=时间基数+帧数量（第几帧）*（帧率/60*1000）单位毫秒
    private long getPresentationTimeUs(long indexFrame){
        return  (long)(indexFrame*(fps/60f)*1000)+baseTimeUs;
    }

    private DecoderProxy(MediaCodec decoder,MediaConfiger.DecoderDataPoll poll,int fps){
        this.decoder=decoder;
        this.dataPoll=poll;
        this.fps=fps;
    }

    public static DecoderProxy createDecoder(@NonNull MediaConfiger configer, SurfaceHolder holder){
        try{
            //通过多媒体格式名创建一个可用的解码器
            MediaCodec decoder = MediaCodec.createDecoderByType(configer.getCodecType());
            MediaFormat mediaformat = MediaFormat.createVideoFormat(configer.getCodecType(),configer.getWidth(),configer.getHeight());
            //获取h264中的pps及sps数据
            if(configer.getCodecType().equals("video/avc")) {
                byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
                byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
                mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
                mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
            }
            mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE,configer.getFps());
            decoder.configure(mediaformat, holder.getSurface(), null, 0);
            return new DecoderProxy(decoder,configer.getDecoderPoll(),configer.getFps());
        } catch (IOException e) {
            Log.e(LOG_TAG, "createDecoder Exception--------->" + e.toString());
            return null;
        }
    }
    /**
     * 开始编解码
     */
    public void start(){
        this.baseTimeUs=System.currentTimeMillis();
        this.countFrame=0;
        ThreadManager.getInstance().executeFixedRunnable(this);
    }
    /**
     * 停止编解码
     */
    public void stop(){
        dataPoll=null;
        decoder.stop();
        decoder.release();
    }


    @Override
    public void run() {
        baseTimeUs=System.currentTimeMillis();
        decoder.start();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while(true) {
            if (dataPoll == null)
                break;
            if(!dataPoll.isEmpty()){
                byte[] inData=dataPoll.poll();
                if(inData!=null) {
                    //输入
                    int indexIn = decoder.dequeueInputBuffer(-1);
                    ToolDebug.printLogD(this,"indexIn="+indexIn+"  inData="+Arrays.toString(inData));
                    if (indexIn >= 0) {
                        ByteBuffer buf = decoder.getInputBuffer(indexIn);
                        buf.clear();
                        buf.put(inData);
                        decoder.queueInputBuffer(indexIn, 0, inData.length,getPresentationTimeUs(countFrame++), MediaCodec.BUFFER_FLAG_KEY_FRAME);

                    }
                    //输出
                    int indexOut = decoder.dequeueOutputBuffer(bufferInfo, 0);
                    ToolDebug.printLogD(this,"indexOut="+indexOut);
                    if (indexOut >= 0) {
                        decoder.releaseOutputBuffer(indexOut, true);
                    }
                }
            }
        }
    }
}
