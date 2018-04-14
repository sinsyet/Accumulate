package com.example.apphelper;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtil {

    private static Toast sToastObj;

    public static void show(Context ctx, String s){
        if (sToastObj == null) {
            sToastObj = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        }
        sToastObj.setText(s);
        sToastObj.show();
    }

    public static void show(Context ctx, @StringRes int strRes){
        if (sToastObj == null) {
            sToastObj = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        }
        sToastObj.setText(strRes);
        sToastObj.show();
    }
}
