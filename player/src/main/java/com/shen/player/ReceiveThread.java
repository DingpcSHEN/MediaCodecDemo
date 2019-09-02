package com.shen.player;

import com.shen.tools.ToolDebug;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class ReceiveThread extends Thread{

    private boolean isStop=false;
    private Callback mCallback;
    private byte[] buffer;
    private DatagramPacket packet;
    private DatagramSocket socket;

    public interface Callback{
        void onReceiveData(byte[] data);
    }
    public void setCallback(Callback callback){
        mCallback=callback;
    }
    public void stopReceive(){
        isStop=true;
    }
    public ReceiveThread() {
        super();
        try {
            buffer = new byte[4*1024*1024];
            packet = new DatagramPacket(buffer, buffer.length);
            socket = new DatagramSocket(5004);//端口号
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            ToolDebug.printLogE(this,"ReceiveThread");
        } catch (Exception e) {
            ToolDebug.printLogE(this,"ReceiveThread-e="+e.toString());
        }
    }

    @Override
    public void run() {
        super.run();
        ToolDebug.printLogD(this,"ReceiveThread-run-isStop="+isStop);
        while (true) {
            if (isStop) {
                if (socket != null) socket.close();
                break;
            }
            if (socket != null) {
                try {
                    ToolDebug.printLogD(this,"ReceiveThread-receive");
                    socket.receive(packet);             //接收数据
                    if (mCallback != null) {
                        int length=packet.getLength();
                        byte[] data=new byte[length];
                        ToolDebug.printLogD(this,"ReceiveThread-receive-length="+length);
                        System.arraycopy(packet.getData(), 0, data, 0,length);
                        mCallback.onReceiveData(data);
                    }
                } catch (Exception e) {
                    ToolDebug.printLogE(this,"ReceiveThread-e="+e.toString());
                }
            }
        }
        ToolDebug.printLogE(this,"ReceiveThread-ran-end");
    }
}
