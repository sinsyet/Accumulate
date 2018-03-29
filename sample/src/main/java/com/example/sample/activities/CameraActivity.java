package com.example.sample.activities;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.SeekBar;

import com.example.appbase.engine.ThreadPool;
import com.example.apphelper.AppHelper;
import com.example.sample.R;

import java.io.IOException;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "CameraActivity";
    private SeekBar mSbZoom;
    private TextureView mTrv;
    private SurfaceTexture mSurface;
    private Camera mCamera;
    private int mCurZoomProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findView();
    }

    void findView(){
        mSbZoom = findViewById(R.id.camera_sb_zoom);
        mTrv = findViewById(R.id.camera_trv);

        mTrv.setSurfaceTextureListener(this);
        mSbZoom.setOnSeekBarChangeListener(this);
    }

    private Runnable mOpenCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(0);
            camera.setDisplayOrientation(
                    AppHelper.getCameraDisplayRotation(CameraActivity.this,0));
            Camera.Parameters params = camera.getParameters();
            mCamera = camera;
            try {
                mCamera.setPreviewTexture(mSurface);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG,"open camera fail");
            }
        }
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = surface;
        ThreadPool.run(mOpenCamera);
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurZoomProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Camera.Parameters params = mCamera.getParameters();
        int maxZoom = params.getMaxZoom();
        params.setZoom((int) (maxZoom * (mCurZoomProgress  * 1.0f/ 100)));
        mCamera.setParameters(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            }catch (Exception e){}

            try{
                mCamera.release();
            }catch (Exception e){}

            mCamera = null;
        }
    }
}
