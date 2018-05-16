package com.example.facedemo.activity;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.facedemo.R;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechUtility;

import java.util.List;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextureView mTrv;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private SurfaceTexture mSurface;
    private int mCurCameraId;
    private FaceDetector mFaceDetector;
    private static final String TAG = "FaceActivity";

    private int mHeight = 480;
    private int mWidth  = 640;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        // 注册
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        mFaceDetector = FaceDetector.createDetector(getApplicationContext(), null);
        findView();
        numberOfCameras = Camera.getNumberOfCameras();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFaceDetector.destroy();
        AppHelper.run(mReleaseCurCamera);
    }

    void findView() {
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

        mTrv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTrv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                initPreviewViewLayoutParams();
            }
        });
    }

    private void initPreviewViewLayoutParams() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;


       //  float max_rate = Math.max(height * 1.0f / heightPixels, width * 1.0f / widthPixels);
        float rate = mHeight*1.0f / mWidth;  // -> rate = height / width, width
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int)(widthPixels),
                (int)(widthPixels * rate));
        mTrv.setLayoutParams(params);
        mTrv.setSurfaceTextureListener(mSurfaceTextureListener);
    }


    private int numberOfCameras;
    private Camera mCamera;
    private Runnable mDisplayCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(mCurCameraId);
            int degrees = AppHelper.getCameraDisplayRotation(
                    getApplicationContext(),
                    mCurCameraId);
            camera.setDisplayOrientation(degrees);
            setDirectionValueByDegree(degrees);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPreviewFormat(ImageFormat.NV21);

            List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size tmp = null;
            for (Camera.Size size:sizes){
                Log.e(TAG, "run: support size: "+size.width+" // "+size.height);
                if(tmp == null) tmp = size;
                else if(tmp.height < size.height) tmp = size;
            }
            if(tmp != null){
                Log.e(TAG, "run: max size: "+tmp.height+" // "+tmp.width);
                mWidth = tmp.width;
                mHeight = tmp.height;
            }
            parameters.setPreviewSize(mWidth,mHeight);
            camera.setParameters(parameters);
            camera.startPreview();
            try {
                camera.setPreviewTexture(mSurface);
                camera.setPreviewCallback(mPreviewCallback);
                mCamera = camera;

            } catch (Exception e) {
                ToastUtil.show(getApplicationContext(), "Open Camera Fail");
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
        Log.e(TAG, "setDirectionValueByDegree: "+degree+" // "+mDetectorDirection);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private byte[] nv21;
    private volatile boolean isBufferEmpty;
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
//            long start = System.currentTimeMillis();

            if (!isBufferEmpty) return;
            if (nv21 == null || nv21.length < data.length) {
                nv21 = new byte[data.length];
                // Log.e(TAG, "onPreviewFrame: " + Thread.currentThread().getName() + (System.currentTimeMillis() - start));
            }
            System.arraycopy(data, 0, nv21, 0, data.length);
            long end = System.currentTimeMillis();
            isBufferEmpty = false;
            // Log.e(TAG, "onPreviewFrame: " + (end - start));
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
            while (mFaceFlag) {
                if (isBufferEmpty) continue;
                String result = mFaceDetector.trackNV21(nv21, mWidth, mHeight, 1, mDetectorDirection);
                isBufferEmpty = true;
                Log.e(TAG, "run: " + result);
            }
        }
    };

    private Runnable mReleaseCurCamera = new Runnable() {
        @Override
        public void run() {
            if (mCamera == null) {
                return;
            }

            mCamera.setPreviewCallback(null);
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCamera = null;
        }
    };

    private Runnable mReleaseCurCameraAnsSwitchCamera = new Runnable() {
        @Override
        public void run() {
            mReleaseCurCamera.run();
            mCurCameraId ++;
            if(mCurCameraId >= numberOfCameras) mCurCameraId = 0;
            mDisplayCamera.run();
        }
    };

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.face_btn_switch) {
            switchCamera();
        }
    }

    private void switchCamera() {
        AppHelper.run(mReleaseCurCameraAnsSwitchCamera);
    }
}

