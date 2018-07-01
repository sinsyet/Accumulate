package com.example.sample.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.apphelper.AppHelper;
import com.example.sample.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraFragment extends Fragment implements
        View.OnClickListener,
        TextureView.SurfaceTextureListener,
        Camera.PictureCallback, Camera.PreviewCallback {

    private ImageView mIv;
    private int mCameraId;
    private Camera mCamera;
    private SurfaceTexture mSurface;
    private int mMaxSupportCamera;
    private Bitmap bitmap;
    private TextView mTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, null);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        findView(view);
        reigsterCallback(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppHelper.run(mReleaseCamera);
    }

    private void findView(View view) {

        mIv = view.findViewById(R.id.camera_iv_photo);
        mTv = view.findViewById(R.id.camera_tv_desc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn_capture: captureCamera(); break;
            case R.id.camera_btn_switch:  switchCamera();  break;
        }
    }

    private void captureCamera() {

        if(mCamera == null) return;

        // mCamera.takePicture(null,null,null, this);
        mCamera.setPreviewCallback(this);
    }

    private void switchCamera() {
       AppHelper.run(mSwitchCamera);
    }

    private void init(){
        mCameraId = 0;
        mMaxSupportCamera = Camera.getNumberOfCameras();
    }

    private void reigsterCallback(View view){
        view.findViewById(R.id.camera_btn_capture).setOnClickListener(this);
        view.findViewById(R.id.camera_btn_switch).setOnClickListener(this);

        ((TextureView) view.findViewById(R.id.camera_displayer)).setSurfaceTextureListener(this);
    }

    private Camera.Size size;
    private Runnable mOpenCamera = new Runnable() {


        @Override
        public void run() {
            Camera camera = Camera.open(mCameraId);
            camera.setDisplayOrientation(
                    AppHelper.getCameraDisplayRotation(getContext(),0));
            size = camera.getParameters().getPreviewSize();
            mCamera = camera;

            try {
                mCamera.setPreviewTexture(mSurface);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }
    };

    private Runnable mSwitchCamera = new Runnable() {
        @Override
        public void run() {
            mReleaseCamera.run();
            mCameraId = (mCameraId == mMaxSupportCamera - 1) ? 0 : ++ mCameraId ;
            mOpenCamera.run();
        }
    };

    private Runnable mReleaseCamera = new Runnable() {
        @Override
        public void run() {
            if (mCamera == null) {
                return;
            }
            Camera c = mCamera;
            mCamera = null;

            try {
                c.stopPreview();
            }catch (Exception e){};

            try {
                c.release();
            }catch (Exception e){};
        }
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = surface;

        AppHelper.run(mOpenCamera);
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
    public void onPictureTaken(byte[] data, Camera camera) {

    }

    private int MAX_FACE_NUM = 10;
    private Runnable findFace = new Runnable() {
        @Override
        public void run() {
            if(bitmap == null) return;

            FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_NUM);
            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_NUM];
            int count = faceDetector.findFaces(bitmap, faces);
            Log.e(TAG, "run: "+count);

        }
    };

    private static final String TAG = "CameraFragment";

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mCamera != null) mCamera.setPreviewCallback(null);

        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);//这个参数不要乱动，乱动会转错误
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);//先按原尺寸转成jpeg的流，后面再压缩一次。这个也不要乱动参数
        byte[] finalface = stream.toByteArray();//此时的尺寸并不小，但我尝试在上面代码直接转小会错位，考虑到要经过网络，所以后面会再转换一次，但转换速度非常快，基本上不会影响性能
        bitmap = BitmapFactory.decodeByteArray(finalface, 0, finalface.length);
        if(bitmap == null){
            Log.e(TAG, "onPreviewFrame: bitmap is null");
        }
        mIv.setImageBitmap(bitmap);

        AppHelper.run(findFace);
    }
}
