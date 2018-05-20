package com.example.sample.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.example.sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.hardware.camera2.CameraDevice.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Camera2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "Camera2Activity";
    private TextureView mTrv;
    private SurfaceTexture mSurface;
    private CaptureRequest captureRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        findView();
    }

    private int mCameraIndex;

    void initCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager == null) {
            Log.e(TAG, "initCamera: cameraManager  is null");
            return;
        }
        try {
            // 对应的是0和1
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length <= 0) {
                Log.e(TAG, "initCamera: cameraIdList is empty");
                return;
            }
            for (int i = 0; i < cameraIdList.length; i++) {
                Log.e(TAG, "initCamera: " + cameraIdList[i]);
            }

            mCameraIndex = 0;
            // 获取相机特性
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[mCameraIndex]);

            // Key
            List<CameraCharacteristics.Key<?>> keys = cameraCharacteristics.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                CameraCharacteristics.Key<?> key = keys.get(i);
                String name = key.getName();
                Log.e(TAG, "initCamera2: " + name);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            // 闪光
            Boolean available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            mFlashSupported = available == null ? false : available;

            cameraManager.openCamera(cameraIdList[mCameraIndex], mStateCallback, getCameraHandler());
        } catch (CameraAccessException e) {

        }
    }

    private Boolean mFlashSupported;
    private Handler mCameraHandler;
    private Handler getCameraHandler(){
        if(mCameraHandler == null){
            HandlerThread handlerThread = new HandlerThread("camera2Handler");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            mCameraHandler = new Handler(looper);
        }
        return mCameraHandler;
    }

    void findView(){
        mTrv = findViewById(R.id.camera2_trv);
        mTrv.setSurfaceTextureListener(this);
    }

    private Surface surface;
    private CaptureRequest.Builder captureRequestBuilder;
    private StateCallback mStateCallback = new StateCallback() {



        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            ArrayList<Surface> list = new ArrayList<>();
            // 展示的控件
            surface = new Surface(mSurface);
            /*
             *
             *  SurfaceView surfaceView = new SurfaceView(getApplicationContext());
             *  Surface surface = surfaceView.getHolder().getSurface();
             *
             *  可以获取到surface
             */

            list.add(Camera2Activity.this.surface);
            Log.e(TAG, "onOpened: ");
            try {
                captureRequestBuilder = camera.createCaptureRequest(TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(Camera2Activity.this.surface);
                camera.createCaptureSession(list,ccsStateCallback,getCameraHandler());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.e(TAG, "onDisconnected: ");
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e(TAG, "onError: ");
        }
    };

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            // 闪光灯
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    };
    private CameraCaptureSession.StateCallback ccsStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "onConfigured: ");
            // 设置自动连续聚焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureRequestBuilder);
            captureRequest = captureRequestBuilder.build();

            try {
                // 不管做什么操作, 都是这个;
                session.setRepeatingRequest(captureRequest,mCaptureCallback,getCameraHandler());
            } catch (CameraAccessException e) {

            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "onConfigureFailed: ");
        }
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = surface;
        initCamera();
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
}
