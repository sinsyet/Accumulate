package com.example.viewlib.utils;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

public class AppHelper {
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

    public static int dp2px(Context ctx,float dp){
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
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
}
