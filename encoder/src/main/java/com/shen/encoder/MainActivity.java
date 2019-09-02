package com.shen.encoder;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shen.media.CamerManager;
import com.shen.media.CamerProxy;
import com.shen.media.EncoderManager;
import com.shen.media.EncoderProxy;
import com.shen.media.MediaBuilder;
import com.shen.media.MediaConfiger;
import com.shen.tools.ToolDebug;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaConfiger mConfiger;
    private CamerProxy mCamer;
    private EncoderProxy mEncoder;
    private SendThread mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView view=findViewById(R.id.view);
        view.getHolder().addCallback(this);
        mSend=new SendThread();
//        EncoderManager.getInstance().showEncoderList();
//        CamerManager.getInstance().showCamerList();
        ToolDebug.printLogE(this,"onCreate");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMedia();
        ToolDebug.printLogE(this,"onDestroy");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        ToolDebug.printLogE(this,"surfaceChanged-format"+format+" width="+width+" height="+height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ToolDebug.printLogE(this,"surfaceDestroyed");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ToolDebug.printLogE(this,"surfaceCreated");
        startMedia(holder);
    }

    private void startMedia(SurfaceHolder holder){
        if(mConfiger==null) {
            mConfiger = MediaBuilder.getLocalDefaultConfiger();
            mConfiger.createEncoderPoll();

            ToolDebug.printLogE(this, "startMedia-mConfiger=" + mConfiger.toString());

            CamerManager.getInstance().openCamer(mConfiger, holder, new CamerProxy.CallBack() {
                @Override
                public void onOpened(CamerProxy camer) {
                    mCamer = camer;
                }

                @Override
                public void onDisconnected() {
                    ToolDebug.printLogE(this, "onDisconnected");
                }

                @Override
                public void onErrorOpenCamera(String msg) {
                    ToolDebug.printLogE(this, "onErrorOpenCamera-msg=" + msg);
                }

                @Override
                public void onErrorCreateSession(String msg) {
                    ToolDebug.printLogE(this, "onErrorCreateSession-msg=" + msg);
                }

                @Override
                public void onErrorRequest(String msg) {
                    ToolDebug.printLogE(this, "onErrorRequest-msg=" + msg);
                }

                @Override
                public void onPreviewFrame(byte[][] yuv) {
                    ToolDebug.printLogD(this, "onPreviewFrame-yuv[0].size(" + yuv[0].length + " yuv[1].size(" + yuv[1].length + " yuv[2].size(" + yuv[2].length + ")");
                    mConfiger.getEncoderPoll().offer(yuv);
                }
            });
            mEncoder = EncoderManager.getInstance().startEncode(mConfiger, new EncoderProxy.CallBack() {
                @Override
                public void onOutFrame(byte[] data) {
                    ToolDebug.printLogD(this, "onOutFrame-data.size(" + data.length + ")" + " data=" + Arrays.toString(data));
                    mSend.sendConfiger(mConfiger);
                    mSend.sendFrameData(data);
                }
            });
        }
    }
    private void stopMedia(){
        if(mCamer!=null) {
            mCamer.close();
            mCamer=null;
        }
        if(mEncoder!=null) {
            mEncoder.stop();
            mEncoder=null;
        }
    }
}
