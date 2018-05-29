package com.example.facedemo.activity;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.facedemo.R;
import com.iflytek.cloud.FaceDetector;

import java.util.List;

public class Face2Activity extends AppCompatActivity implements View.OnClickListener, TextureView.SurfaceTextureListener {

    private SurfaceView mSfv;
    private TextureView mTrv;
    private SurfaceTexture mSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppHelper.setFullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face2);

        initClickEvent();

        findView();

        init();
    }

    void findView(){
        findViewById(R.id.face2_btn_back).setOnClickListener(this);
        findViewById(R.id.face2_btn_switchCamera).setOnClickListener(this);

        mSfv = findViewById(R.id.face2_sfv);
        mTrv = findViewById(R.id.face2_trv);

        mSfv.setZOrderOnTop(true);

        mSfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mTrv.setSurfaceTextureListener(this);

    }

    private int mCurCameraId;
    private FaceDetector mFaceDetector;
    private int numberOfCameras;
    private Camera mCamera;
    private void init(){
        mFaceDetector = FaceDetector.createDetector(getApplicationContext(), null);
        numberOfCameras = Camera.getNumberOfCameras();

    }
    private boolean mFaceFlag;

    private float mRate;
    // 显示图像的控件宽高
    private Runnable mFaceDecoder = new Runnable() {

        @Override
        public void run() {
            mFaceFlag = true;
            while (mFaceFlag) {
                if (isBufferEmpty) continue;
                String result = mFaceDetector.trackNV21(nv21, mWidth, mHeight, 1, mDetectorDirection);

                // FaceR r = FaceR.decode(result,mRate,mPreviewWidth,mPreviewHeight, mCurCameraId == 1 ? Camera.CameraInfo.CAMERA_FACING_FRONT: Camera.CameraInfo.CAMERA_FACING_BACK);
                /*Log.e(TAG, "runresult: " + result + " // " + (r != null ? r.getCount() : "null"));
                if (r != null) {
                    drawFace(r);
                }*/
                // mSfvDrawer.postRefreshFace(r);
                isBufferEmpty = true;
                Log.e(TAG, "run: " + result);
            }
        }
    };

    private static final String TAG = "Face2Activity";
    private int mHeight = 480;
    private int mWidth = 640;
/*    private int mHeight = 600;
    private int mWidth = 800;*/
    private Runnable mDisplayCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(mCurCameraId);
            int degrees = AppHelper.getCameraDisplayRotation(
                    getApplicationContext(),
                    mCurCameraId);
            /*if (isHorizontail()) {
                degrees += 90;
            }*/
            camera.setDisplayOrientation(degrees);
            setDirectionValueByDegree(degrees);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPreviewFormat(ImageFormat.NV21);

            // 设置聚焦模式
            List<String> focusModes = parameters.getSupportedFocusModes();
            for (String mode : focusModes) {
                Log.e(TAG, "run:focusModes: " + mode);
            }
            if (focusModes.contains("continuous-video")) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            /*Camera.Size size = AppHelper.getMaxCameraSupportSize(parameters);
            mWidth = size.width;
            mHeight = size.height;*/
            Log.e(TAG, "run-ygx : "+mWidth+" // "+mHeight);
            parameters.setPreviewSize(mWidth, mHeight);
            camera.setParameters(parameters);
            camera.startPreview();
            try {

                // camera.setPreviewTexture(mSurface);
                camera.setPreviewTexture(mSurface);
                camera.setPreviewCallback(mPreviewCallback);
                mCamera = camera;

            } catch (Exception e) {
                ToastUtil.show(getApplicationContext(), "Open Camera Fail");
            }

        }
    };

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

        if (mCurCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
            mDetectorDirection = (4 - mDetectorDirection) % 4;
        }
    }


    @Override
    public void onClick(View v) {

        Runnable r = mClickRunnable.get(v.getId());

        if(r != null) r.run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppHelper.run(mReleaseCurCamera);
    }

    private SparseArray<Runnable> mClickRunnable = new SparseArray<>();

    private void initClickEvent(){
        mClickRunnable.put(R.id.face2_btn_back, this::finish);
        mClickRunnable.put(R.id.face2_btn_switchCamera, this::switchCamera);
    }

    private Runnable mReleaseCurCameraAnsSwitchCamera = new Runnable() {
        @Override
        public void run() {
            mReleaseCurCamera.run();
            mCurCameraId++;
            if (mCurCameraId >= numberOfCameras) mCurCameraId = 0;
            mDisplayCamera.run();
            isSwitchCamera = false;
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

    private boolean isSwitchCamera;
    void switchCamera(){
        if(isSwitchCamera) return;

        isSwitchCamera = true;
        AppHelper.run(mReleaseCurCameraAnsSwitchCamera);
    }

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
}
