package com.example.facedemo.activity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

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
    }


    private int numberOfCameras;
    private Camera mCamera;
    private Runnable mDisplayCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(mDefaultCameraId);
            camera.setDisplayOrientation(
                    AppHelper.getCameraDisplayRotation(
                            getApplicationContext(),
                            mDefaultCameraId));

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

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

        }
    };

    @Override
    public void onClick(View v) {

    }
}
d