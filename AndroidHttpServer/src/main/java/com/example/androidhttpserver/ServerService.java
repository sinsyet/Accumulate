package com.example.androidhttpserver;

import android.app.Service;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class ServerService extends Service {
    private SimpleServer server;
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

        server = new SimpleServer();
        try {

            // 因为程序模拟的是html放置在asset目录下，
            // 所以在这里存储一下AssetManager的指针。
            server.asset_mgr = this.getAssets();

            // 启动web服务
            server.start();

            Log.i("Httpd", "The server started.");
        } catch(IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        // XmlResourceParser xml = getResources().getXml(R.xml.servlet);
    }
}
