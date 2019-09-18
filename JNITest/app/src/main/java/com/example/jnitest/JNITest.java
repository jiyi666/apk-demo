package com.example.jnitest;

import android.util.Log;

public class JNITest {

    static {
        System.loadLibrary("hellojni");
    }

    public  static int age = 18;

    public int num = 25;

    public String str = "hello";

    /* JNI返回一个hello的字符串 */
    public native String helloJNI();

    /* JNI返回两个数的相加值 */
    public native int add(int x, int y);

    /* JNI改变类中变量num的值 */
    public native void changeNum();

    /* JNI改变类中变量str的值 */
    public native void changeStr();

    /* JNI改变类中static变量age的值 */
    public native void changeAge();

    /* JNI返回s字符串并在后面接续内容hello */
    public native String stringAdd(String s);

    /* JNI返回数组arrys并将每个元素的值加10 */
    public native int[] intMathod(int[] arrys);

    /* JNI回调java函数:调用helloToJava */
    public native void callbackJni();

    /* JNI回调java函数:调用addSum */
    public native void callAddSum();

    /* JNI回调java函数:调用StrFromJni */
    public native void callStrFromJni();

    /* java层中的共有方法，供JNI回调 */
    public void helloToJava(){
        Log.e("jiyi", "helloFromJava:hello!");
    }

    public void addSum(int x, int y){
        Log.e("jiyi", "addSum: " + (x + y));
    }

    public void StrFromJni(String str){
        Log.e("jiyi", "StrFromJni: " + str);
    }

}
