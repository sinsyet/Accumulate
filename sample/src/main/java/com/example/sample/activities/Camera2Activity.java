package com.example.sample.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.example.sample.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static android.hardware.camera2.CameraDevice.StateCallback;
import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Camera2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "Camera2Activity";
    private TextureView mTrv;
    private SurfaceTexture mSurface;
    private CaptureRequest captureRequest;
    private String[] cameraIdList;
    private CameraManager cameraManager;
    private ImageReader mImageReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        findView();
    }

    private void initImageReader(String cameraId) throws CameraAccessException {
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        // 获取分辨率
        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            return;
        }
        Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
            @Override
            public int compare(Size o1, Size o2) {

                return o1.getHeight() * o1.getWidth() - o2.getHeight() * o2.getWidth();
            }
        });

        mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                ImageFormat.JPEG, /*maxImages*/2);

        mImageReader.setOnImageAvailableListener(
                mOnImageAvailableListener, getCameraHandler());
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            getCameraHandler().post(
                    new ImageSaver(
                            reader.acquireNextImage(),
                            new File(getExternalFilesDir(null),
                                    System.currentTimeMillis()+"_pic.jpg")));
        }
    };


    private static class ImageSaver implements Runnable{

        private final Image image;
        private final File f;

        ImageSaver(Image image, File f){
            this.image = image;
            this.f = f;
        }
        @Override
        public void run() {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            OutputStream os = null;
            try {
                os = new FileOutputStream(f);
                os.write(buf);
                os.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
    private int mCameraIndex;

    void initCameraManager() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager == null) {
            Log.e(TAG, "initCamera: cameraManager  is null");
            return;
        }

        try {
            // 对应的是0和1
            cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length <= 0) {
                Log.e(TAG, "initCamera: cameraIdList is empty");
                return;
            }
            for (int i = 0; i < cameraIdList.length; i++) {
                Log.e(TAG, "initCamera: " + cameraIdList[i]);
            }

            mCameraIndex = 0;
            initCamera();
        } catch (CameraAccessException e) {

        }
    }

    private void initCamera() throws CameraAccessException {

        // 获取相机特性
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[mCameraIndex]);

        // 闪光
        Boolean available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        mFlashSupported = available == null ? false : available;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        initImageReader(cameraIdList[mCameraIndex]);
        cameraManager.openCamera(cameraIdList[mCameraIndex], mStateCallback, getCameraHandler());
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
            list.add(mImageReader.getSurface());
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
        initCameraManager();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 释放相机
     */
    private void releaseCamera(){

    }
}
