/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class sg_edu_ntu_sce_cx2002_group6_console_Console */

#ifndef _Included_sg_edu_ntu_sce_cx2002_group6_console_Console
#define _Included_sg_edu_ntu_sce_cx2002_group6_console_Console
#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    isKeyDown
	* Signature: (C)Z
	*/
	JNIEXPORT jboolean JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_isKeyDown
		(JNIEnv *, jclass, jchar);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    init
	* Signature: ()V
	*/
	JNIEXPORT void JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_init
		(JNIEnv *, jclass);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    putStrIn
	* Signature: (Ljava/lang/String;)V
	*/
	JNIEXPORT void JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_putStrIn
		(JNIEnv *, jclass, jstring);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    goToXY
	* Signature: (II)V
	*/
	JNIEXPORT void JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_goToXY
		(JNIEnv *, jclass, jint, jint);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    clear
	* Signature: (IIII)V
	*/
	JNIEXPORT void JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_clear
		(JNIEnv *, jclass, jint, jint, jint, jint);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    getBufferInfo
	* Signature: ()Lsg/edu/ntu/sce/cx2002/group6/console/Console/BufferInfo;
	*/
	JNIEXPORT jobject JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_getBufferInfo
		(JNIEnv *, jclass);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    getCh
	* Signature: ()C
	*/
	JNIEXPORT jchar JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_getCh
		(JNIEnv *, jclass);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    getTextAttribute
	* Signature: ()I
	*/
	JNIEXPORT jint JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_getTextAttribute
		(JNIEnv *, jclass);

	/*
	* Class:     sg_edu_ntu_sce_cx2002_group6_console_Console
	* Method:    setTextAttribute
	* Signature: (I)V
	*/
	JNIEXPORT void JNICALL Java_sg_edu_ntu_sce_cx2002_group6_console_Console_setTextAttribute
		(JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
