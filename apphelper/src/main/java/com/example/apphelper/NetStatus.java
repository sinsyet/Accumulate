package com.example.apphelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.demo.app.App;
import com.example.demo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YGX
 *         <p>
 *         网络状态广播接收者
 */

public class NetStatus extends BroadcastReceiver {
    private static final String TAG = "NetStatus";
    private static boolean sNetStatus;
    private static NetStatus netStatus;

    static {
        sNetStatus = AppHelper.isNetActive();
    }

    private static List<Callback> sCbs = new ArrayList<>();

    public interface Callback {

        void onNetChanged(boolean connect);
    }

    public static void registerNoRepeat(){
        if(netStatus!= null) return;
        netStatus = new NetStatus();
        App.sContext
                .registerReceiver(
                        netStatus,
                        new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void unregister(){
        if(netStatus == null) return;
        App.sContext.unregisterReceiver(netStatus);
        netStatus = null;
    }

    public static void subscribe(Callback cb) {
        registerNoRepeat();
        sCbs.add(cb);
    }

    public static void unSubscribe(Callback cb) {
        sCbs.remove(cb);
        if(sCbs.size() == 0){
            unregister();
        }
    }

    public static boolean isNetActive() {
        return AppHelper.isNetActive();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            return;
        }

        boolean netConneted = AppHelper.isNetActive();
        if (netConneted == sNetStatus) {
            return;
        }
        sNetStatus = netConneted;
        Log.e(TAG, "onReceive: "+sNetStatus);
        postNetStatus();
    }

    private static void postNetStatus(){
        for (Callback cb : sCbs) {
            if (cb != null) {
                cb.onNetChanged(sNetStatus);
            }
        }
    }
}
