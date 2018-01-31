package com.example.apphelper;

import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.WindowManager;


import java.util.Random;

/**
 * @author YGX
 *         <p>
 *         app帮助者
 */

public class AppHelper {


    private static Handler sMainHandler = new Handler(Looper.getMainLooper());


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

    public static float getFloatFractionValue2(float fraction, float... value) {

        int length = value.length;
        if (length == 0) return 0;
        if (length == 1) return value[0];

        // 3个数的时候, 只有2个区间
        int len = length - 1;

        // ratio表示每段区间分布的比例大小
        float ratio = 1.0f / len;

        // 获得区间开始索引
        int startIndex = (int) (fraction / ratio);
        int endIndex = startIndex + 1;

        // 获得fraction在ratio中的比例
        float f = ((fraction - startIndex * ratio) / ratio);
        return getFloatFractionValue(value[startIndex], value[endIndex], f);
    }
}
