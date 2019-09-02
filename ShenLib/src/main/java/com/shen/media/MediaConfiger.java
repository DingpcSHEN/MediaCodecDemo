package com.shen.media;

import android.graphics.ImageFormat;
import android.media.MediaCodecInfo;


import com.shen.tools.ToolDebug;

import org.json.JSONObject;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MediaConfiger {

    public final static int    DEF_BITRATE = 10000;
    public final static int    DEF_FPS = 15;
    public final static int    DEF_CAMERA_IMAGE_FORMAT = ImageFormat.YUV_420_888;
    public final static String DEF_CODEC_TYPE = "video/avc";
    public final static String DEF_CAMERA_ID = "0";
    //固定参数
    private String codecType;                                                                       //编码类型
    private String cameraID ;                                                                       //摄像头ID
    private int cameraImageFormat;                                                                  //摄像头数据格式
    private int bitrate;                                                                            //比特率
    private int fps;                                                                                //帧率
    //可变参数
    private int width;                                                                              //像素 宽度
    private int height;                                                                             //像素 高度
    private int encodeColorFormat;                                                                  //编码器颜色格式

    //数据池
    private EncoderDataPoll encoderPoll;
    private DecoderDataPoll decoderPoll;

    public MediaConfiger(){
        this.codecType=DEF_CODEC_TYPE;
        this.cameraID=DEF_CAMERA_ID;
        this.cameraImageFormat=DEF_CAMERA_IMAGE_FORMAT;
        this.bitrate=DEF_BITRATE;
        this.fps=DEF_FPS;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }
    public void setFps(int fps) {
        this.fps = fps;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public int getBitRate(){
        return this.bitrate;
    }
    public int getFps(){
        return this.fps;
    }

    public int getEncodeColorFormat() {
        return encodeColorFormat;
    }
    public void setEncodeColorFormat(int encodeColorFormat) {
        this.encodeColorFormat = encodeColorFormat;
    }
    public String getCodecType() {
        return codecType;
    }
    public String getCameraID() {
        return cameraID;
    }
    public int getCameraImageFormat() {
        return cameraImageFormat;
    }
    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("width", width);
            jsonObject.put("height",height);
            jsonObject.put("bitrate",bitrate);
            jsonObject.put("fps",fps);
            jsonObject.put("codecType",codecType);
            jsonObject.put("encodeColorForamt",encodeColorFormat);
        }catch (Exception e){
        }
        return jsonObject.toString();
    }
    /**
     * 创建一个编码器缓存池
     * @return
     */
    public EncoderDataPoll createEncoderPoll(){
        switch (encodeColorFormat){
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                encoderPoll = new PollYUV420SP(width,height);
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                encoderPoll = new PollYUV420P(width,height);
                break;
        }
        return encoderPoll;
    }
    public EncoderDataPoll getEncoderPoll(){
        return encoderPoll;
    }

    /**
     * 创建一个解码器缓存池
     * @return
     */
    public DecoderDataPoll createDecoderPoll(){
        decoderPoll=new DecoderDataPoll();
        return decoderPoll;
    }
    public DecoderDataPoll getDecoderPoll(){
        return decoderPoll;
    }

    /**
     * 解码器数据池
     */
    public class DecoderDataPoll{
        private ConcurrentLinkedQueue<byte[]> decodInQueue;
        public DecoderDataPoll() {
            decodInQueue=new ConcurrentLinkedQueue();
        }
        public boolean isEmpty(){
            return decodInQueue.isEmpty();
        }
        public int size(){
            return decoderPoll.size();
        }
        public void offer(byte[] data){
            if(data!=null) decodInQueue.offer(data);
        }
        public byte[] poll(){
            if(!decodInQueue.isEmpty()){
                return decodInQueue.poll();
            }
            return null;
        }
    }

    /**
     * 编码器数据池
     */
    public abstract class EncoderDataPoll{
        protected int w;
        protected int h;
        protected int yLength;
        protected int uLength;
        protected int vLength;
        protected byte[] fomatData;
        private ConcurrentLinkedQueue<byte[][]> yuvQueue;
        public EncoderDataPoll(int w,int h){
            this.w=w;
            this.h=h;
            this.yLength=w*h;
            this.uLength=(int)(w*h*0.25f);
            this.vLength=(int)(w*h*0.25f);
            this.yuvQueue=new ConcurrentLinkedQueue();
            this.fomatData=new byte[yLength+uLength+vLength];
        }
        public boolean isEmpty(){
            return yuvQueue.isEmpty();
        }
        public void offer(byte[][] yuvData){
            if(yuvData.length!=3)
                return;
            if(yuvData[0].length!=yLength)
                return;
            yuvQueue.offer(yuvData);
        }
        public byte[] poll(){
            if(!yuvQueue.isEmpty()){
                byte[][] yuvData=yuvQueue.poll();
                if(yuvData.length!=3)
                    return null;
                if(yuvData[0].length!=yLength)
                    return null;
                return toEncodeColorFormat(yuvData[0],yuvData[1],yuvData[2]);
            }
            return null;
        }

        /**
         * 讲Image即摄像头返回的数据转换成编码器指定的格式
         * @param y Image的Y分量
         * @param u Image的U分量  注意：有可能包含了V的数据这个时候u.length=w*h*0.5 因为包含了uv所有数据
         * @param v Image的V分量  同上
         * @return
         */
        public abstract byte[] toEncodeColorFormat(byte[] y,byte[] u,byte[] v);
    }


    public class PollYUV420P extends EncoderDataPoll{
        public PollYUV420P(int w, int h) {
            super(w, h);
        }
        @Override
        public byte[] toEncodeColorFormat(byte[] y, byte[] u, byte[] v) {
            //Image给出的数据格式为Planar u/v分量的长度分别都是w*h的四分之一
            if((u.length==uLength)&&(v.length==vLength)){
                System.arraycopy(y, 0, fomatData, 0, yLength);                    //y分量
                System.arraycopy(u, 0, fomatData, 0+yLength, uLength);            //u分量
                System.arraycopy(v, 0, fomatData, 0+yLength+uLength, vLength);    //v分量
                return fomatData;
            }
            //Image给出的数据格式为SemiPlanar u/v分量交替存储 Image并没有给我们分离出来 因此u/v分量的长度是uv分量之和即w*h的二分之一
            if((u.length==v.length)&&((u.length/2+1)>=uLength)){ //条件2可能会出现u.length=1036799 y[1].length=518400的情况
                System.arraycopy(y, 0, fomatData, 0, yLength);                    //y分量
                for(int i=0;i<uLength;i++){
                    fomatData[yLength+i]=u[2*i];                                                    //u分量
                }
                for(int i=0;i<vLength;i++){
                    fomatData[yLength+uLength+i]=v[2*i];                                            //v分量
                }
                return fomatData;
            }
            //其他未知格式
            return null;
        }
    }


    public class PollYUV420SP extends EncoderDataPoll{
        public PollYUV420SP(int w, int h) {
            super(w, h);
        }
        @Override
        public byte[] toEncodeColorFormat(byte[] y, byte[] u, byte[] v) {
            //Image给出的数据格式为Planar u/v分量的长度分别都是w*h的四分之一
            if((u.length==uLength)&&(v.length==vLength)){
                System.arraycopy(y, 0, fomatData, 0, yLength);                    //y分量
                for(int i=0;i<uLength;i++){
                    fomatData[yLength+i*2]=u[i];                                                    //交替存储u分量
                    fomatData[yLength+i*2+1]=v[i];                                                  //交替存储v分量
                }
                return fomatData;
            }
            //Image给出的数据格式为SemiPlanar u/v分量交替存储 Image并没有给我们分离出来 因此u/v分量的长度是uv分量之和即w*h的二分之一
            if((u.length==v.length)&&((u.length/2+1)>=uLength)){ //条件2可能会出现u.length=1036799 y[1].length=518400的情况
                System.arraycopy(y, 0, fomatData, 0, yLength);                    //y分量
                for(int i=0;i<uLength;i++){
                    fomatData[yLength+2*i]=u[2*i];                                                  //交替存储u分量
                    fomatData[yLength+2*i+1]=v[2*i];                                                //交替存储v分量
                }
                return fomatData;
            }
            //其他未知格式
            return null;
        }
    }
}
