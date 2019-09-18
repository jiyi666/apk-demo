/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_jnitest_JNITest */

#ifndef _Included_com_example_jnitest_JNITest
#define _Included_com_example_jnitest_JNITest
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_jnitest_JNITest
 * Method:    helloJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_jnitest_JNITest_helloJNI
  (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_jnitest_JNITest_add
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    changeNum
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeNum
  (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    changeStr
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeStr
        (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    changeAge
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_changeAge
  (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    stringAdd
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_jnitest_JNITest_stringAdd
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    intMathod
 * Signature: ([I)[I
 */
JNIEXPORT jintArray JNICALL Java_com_example_jnitest_JNITest_intMathod
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    callbackJni
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callbackJni
  (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    callAddSum
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callAddSum
  (JNIEnv *, jobject);

/*
 * Class:     com_example_jnitest_JNITest
 * Method:    callStrFromJni
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_jnitest_JNITest_callStrFromJni
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
