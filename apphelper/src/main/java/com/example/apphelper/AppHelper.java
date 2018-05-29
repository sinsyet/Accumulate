package com.example.apphelper;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntRange;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YGX
 *         <p>
 *         app帮助者
 */

public class AppHelper {

    private static ExecutorService sPool = Executors.newCachedThreadPool();

    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void run(Runnable r){
        if(r == null) return;

        sPool.execute(r);
    }

    public static boolean isNetActive(Context ctx) {
        // 判断网络状态
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {// 有网
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUiThread() {
        return Thread.currentThread().getName().equals("main");
    }

    public static void runOnUiThread(Runnable r) {
        if (r == null) return;

        sMainHandler.post(r);
    }

    public static int getCameraDisplayRotation(Context ctx,int cameraId) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        int rotation = wm.getDefaultDisplay().getRotation();

        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (720 - (cameraInfo.orientation + degree)) % 360;
        } else {
            return (360 - degree + cameraInfo.orientation) % 360;
        }
    }

    public static int dp2px(Context ctx,float dpValue) {

        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static Random sR = new Random();

    public static int randArgb() {

        int alpha = sR.nextInt(0xFF);
        int red = sR.nextInt(0xFF);
        int green = sR.nextInt(0xFF);
        int blue = sR.nextInt(0xFF);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static long charArr2CardId(char[] cArr) {
        long result = 0;
        int cursor = 0;
        for (int i = cArr.length - 1; i >= 0; i--) {
            result |= ((long) (cArr[i] & 0xFF)) << (8 * cursor);
            cursor++;
        }
        return result;
    }


    public static int getIntFractionValue(int startValue, int endValue, float fraction) {
        return startValue + (int) ((endValue - startValue) * fraction);
    }

    public static float getFloatFractionValue(float startValue, float endValue, float fraction) {
        return startValue + ((endValue - startValue) * fraction);
    }

    /**
     * 根据fraction从可变数组中取出对应的值
     * @param fraction 比例
     * @param values 可变数组值
     * @return 百分比对应的值
     */
    public static float getValue(float fraction, float...values){
        if(values.length == 0) return 0;
        if(values.length == 1) return values[0];

        // fraction == 1f的时候, 会造成下面的计算index + 1超出范围
        if(fraction == 1f) return values[values.length - 1];
        // fraction: 0.23 1 2 3 2 1
        float gp = 1f / (values.length - 1);    // 0.25
        int index = (int) (fraction / gp);      // 0;
        float section_start = values[index];    // 1
        float section_end = values[index + 1];  // 2
        float section_fraction = (fraction - index * gp) / gp; // 0.23

        return getFloatFractionValue(section_start,section_end,section_fraction);
    }


    public static String getDns(@IntRange(from = 1, to = 2) int index){
        if(index < 1 || index > 2) return null;
        Process process = null;
        InputStream is = null;
        try {
            process = Runtime.getRuntime().exec("getprop net.dns" + index);
            is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.readLine().trim();
        } catch (IOException e) {
        }finally {
            if (process != null) {
                process.destroy();
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static Camera.Size getMaxCameraSupportSize(Camera.Parameters parameters){
        if(parameters == null) return null;

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size size = null;
        for (Camera.Size s : sizes){
            Log.e(TAG, "getMaxCameraSupportSize: "+s.height+" // "+s.width);
            if(size == null){
                size = s;
                continue;
            }

            if(size.height < s.height){
                size = s;
            }
        }
        return size;
    }

    public static void setFullScreen(AppCompatActivity aty){
        if(aty == null) return;



        /*set it to be no title*/
        aty.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        aty.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = aty.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    private static final String TAG = "AppHelper";
}
