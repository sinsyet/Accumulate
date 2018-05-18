package com.example.facedemo.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.facedemo.R;
import com.example.facedemo.msc.FaceR;
import com.example.facedemo.view.FaceDrawerView;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechUtility;

import java.util.List;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView mTrv;
    private FaceDrawerView mSfvDrawer;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private SurfaceTexture mSurface;
    private int mCurCameraId;
    private FaceDetector mFaceDetector;
    private static final String TAG = "FaceActivity";

    private int mHeight = 480;
    private int mWidth = 640;
    private SurfaceHolder surfaceHolder;
    private SurfaceHolder holder;

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
        mSfvDrawer = findViewById(R.id.face_sfv_drawer);


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

        initPreviewViewLayoutParams();
    }

    private DisplayMetrics mMetrics;

    DisplayMetrics getScreenMetrics() {
        if (mMetrics == null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mMetrics = metrics;
        }
        return mMetrics;

    }

    private void initPreviewViewLayoutParams() {

        DisplayMetrics metrics = getScreenMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        //  float max_rate = Math.max(height * 1.0f / heightPixels, width * 1.0f / widthPixels);
        float rate = mHeight * 1.0f / mWidth;  // -> rate = height / width, width

        FrameLayout.LayoutParams params = null;
        if (isHorizontail()) {
            if (rate * widthPixels > heightPixels) {
                params = new FrameLayout.LayoutParams(
                        (int) (heightPixels * 1.0f / rate),
                        (int) (heightPixels));
            } else {
                params = new FrameLayout.LayoutParams(
                        (int) (widthPixels),
                        (int) (widthPixels * rate));
            }
        }else{
            int height = (int) (widthPixels * mWidth / (float) mHeight);
            params = new FrameLayout.LayoutParams(widthPixels, height);
        }
        mPreviewHeight = params.height;
        mPreviewWidth = params.width;
        this.mRate = mPreviewWidth * 1.0f / mWidth;
        Log.e(TAG, "initPreviewViewLayoutParams:height "+params.height+" // width "+params.width+" // "+this.mRate);
        params.gravity = Gravity.CENTER;
        mTrv.setLayoutParams(params);
        mSfvDrawer.setLayoutParams(params);
        // mTrv.setSurfaceTextureListener(mSurfaceTextureListener);
        mTrv.getHolder().addCallback(this);

    }

    private int mPreviewHeight;
    private int mPreviewWidth;


    private int numberOfCameras;
    private Camera mCamera;
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

            parameters.setPreviewSize(mWidth, mHeight);
            camera.setParameters(parameters);
            camera.startPreview();
            try {

                // camera.setPreviewTexture(mSurface);
                camera.setPreviewDisplay(surfaceHolder);
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

        if (mCurCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
            mDetectorDirection = (4 - mDetectorDirection) % 4;
        }
        Log.e(TAG, "setDirectionValueByDegree: " + degree + " // " + mDetectorDirection);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean isHorizontail() {
        DisplayMetrics metrics = getScreenMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        return widthPixels > heightPixels;
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

    private float mRate;
    // 显示图像的控件宽高
    private Runnable mFaceDecoder = new Runnable() {

        @Override
        public void run() {
            mFaceFlag = true;
            while (mFaceFlag) {
                if (isBufferEmpty) continue;
                String result = mFaceDetector.trackNV21(nv21, mWidth, mHeight, 1, mDetectorDirection);

                FaceR r = FaceR.decode(result,mRate,mPreviewWidth,mPreviewHeight, mCurCameraId == 1 ? Camera.CameraInfo.CAMERA_FACING_FRONT: Camera.CameraInfo.CAMERA_FACING_BACK);
                /*Log.e(TAG, "runresult: " + result + " // " + (r != null ? r.getCount() : "null"));
                if (r != null) {
                    drawFace(r);
                }*/
                mSfvDrawer.postRefreshFace(r);
                isBufferEmpty = true;
                Log.e(TAG, "run: " + result);
            }
        }
    };

    void drawFace(FaceR r) {
        if (r.getCount() < 1) {
            return;
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) return;

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        List<FaceR.Face> faces = r.getFaces();
        int size = faces.size();
        Paint paint = getPaint();
        FaceR.Face face = null;
        Path position = null;
        for (int i = 0; i < size; i++) {
            face = faces.get(i);
            position = face.getPosition();

            canvas.drawPath(position, paint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private Paint mPaint;

    private Paint getPaint() {
        if (mPaint == null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(AppHelper.dp2px(getApplicationContext(), 2));
            paint.setStyle(Paint.Style.STROKE);
            mPaint = paint;
        }

        return mPaint;
    }

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
            mCurCameraId++;
            if (mCurCameraId >= numberOfCameras) mCurCameraId = 0;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        AppHelper.run(mDisplayCamera);
        AppHelper.run(mFaceDecoder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

