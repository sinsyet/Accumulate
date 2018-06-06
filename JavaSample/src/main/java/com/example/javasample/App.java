package com.example.javasample;

import com.example.javasample.utils.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
      /* new UdpServer(12200).start();*/
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
     /* byte b = -1;
        System.out.println(b & 0xFF);*/
       /* byte[] arr = {0x20, 0x52, 0x52, 0x00, 0x00, 0x03};
        // 20 52 52 00 ff 03
        calcCommandsBBC(arr);
        toHex(arr);*/

        /*System.out.println(0.65D * 1.0f);*/
        System.out.println(100%3.0);
        System.out.println(Math.round(12.5));
        System.out.println(Double.toHexString(12.5));
        System.out.println(Float.toHexString(1.0f));
        /*
        0x1.9p3
        0x1.0p0
         */
    }

    private static void toHex(byte[] arr){
        for (byte b: arr){
            System.out.print(Integer.toHexString(b & 0xFF) + " ");
        }
    }

    private static final String TAG = "App";

    private static byte[] calcCommandsBBC(byte[] src) {

        int length = src.length;

        byte c = src[1];
        for (int i = 2; i < length - 2; i++) {
            c ^= src[i];
        }

        src[length - 2] = (byte) ~c;

        return src;
    }
}
