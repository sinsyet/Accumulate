package com.example.androidhttpserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.androidhttpserver.base.Constants;
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


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        int port = intent.getIntExtra(Constants.KEY.PORT, Constants.DEFAULT_PORT);
        String webinfoPath = intent.getStringExtra(Constants.KEY.WEBINFO_PATH);
        androidHttpServer = new AndroidHttpServer(port, getApplicationContext(),webinfoPath);
        try {
            androidHttpServer.start();
            Log.e(TAG, "onCreate: android http server is running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStart(intent, startId);
    }
}
