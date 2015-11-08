/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jni_Console */

#ifndef _Included_jni_Console
#define _Included_jni_Console
#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     jni_Console
	* Method:    isKeyDown
	* Signature: (C)Z
	*/
	JNIEXPORT jboolean JNICALL Java_jni_Console_isKeyDown
		(JNIEnv *, jclass, jchar);

	/*
	* Class:     jni_Console
	* Method:    init
	* Signature: ()V
	*/
	JNIEXPORT void JNICALL Java_jni_Console_init
		(JNIEnv *, jclass);

	/*
	* Class:     jni_Console
	* Method:    putStrIn
	* Signature: (Ljava/lang/String;)V
	*/
	JNIEXPORT void JNICALL Java_jni_Console_putStrIn
		(JNIEnv *, jclass, jstring);

	/*
	* Class:     jni_Console
	* Method:    goToXY
	* Signature: (II)V
	*/
	JNIEXPORT void JNICALL Java_jni_Console_goToXY
		(JNIEnv *, jclass, jint, jint);

	/*
	* Class:     jni_Console
	* Method:    clear
	* Signature: (IIII)V
	*/
	JNIEXPORT void JNICALL Java_jni_Console_clear
		(JNIEnv *, jclass, jint, jint, jint, jint);

	/*
	* Class:     jni_Console
	* Method:    getBufferInfo
	* Signature: ()Ljni/Console/BufferInfo;
	*/
	JNIEXPORT jobject JNICALL Java_jni_Console_getBufferInfo
		(JNIEnv *, jclass);

	/*
	* Class:     jni_Console
	* Method:    getCh
	* Signature: ()C
	*/
	JNIEXPORT jchar JNICALL Java_jni_Console_getCh
		(JNIEnv *, jclass);

	/*
	* Class:     jni_Console
	* Method:    getTextAttribute
	* Signature: ()I
	*/
	JNIEXPORT jint JNICALL Java_jni_Console_getTextAttribute
		(JNIEnv *, jclass);

	/*
	* Class:     jni_Console
	* Method:    setTextAttribute
	* Signature: (I)V
	*/
	JNIEXPORT void JNICALL Java_jni_Console_setTextAttribute
		(JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
