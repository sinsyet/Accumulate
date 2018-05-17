package com.example.facedemo.fragment;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.example.apphelper.ToastUtil;
import com.example.facedemo.R;
import com.example.facedemo.engine.CameraInterface;
import com.example.facedemo.engine.HttpUtils;
import com.example.facedemo.engine.StaticArguments;
import com.example.facedemo.engine.TakePictureListener;
import com.example.facedemo.view.CameraSurfaceView;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.Accelerometer;

import java.util.List;

import Decoder.BASE64Encoder;


public class FaceFragment extends Fragment implements HttpUtils.HttpListener {
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH  = 640;
    private int PREVIEW_HEIGHT = 480;
    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    private byte[] buffer;
    // FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
    private FaceDetector mFaceDetector;
    private Canvas mCanvas;
    // 加速度感应器，用于获取手机的朝向
    private Accelerometer mAcceler;
    private boolean mStopTrack;
    // 是否显示聚焦点 0不显示 1显示
    private int isAlign = 1;
    protected int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    private boolean isOpenMouth = false;
    private boolean isShakeHead = false;
    private TextView tv_notice;
    private ImageView img_animation;
    private CameraSurfaceView mPreviewSurface = null;
    private SurfaceView mFaceSurface;
    private boolean isFace = false;
    // 进行人脸检测及活体检测的子线程
    private Thread mThread;
    View mFaceView;
    /**
     * 摇头拍照
     */
    int iFlag = 0;
    /**
     * 张嘴拍照
     */
    int oFlag = 0;
    int countHead = 0;// 向左向右摇头计数
    /**
     * 拍照的次数
     */
    int takePictureFlag = 0;
    BASE64Encoder mBASE64Encoder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mFaceView == null) {
            SpeechUtility.createUtility(getContext(), "appid=" + getString(R.string.app_id));
            mFaceView = inflater.inflate(R.layout.fragment_face, null);
            mPreviewSurface = (CameraSurfaceView) mFaceView
                    .findViewById(R.id.surface_fragment_face_preview);
            mFaceSurface = (SurfaceView) mFaceView
                    .findViewById(R.id.surface_fragment_face_face);
            //mFaceSurface置于窗口的最顶层
            mFaceSurface.setZOrderOnTop(true);
            mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            //设置预览界面的尺寸
            setSurfaceSize();
            tv_notice = (TextView) mFaceView
                    .findViewById(R.id.tv_fragment_face_notice);

            img_animation = (ImageView) mFaceView
                    .findViewById(R.id.img_fragment_face_anim);
            img_animation.setImageResource(R.drawable.img_face_open_mouth);
            AnimationDrawable animationDrawable = (AnimationDrawable) img_animation
                    .getDrawable();
            animationDrawable.start();
        }
        ViewGroup parent = (ViewGroup) mFaceView.getParent();
        if (parent != null) {
            parent.removeView(mFaceView);
        }
        return mFaceView;
    }

    private void setSurfaceSize() {

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        LayoutParams params = new LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mPreviewSurface.setLayoutParams(params);
        mFaceSurface.setLayoutParams(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // List<Object> previewDate = CameraInterface.getInstance()
        // .getPreviewDate();
        // buffer = (byte[]) previewDate.get(0);
        // nv21 = (byte[]) previewDate.get(1);
        mAcceler = new Accelerometer(getActivity());
        mFaceDetector = FaceDetector.createDetector(getActivity(), null);
        mBASE64Encoder = new BASE64Encoder();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mAcceler) {
            mAcceler.start();
        }
        mStopTrack = false;
        setFaceDetection();
       /* CameraInterface.getInstance().setOnTakePictureListener(
                new TakePictureListener() {
                    @Override
                    public void onTakePictuerResult(Bitmap mBitmap) {
                        String bitMapStr = getImage(mBitmap);
                        Message msg = new Message();
                        msg.what = StaticArguments.FACE_HAS_TAKE_PPICTURE;
                        msg.obj = bitMapStr;
                        mHandler.sendMessage(msg);
                    }
                });*/
    }

    @Override
    public void onPause() {
        super.onPause();
        CameraInterface.getInstance().doDestroyCamera();
        if (null != mAcceler) {
            mAcceler.stop();
        }
        mStopTrack = true;
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        CameraInterface.getInstance().doDestroyCamera();
        mStopTrack = true;
        // 销毁对象
        mFaceDetector.destroy();

    }
    /**
     * 人脸检测
     */
    private void setFaceDetection() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Object> previewDate = CameraInterface.getInstance()
                        .getPreviewDate();
                buffer = (byte[]) previewDate.get(0);
                nv21 = (byte[]) previewDate.get(1);
                while (!mStopTrack) {
                    if (null == nv21) {
                        continue;
                    }
                    synchronized (nv21) {
                        System.arraycopy(nv21, 0, buffer, 0, nv21.length);
                    }
                    // 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
                    int direction = Accelerometer.getDirection();
                    boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
                    // 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
                    // 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
                    if (frontCamera) {
                        // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
                        direction = (4 - direction) % 4;
                    }
                    if (mFaceDetector == null) {
                        /**
                         * 离线视频流检测功能需要单独下载支持离线人脸的SDK 请开发者前往语音云官网下载对应SDK
                         */
                        ToastUtil.show(getActivity(), "本SDK不支持离线视频流检测");
                        break;
                    }

                    String result = mFaceDetector.trackNV21(buffer,
                            PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
                    Log.e(TAG, "run: "+result);
                    // FaceRect[] faces = ParseResult.parseResult(result);

                    // drawRect(frontCamera, faces);

                }
            }
            /**
             * 绘制人脸框，并进行活体检测
             *
             * @param frontCamera
             * @param faces
            private void drawRect(boolean frontCamera, FaceRect[] faces) {
                mCanvas = mFaceSurface.getHolder().lockCanvas();
                if (null == mCanvas) {
                    return;
                }
                Point mouth_lower_lip_bottom = FaceMouth.getInstance()
                        .getMouth_lower_lip_bottom();
                Point mouth_upper_lip_top = FaceMouth.getInstance()
                        .getMouth_upper_lip_top();

                Point left_eyebrow_middle = FaceEyebrow.getInstance()
                        .getLeft_eyebrow_middle();
                Point right_eyebrow_middle = FaceEyebrow.getInstance()
                        .getRight_eyebrow_middle();

                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

                mCanvas.setMatrix(mPreviewSurface.getMatrixInstance());

                if (faces == null || faces.length <= 0) {
                    mFaceSurface.getHolder().unlockCanvasAndPost(mCanvas);
                    isOpenMouth = false;
                    isFace = false;
                    return;
                }

                if (null != faces
                        && frontCamera == (CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
                    for (FaceRect face : faces) {
                        face.bound = FaceUtil.RotateDeg90(face.bound,
                                PREVIEW_WIDTH, PREVIEW_HEIGHT);
                        if (face.point != null) {
                            for (int i = 0; i < face.point.length; i++) {
                                face.point[i] = FaceUtil.RotateDeg90(
                                        face.point[i], PREVIEW_WIDTH,
                                        PREVIEW_HEIGHT);
                            }
                        }
                        FaceUtil.drawFaceRect(mCanvas, face, PREVIEW_WIDTH,
                                PREVIEW_HEIGHT, frontCamera, false);
                    }
                    isFace = true;
                } else {
                    // Log.d(TAG, "faces:0");
                }

                if (mouth_lower_lip_bottom != null
                        && mouth_upper_lip_top != null
                        && left_eyebrow_middle != null
                        && right_eyebrow_middle != null) {

                    int mouth_top_x = mouth_upper_lip_top.x;
                    int mouth_bottom_x = mouth_lower_lip_bottom.x;

                    int left_eyebrow_middle_y = left_eyebrow_middle.y;
                    int right_eyebrow_middle_y = right_eyebrow_middle.y;

                    int eyebrow_mlr_y = right_eyebrow_middle_y
                            - left_eyebrow_middle_y;

                    // 获取缩放矩形框的position
                    int bottom = FacePosition.getInstance().getBottom();
                    int top = FacePosition.getInstance().getTop();
                    // 缩放正方形的长宽
                    int rectLength = bottom - top;

                    int mouth_bt = mouth_top_x - mouth_bottom_x;

                    if (isFace) {
                        // 进行活体检测
                        if (rectLength >= 160 && rectLength < 180) {
                            // 判断张嘴后进行拍照
                            if (mouth_bt >= 40 && !isOpenMouth) {
                                isOpenMouth = true;
                                isShakeHead = true;
                                mHandler.sendEmptyMessageDelayed(
                                        StaticArguments.FACE_OPEN_MOUTH, 1000);
                            }
                            // 进行摇头判断
                            if (eyebrow_mlr_y < 79 && eyebrow_mlr_y >= 0
                                    && isShakeHead) {
                                isOpenMouth = true;

                                countHead++;
                                if (countHead % 2 == 0) {
                                    mHandler.sendEmptyMessageDelayed(
                                            StaticArguments.FACE_SHAKE_HEAD,
                                            3000);
                                }
                            }

                        } else if (rectLength >= 180 && rectLength < 200) {
                            // 判断张嘴后进行拍照
                            if (mouth_bt >= 45 && !isOpenMouth) {
                                isShakeHead = true;
                                isOpenMouth = true;
                                mHandler.sendEmptyMessageDelayed(
                                        StaticArguments.FACE_OPEN_MOUTH, 1000);
                            }
                            // 进行摇头判断
                            if (eyebrow_mlr_y < 89 && eyebrow_mlr_y >= 0
                                    && isShakeHead) {
                                isOpenMouth = true;

                                countHead++;
                                if (countHead % 2 == 0) {
                                    mHandler.sendEmptyMessageDelayed(
                                            StaticArguments.FACE_SHAKE_HEAD,
                                            1000);
                                }
                            }
                        } else if (rectLength >= 200 && rectLength < 340) {
                            // 判断张嘴后进行拍照
                            if (mouth_bt >= 50 && !isOpenMouth) {
                                isShakeHead = true;
                                isOpenMouth = true;
                                mHandler.sendEmptyMessageDelayed(
                                        StaticArguments.FACE_OPEN_MOUTH, 500);
                            }
                            // 进行摇头判断
                            if (eyebrow_mlr_y < 95 && eyebrow_mlr_y >= 0
                                    && isShakeHead) {
                                isOpenMouth = true;

                                countHead++;
                                if (countHead % 2 == 0) {
                                    mHandler.sendEmptyMessageDelayed(
                                            StaticArguments.FACE_SHAKE_HEAD,
                                            1000);
                                }
                            }
                        } else if (rectLength < 160) {// 检测脸部离手机太远
                            mHandler.sendEmptyMessage(StaticArguments.FACE_LENGTH_LONG);
                        } else if (rectLength >= 340 && rectLength >= 0) {// 检测脸部离手机太近
                            mHandler.sendEmptyMessage(StaticArguments.FACE_LENGTH_NEAR);
                        }
                    }
                }
                mFaceSurface.getHolder().unlockCanvasAndPost(mCanvas);
            }*/
        });
        mThread.start();
    }

    private static final String TAG = "FaceFragment";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*switch (msg.what) {
                case StaticArguments.FACE_HAS_TAKE_PPICTURE:
                    if (takePictureFlag == 0) {
                        takePictureFlag++;
                        ShowToast.showToast(getActivity(), "将张嘴拍照上传到指定的路径");
                        //上传人脸识别的照片
                        upLoadFace((String) msg.obj, 21);
                    } else if (takePictureFlag == 1) {
                        ShowToast.showToast(getActivity(), "将摇头拍照上传到指定的路径");
                        upLoadFace((String) msg.obj, 22);
                    }
                    break;

                case StaticArguments.FACE_OPEN_MOUTH:
                    if (oFlag == 0) {
                        takePicture();
                        oFlag++;
                        img_animation.setImageResource(R.drawable.img_face_head);
                        AnimationDrawable animationDrawable = (AnimationDrawable) img_animation
                                .getDrawable();
                        animationDrawable.start();
                        tv_notice.setText("请向左向右转头...");
                    }
                    break;

                case StaticArguments.FACE_LENGTH_NEAR:
                    ShowToast.showToast(getActivity(), "脸部不能离手机太近");
                    break;

                case StaticArguments.FACE_SHAKE_HEAD:
                    if (iFlag == 0) {
                        iFlag++;
                        mStopTrack = true;// 拍照成功，但多次运行后会崩溃拍照失败
                        takePicture();
                    }
                    break;
                case StaticArguments.FACE_LENGTH_LONG:
                    ShowToast.showToast(getActivity(), "脸部离的太远了");
                    break;
                default:
                    break;
            }*/
        }
    };

    /**
     * 拍照
     */
    private void takePicture() {
        // isOpenMouth = true;
        CameraInterface.getInstance().doTakePicture();
    }

    public void upLoadFace(String file_image_String, int flag) {
        if (flag == 22) {
            Toast.makeText(getActivity(), "检测到左右摇头,完成人脸识别", Toast.LENGTH_LONG).show();
        }

    }

   /* private String getImage(Bitmap bitmap) {
        byte[] file_image_bytes = ImageUtils.bitmapToByte(bitmap);
        String bitMapStr = mBASE64Encoder.encodeBuffer(file_image_bytes);
        return bitMapStr;
    }*/
    @Override
    public void onPreGet() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPostGet(Message message) {
        switch (message.what) {
            case 22:
                break;
            case 21:
                break;
            default:
                break;
        }

    }
}