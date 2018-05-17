package com.example.facedemo;

import android.content.Context;

import com.iflytek.cloud.SpeechUtility;

public class Msc {

    public static void init(Context ctx){
        SpeechUtility.createUtility(ctx, "appid=" + ctx.getString(R.string.app_id));
    }
}
