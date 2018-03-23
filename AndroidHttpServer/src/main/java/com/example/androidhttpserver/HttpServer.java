package com.example.androidhttpserver;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.example.androidhttpserver.base.Constants;

public class HttpServer {

    private static Context sCtx;

    public static void init(Context ctx,
                            @IntRange(from = 1, to = 65535) int serverPort,
                            String assetsWebInfoPath){
        sCtx = ctx;
        Intent intent = new Intent(ctx, ServerService.class);
        intent.putExtra(Constants.KEY.PORT, serverPort);
        intent.putExtra(Constants.KEY.WEBINFO_PATH, assetsWebInfoPath);
        sCtx.startService(intent);
    }

}
