#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "com_example_jnitest_JNITest.h"

#include <Android/log.h>
#define LOG "jiyi"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG,__VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

/* 将java中的string转换成char型 */
char *Jstring2CStr(JNIEnv *env, jstring jstr) {

    /* 通过路径去加载java类，这里加载的是java的String类 */
    jclass clazz = env->FindClass("java/lang/String");
    //jclass clazz = env->GetObjectClass(jstr); //这种方式也可以获取类对象
    /* 调用java的String方法 */
    jstring strencode = env->NewStringUTF("UTF-8");

    /* 获取java类中方法ID：参数一位java对象，参数二方法名，参数三为该方法签名 */
    jmethodID jid = env->GetMethodID(clazz, "getBytes", "(Ljava/lang/String;)[B");
    /* 回调java中的方法：*/
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, jid, strencode);

    jsize jsize = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    char* str = NULL;
    if (jsize > 0) {
        str = (char *) malloc(sizeof(char) * jsize + 1);
        if (str != NULL) {
            memcpy(str, ba, jsize);
            str[jsize] = '\0';
        }
    }

    return str;
}
JNIEXPORT jstring JNICALL Java_com_example_jnitest_JNITest_helloJNI(JNIEnv *env, jobject jobj) {
    LOGE("hello JNI!");
    return env->NewStringUTF("hello JNI!");
}

JNIEXPORT jint JNICALL Java_com_example_jnitest_JNITest_add
        (JNIEnv *env, jobject jobj, jint x, jint y) {
    LOGE("test:x = %d, y = %d", x, y);
    return x + y;
}

JNIEXPORT jstring JNICALL Java_com_example_jnitest_JNITest_stringAdd
        (JNIEnv *env, jobject jbj, jstring jstr) {
    char* str = Jstring2CStr(env, jstr);
    LOGE("str from java: %s", str);
    /* 创建新的字符串常量进行接续 */
    char* newStr = ":hello";
    return env->NewStringUTF(strcat(str, newStr));
}

JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeNum
        (JNIEnv *env, jobject jobj)
{
    /* 获取JNITest的字节码 */
    jclass clazz = env->GetObjectClass(jobj);

    /* 获取java类中变量num的Field */
    jfieldID jId = env->GetFieldID(clazz, "num", "I");

    /* 获取num的值 */
    jint jSum = env->GetIntField(jobj, jId);

    /* 修改java中num的值 */
    env->SetIntField(jobj, jId, 30);

}


JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeAge
        (JNIEnv *env, jobject jobj)
{
    /* 获取JNITest的字节码 */
    jclass clazz = env->GetObjectClass(jobj);

    /* 获取java类中变量num的Field */
    jfieldID jId = env->GetStaticFieldID(clazz, "age", "I");

    /* 获取num的值 */
    jint jAge = env->GetStaticIntField(clazz, jId);

    jAge = 28;

    env->SetStaticIntField(clazz, jId, jAge);
}

JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeStr
        (JNIEnv *env, jobject jobj)
{
    /* 获取JNITest的字节码 */
    jclass clazz = env->GetObjectClass(jobj);

    /* 获取java类中变量num的Field */
    jfieldID jId = env->GetFieldID(clazz, "str", "Ljava/lang/String;");

    /* 获取java中str的值 */
    jstring jstr = (jstring)env->GetObjectField(jobj, jId);

    /* 将jstring转化为char */
    char* cstr = (char* )env->GetStringUTFChars(jstr, NULL);

    /* 修改值 */
    char newstr[20] = "world!";
    strcat(newstr, cstr);

    /* 将char转成jstring */
    jstring jstrnew = env->NewStringUTF(newstr);

    /* 重新设置到java类变量里面去 */
    env->SetObjectField(jobj, jId, jstrnew);

    /* 释放JNI层资源 */
    env->ReleaseStringUTFChars(jstr, cstr);
}


JNIEXPORT jintArray JNICALL Java_com_example_jnitest_JNITest_intMathod
        (JNIEnv *env, jobject jobj, jintArray jintArray) {
    /* 获取数组长度 */
    jint jlen = env->GetArrayLength(jintArray);
    LOGE("Array length: %d", jlen);
    /* 获取数组元素:不以复制的形式获取 */
    jint* jArr = env->GetIntArrayElements(jintArray, 0);

    for (int i = 0; i < jlen; i++)
    {
        *(jArr + i) += 10;
    }

    /* 释放资源 */
    env->ReleaseIntArrayElements(jintArray, jArr, 0);

    return jintArray;
}

/* 回调java类中的helloFromJava方法 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callbackJni
        (JNIEnv *env, jobject jobj)
{
    /* 通过包名找到Java中的类名 */
    jclass clazz = env->FindClass("com/example/jnitest/JNITest");
    if (clazz == 0)
    {
        LOGE("failed to find class JNITest");
    }
    else
    {
        LOGE("find class JNITest succeed!");
    }

    jmethodID jId = env->GetMethodID(clazz, "helloToJava", "()V");
    if (jId == 0)
    {
        LOGE("failed to find method in java!");
    }
    else
    {
        LOGE("find method succeed!");
    }

    /* 调用java中的方法 */
    env->CallVoidMethod(jobj, jId);
}


/* 回调java类中的addSum函数:传入两个int型参数 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callAddSum
        (JNIEnv *env, jobject jobj)
{
    /* 通过包名找到Java中的类名 */
    jclass clazz = env->FindClass("com/example/jnitest/JNITest");
    if (clazz == 0)
    {
        LOGE("failed to find class JNITest");
    }
    else
    {
        LOGE("find class JNITest succeed!");
    }

    /* 获取java类中方法id：参数一为类对象，参数二为java中方法名，参数三为方法唯一签名 */
    jmethodID jId = env->GetMethodID(clazz, "addSum", "(II)V");
    if (jId == 0)
    {
        LOGE("failed to find method in java!");
    }
    else
    {
        LOGE("find method succeed!");
    }

    /* 调用java中的方法 */
    env->CallVoidMethod(jobj, jId, 10, 20);
}

/* 回调java类中的StrFromJni方法：传入参数String */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callStrFromJni
        (JNIEnv *env, jobject jobj)
{
    /* 通过包名找到Java中的类名 */
    jclass clazz = env->FindClass("com/example/jnitest/JNITest");
    if (clazz == 0)
    {
        LOGE("failed to find class JNITest");
    }
    else
    {
        LOGE("find class JNITest succeed!");
    }

    /* 获取java类中方法id：参数一为类对象，参数二为java中方法名，参数三为方法唯一签名 */
    jmethodID jId = env->GetMethodID(clazz, "StrFromJni", "(Ljava/lang/String;)V");
    if (jId == 0)
    {
        LOGE("failed to find method in java!");
    }
    else
    {
        LOGE("find method succeed!");
    }

    /* 调用java中的方法 */
    env->CallVoidMethod(jobj, jId, env->NewStringUTF("I'm from JNI!"));
}
#ifdef __cplusplus
}
#endif