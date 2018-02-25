package com.example.androidhttpserver;

import android.app.Service;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class ServerService extends Service {
    private static final String TAG = "ServerService";
    private AndroidHttpServer androidHttpServer;

    public ServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        androidHttpServer = new AndroidHttpServer(9999, getApplicationContext());
        try {
            androidHttpServer.start();
            Log.e(TAG, "onCreate: android http server is running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
