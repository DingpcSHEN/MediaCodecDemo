package com.shen.encoder;

import android.support.annotation.NonNull;

import com.shen.media.MediaConfiger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SendThread  {

    private byte[] buffer;
    private DatagramPacket packet;
    private DatagramSocket socket;

    public SendThread() {
        super();
        try {
            buffer = new byte[4*1024*1024];       //缓存4M
            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 5004);
            socket = new DatagramSocket(5004);//端口号
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送帧数据
     * @param data
     */
    public synchronized void sendFrameData(byte[] data){
        try {
            packet.setData(data);
            socket.send(packet);
        }catch (Exception e){
        }
    }

    /**
     * 发送配置
     * @param configer
     */
    public synchronized void sendConfiger(@NonNull MediaConfiger configer){
        try {
            JSONObject json = new JSONObject();
            json.put("type", configer.getCodecType());
            json.put("w", configer.getWidth());
            json.put("h", configer.getHeight());
            json.put("bit", configer.getBitRate());
            json.put("fps", configer.getFps());
            packet.setData(json.toString().getBytes("UTF-8"));
            socket.send(packet);
        }catch (Exception e){
        }
    }
}
