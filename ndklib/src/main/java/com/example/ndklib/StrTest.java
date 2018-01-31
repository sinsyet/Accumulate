package com.example.ndklib;


public class StrTest {
    static {
        System.loadLibrary("hello_jni");
    }
    public native static String getStr();
}
