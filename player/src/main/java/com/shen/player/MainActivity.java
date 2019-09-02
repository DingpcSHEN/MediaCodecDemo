package com.shen.player;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shen.media.DecoderManager;
import com.shen.media.DecoderProxy;
import com.shen.media.MediaBuilder;
import com.shen.media.MediaConfiger;
import com.shen.tools.ToolDebug;
import com.shen.tools.ToolJson;
import com.shen.tools.ToolPermission;

import org.json.JSONObject;

import java.security.Permission;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback ,ReceiveThread.Callback{


    private ReceiveThread mSocket;
    private SurfaceView mView;
    private MediaConfiger mConfiger;
    private DecoderProxy mDecoder;
    private SurfaceHolder mHolder;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mDecoder=DecoderManager.getInstance().startDecode(mConfiger,mHolder);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ToolPermission.requestPermissionList(this,new String[]{
//                Manifest.permission.INTERNET,
//                Manifest.permission.ACCESS_NETWORK_STATE,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.MODIFY_AUDIO_SETTINGS,
//                Manifest.permission.BLUETOOTH,
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_PHONE_STATE,
//        });

        mView=findViewById(R.id.view);
        mView.getHolder().addCallback(this);
        mSocket=new ReceiveThread();
        mSocket.setCallback(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.stopReceive();
        mDecoder.stop();
        ToolDebug.printLogD(this,"onDestroy");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder=holder;
        mSocket.start();
        ToolDebug.printLogD(this,"surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onReceiveData(byte[] data) {
//        ToolDebug.printLogD(this,"onReceiveData-data.size("+data.length+")");
        if(mConfiger==null){
            try {
                String str = new String(data, "UTF-8");
                JSONObject json=new JSONObject(str);
                String type=ToolJson.getStringCheck(json,"type");
                int width=ToolJson.getIntCheck(json,"w");
                int height=ToolJson.getIntCheck(json,"h");
                int bit=ToolJson.getIntCheck(json,"bit");
                int fps=ToolJson.getIntCheck(json,"fps");
                mConfiger=new MediaBuilder()
                        .setSize(width,height)
                        .setBitrate(bit)
                        .setFps(fps)
                        .build();
                mConfiger.createDecoderPoll();
                ToolDebug.printLogE(this,"mConfiger="+mConfiger.toString());
                ToolDebug.printLogE(this,"isSupport="+DecoderManager.getInstance().isSupportConfiger(mConfiger));
                mHandler.sendEmptyMessage(1);
            }catch (Exception e){
                ToolDebug.printLogE(this,"onReceiveData-e="+e.toString());
            }
        }else {
            if((data[0]==0x00)&&(data[1]==0x00)&&(data[2]==0x00)&&(data[3]==0x01)) {
                mConfiger.getDecoderPoll().offer(data);
                ToolDebug.printLogD(this,"onReceiveData-data="+Arrays.toString(data));
            }else{
                ToolDebug.printLogD(this,"onReceiveData-conf="+mConfiger.toString());
            }
        }
    }
}
