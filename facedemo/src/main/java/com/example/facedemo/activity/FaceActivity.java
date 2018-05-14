package com.example.facedemo.activity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.facedemo.R;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextureView mTrv;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private SurfaceTexture mSurface;
    private int mDefaultCameraId;
    private FaceDetector mFaceDetector;
    private static final String TAG = "FaceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        // 注册
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        mFaceDetector = FaceDetector.createDetector(getApplicationContext(), null);
        findView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFaceDetector.destroy();
    }

    void findView(){
        findViewById(R.id.face_btn_switch).setOnClickListener(this);
        mTrv = findViewById(R.id.face_trv);
        mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurface = surface;
                AppHelper.run(mDisplayCamera);
                AppHelper.run(mFaceDecoder);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
        mTrv.setSurfaceTextureListener(mSurfaceTextureListener);
        mTrv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTrv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPreviewHeight = mTrv.getHeight();
                mPreviewWidth = mTrv.getWidth();
                Log.e(TAG, "onResume: "+mPreviewWidth+" // "+mPreviewHeight);
            }
        });
    }


    private int numberOfCameras;
    private Camera mCamera;
    private Runnable mDisplayCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(mDefaultCameraId);
            int degrees = AppHelper.getCameraDisplayRotation(
                    getApplicationContext(),
                    mDefaultCameraId);
            camera.setDisplayOrientation(degrees);
            setDirectionValueByDegree(degrees);

            camera.startPreview();
            try {
                camera.setPreviewTexture(mSurface);
                camera.setPreviewCallback(mPreviewCallback);
                mCamera = camera;

            } catch (Exception e) {
                ToastUtil.show(getApplicationContext(),"Open Camera Fail");
            }

        }
    };

    private int mDetectorDirection;
    private void setDirectionValueByDegree(int degree) {
        switch (degree) {
            case 0:
                mDetectorDirection = 0;
                break;
            case 90:
                mDetectorDirection = 1;
                break;
            case 180:
                mDetectorDirection = 2;
                break;
            case 270:
                mDetectorDirection = 3;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private byte[] nv21;
    private boolean isBufferEmpty;
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if(!isBufferEmpty) return;
            // String result = mFaceDetector.trackNV21(buffer, mPreviewWidth, mPreviewHeight, 1, mDetectorDirection);
            if(nv21 == null || nv21.length < data.length){
                nv21 = new byte[data.length];
                Log.e(TAG, "onPreviewFrame: "+Thread.currentThread().getName());
            }
            long start = System.currentTimeMillis();
            System.arraycopy(data,0,nv21,0,data.length);
            long end = System.currentTimeMillis();
            Log.e(TAG, "onPreviewFrame: "+(end-start));
            isBufferEmpty = false;
        }
    };

    private boolean mFaceFlag;

    // 显示图像的控件宽高
    private int mPreviewWidth;
    public int mPreviewHeight;
    private Runnable mFaceDecoder = new Runnable() {

        @Override
        public void run() {
            mFaceFlag = true;
            while (mFaceFlag){
                if(isBufferEmpty) continue;
                String result = mFaceDetector.trackNV21(nv21, mPreviewWidth, mPreviewHeight, 1, mDetectorDirection);
                isBufferEmpty = true;
                Log.e(TAG, "run: "+result);
            }
        }
    };

    @Override
    public void onClick(View v) {

    }
}