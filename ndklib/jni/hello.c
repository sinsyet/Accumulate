//
// Created by Administrator on 2018/1/31.
//
#include <stdio.h>
#include <jni.h>

// typedef const struct JNINativeInterface* JNIEnv; // -> JNIEnv 等同于 JNINativeInterface *
// JNIEnv* 等同于 const struct JNINativeInterface **
// NewStringUTF是JNINativeInterface里的函数
jstring Java_com_example_ndklib_StrTest_getStr(JNIEnv *env, jobject obj)
{
    char *str = "hello ,fuck you!";
    return (*env)->NewStringUTF(env,str);
}
