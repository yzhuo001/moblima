#include "jni_Console.h"
#include <conio.h>
#include <windows.h>

JNIEXPORT jint JNICALL Java_jni_Console_getChar(JNIEnv *, jclass)
{
	return _getch();
}

JNIEXPORT void JNICALL Java_jni_Console_goToXY(JNIEnv *, jclass, jshort x, jshort y)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleCursorPosition(std_out, COORD{x, y});
}

jint __stdcall Java_jni_Console_getTextAttribute(JNIEnv*, jclass)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	CONSOLE_SCREEN_BUFFER_INFO buffer_info;
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	return buffer_info.wAttributes;
}

void __stdcall Java_jni_Console_setTextAttribute(JNIEnv*, jclass, jint attr)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleTextAttribute(std_out, static_cast<WORD>(attr));
}

void __stdcall Java_jni_Console_clear(JNIEnv*, jclass)
{
	COORD home_coord = {0, 0};

	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);

	//get console area
	CONSOLE_SCREEN_BUFFER_INFO buffer_info; 
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	auto console_area = buffer_info.dwSize.X * buffer_info.dwSize.Y;

	//clear all characters
	DWORD written_char_count;
	FillConsoleOutputCharacterW(std_out, L' ', console_area, home_coord, &written_char_count);

	//clear all attributes
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	FillConsoleOutputAttribute(std_out, buffer_info.wAttributes, console_area, home_coord, &written_char_count);

	// put the cursor at (0, 0)
	SetConsoleCursorPosition(std_out, home_coord);
}

jobject __stdcall Java_jni_Console_getBufferInfo(JNIEnv* env, jclass)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	CONSOLE_SCREEN_BUFFER_INFO buffer_info;
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	auto cls = env->FindClass("jni/Console$BufferInfo");
	auto constructor = env->GetMethodID(cls, "<init>", "()V");
	auto obj = env->NewObject(cls, constructor);
	env->SetShortField(obj, env->GetFieldID(cls, "width", "S"), buffer_info.dwSize.X);
	env->SetShortField(obj, env->GetFieldID(cls, "height", "S"), buffer_info.dwSize.Y);
	env->SetShortField(obj, env->GetFieldID(cls, "cursorX", "S"), buffer_info.dwCursorPosition.X);
	env->SetShortField(obj, env->GetFieldID(cls, "cursorY", "S"), buffer_info.dwCursorPosition.Y);

	return obj;
}