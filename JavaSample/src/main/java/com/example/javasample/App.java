package com.example.javasample;

import com.example.javasample.utils.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
       new UdpServer(12200).start();
      /* String msg = "{\"t\":1,\"mid\":1526373002242}";
       String regex = "\"mid\":[\\d]{1,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);
        if(matcher.find()){
            String group = matcher.group();
            Log.e(TAG,"group : "+group);
        }else{
            Log.e(TAG, "main: not find");
        }*/
    }

    private static final String TAG = "App";
}
