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
import java.util.List;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private static final String TAG = "CameraActivity";
    private SeekBar mSbZoom;
    private TextureView mTrv;
    private SurfaceTexture mSurface;
    private Camera mCamera;
    private int mCurZoomProgress;
    private List<String> supportedSceneModes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findView();
    }

    void findView(){
        mSbZoom = findViewById(R.id.camera_sb_zoom);
        mTrv = findViewById(R.id.camera_trv);
        findViewById(R.id.camera_btn_scene).setOnClickListener(this);

        mTrv.setSurfaceTextureListener(this);
        mSbZoom.setOnSeekBarChangeListener(this);
    }

    private Runnable mOpenCamera = new Runnable() {
        @Override
        public void run() {
            Camera camera = Camera.open(1);
            camera.setDisplayOrientation(
                    AppHelper.getCameraDisplayRotation(CameraActivity.this,0));
            Camera.Parameters params = camera.getParameters();
            supportedSceneModes = params.getSupportedSceneModes();
            Log.e(TAG, "run: supportedSceneModes - "+supportedSceneModes.size());
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
        /*int maxZoom = params.getMaxZoom();
        params.setZoom((int) (maxZoom * (mCurZoomProgress  * 1.0f/ 100)));*/
        int maxExposureCompensation = params.getMaxExposureCompensation();
        params.setExposureCompensation(
                (
                        (int)
                                ((maxExposureCompensation * 2) * (mCurZoomProgress  * 1.0f/ 100))
                                - maxExposureCompensation));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn_scene: setScene(); break;
        }
    }

    private int index;
    private void setScene() {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters params = mCamera.getParameters();
        if(index >= supportedSceneModes.size()) index = 0;
        String s = supportedSceneModes.get(index);
        params.setSceneMode(s);
        mCamera.setParameters(params);
        Log.e(TAG, "setScene: "+s);
        index ++;
    }
}
