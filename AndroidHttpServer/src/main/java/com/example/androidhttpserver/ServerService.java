package com.example.androidhttpserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.androidhttpserver.servlet.impl.AndroidHttpServer;

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
