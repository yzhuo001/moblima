#define NOMINMAX
#include "jni_Console.h"
#include <conio.h>
#include <windows.h>
#include <algorithm>
#include <vector>
#include <iostream>

HANDLE stdout_handle()
{
	static auto out = GetStdHandle(STD_OUTPUT_HANDLE);
	return out;
}

JNIEXPORT jchar JNICALL Java_jni_Console_getCh(JNIEnv *, jclass)
{
	return static_cast<jchar>(_getch());
}

JNIEXPORT void JNICALL Java_jni_Console_goToXY(JNIEnv *, jclass, jint x, jint y)
{
	SetConsoleCursorPosition(stdout_handle(), COORD{static_cast<short>(x), static_cast<short>(y)});
}

jint __stdcall Java_jni_Console_getTextAttribute(JNIEnv*, jclass)
{
	CONSOLE_SCREEN_BUFFER_INFO buffer_info;
	GetConsoleScreenBufferInfo(stdout_handle(), &buffer_info);
	return buffer_info.wAttributes;
}

void __stdcall Java_jni_Console_setTextAttribute(JNIEnv*, jclass, jint attr)
{
	SetConsoleTextAttribute(stdout_handle(), static_cast<WORD>(attr));
}

void __stdcall Java_jni_Console_clear(JNIEnv*, jclass, jint x, jint y, jint width, jint height)
{
	COORD home_coord = {static_cast<short>(x), static_cast<short>(y)};

	//get console area
	CONSOLE_SCREEN_BUFFER_INFO buffer_info; 
	GetConsoleScreenBufferInfo(stdout_handle(), &buffer_info);
	if (width < 0) {
		width = buffer_info.dwSize.X - x + 1;
	}

	if (height < 0) {
		height = buffer_info.dwSize.Y - y + 1;
	}

	auto clear_area = static_cast<short>(width * height);

	//clear characters
	DWORD written_char_count;
	FillConsoleOutputCharacterW(stdout_handle(), L' ', clear_area, home_coord, &written_char_count);

	//clear attributes
	GetConsoleScreenBufferInfo(stdout_handle(), &buffer_info);
	FillConsoleOutputAttribute(stdout_handle(), buffer_info.wAttributes, clear_area, home_coord, &written_char_count);

	// put the cursor at home
	SetConsoleCursorPosition(stdout_handle(), home_coord);
}

jobject __stdcall Java_jni_Console_getBufferInfo(JNIEnv* env, jclass)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	CONSOLE_SCREEN_BUFFER_INFO buffer_info;
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	auto cls = env->FindClass("jni/Console$BufferInfo");
	auto constructor = env->GetMethodID(cls, "<init>", "()V");
	auto obj = env->NewObject(cls, constructor);
	env->SetIntField(obj, env->GetFieldID(cls, "width", "I"), buffer_info.dwSize.X);
	env->SetIntField(obj, env->GetFieldID(cls, "height", "I"), buffer_info.dwSize.Y);
	env->SetIntField(obj, env->GetFieldID(cls, "cursorX", "I"), buffer_info.dwCursorPosition.X);
	env->SetIntField(obj, env->GetFieldID(cls, "cursorY", "I"), buffer_info.dwCursorPosition.Y);
	env->SetIntField(obj, env->GetFieldID(cls, "windowTop", "I"), buffer_info.srWindow.Top);
	env->SetIntField(obj, env->GetFieldID(cls, "windowLeft", "I"), buffer_info.srWindow.Left);
	env->SetIntField(obj, env->GetFieldID(cls, "windowRight", "I"), buffer_info.srWindow.Right);
	env->SetIntField(obj, env->GetFieldID(cls, "windowBottom", "I"), buffer_info.srWindow.Bottom);

	return obj;
}

std::pair<int, int> charToVk(jchar ch)
{
	auto modifier_vk = VkKeyScanW(ch);
	auto vk = modifier_vk & 0xff; //low
	auto modifier = (modifier_vk >> 8) & 0xff; //hi
	return std::make_pair(vk, modifier);
}

jboolean __stdcall Java_jni_Console_isKeyDown(JNIEnv*, jclass, jchar ch)
{
	auto vk_modifier_pair = charToVk(ch);
	
	return (GetAsyncKeyState(vk_modifier_pair.first) & 0x8000)
		&& (!(vk_modifier_pair.second & 1) || (GetAsyncKeyState(VK_SHIFT) & 0x8000))
		&& (!(vk_modifier_pair.second & 2) || (GetAsyncKeyState(VK_CONTROL) & 0x8000));
}

void __stdcall Java_jni_Console_putStrIn(JNIEnv* env, jclass, jstring str)
{
	auto utf8 = env->GetStringUTFChars(str, nullptr);
	std::vector<INPUT_RECORD> records;
	records.reserve(env->GetStringLength(str) * 2);

	for (auto it = utf8; *it; ++it) {
		KEY_EVENT_RECORD key_event;
		auto vk_modifier_pair = charToVk(*it);
		key_event.bKeyDown = TRUE;
		key_event.wRepeatCount = 1;
		key_event.wVirtualKeyCode = vk_modifier_pair.first;
		key_event.wVirtualScanCode = MapVirtualKeyW(vk_modifier_pair.first, MAPVK_VK_TO_VSC);
		key_event.uChar.UnicodeChar = static_cast<wchar_t>(*it);
		auto control_key_state = 0u;

		if (vk_modifier_pair.second & 1) {
			control_key_state |= SHIFT_PRESSED;
		}

		if (vk_modifier_pair.second & 2) {
			control_key_state |= LEFT_CTRL_PRESSED;
		}

		key_event.dwControlKeyState = control_key_state;

		INPUT_RECORD record;
		
		record.EventType = KEY_EVENT;
		record.Event.KeyEvent = key_event;
		records.push_back(record);
		
		key_event.bKeyDown = FALSE;
		record.Event.KeyEvent = key_event;
		records.push_back(record);
	}
	env->ReleaseStringUTFChars(str, utf8);

	DWORD written_event_count;
	WriteConsoleInput(GetStdHandle(STD_INPUT_HANDLE), records.data(), records.size(), &written_event_count);  
}

void __stdcall Java_jni_Console_scrollWindow(JNIEnv*, jclass, jint deltaX, jint deltaY)
{
	CONSOLE_SCREEN_BUFFER_INFO bi;
	GetConsoleScreenBufferInfo(stdout_handle(), &bi);
	auto window_rect = bi.srWindow;

	window_rect.Top = static_cast<short>(std::max(window_rect.Top + deltaY, 0l));  
	window_rect.Bottom = static_cast<short>(window_rect.Bottom + deltaY); 
	window_rect.Left = static_cast<short>(std::max(window_rect.Left + deltaX, 0l));        
	window_rect.Right = static_cast<short>(window_rect.Right + deltaX);
	SetConsoleWindowInfo(stdout_handle(), TRUE, &window_rect);
}


JavaVM* jvm;

BOOL __stdcall handler_routine(DWORD dwCtrlType)
{
	if (dwCtrlType == CTRL_CLOSE_EVENT || dwCtrlType == CTRL_C_EVENT) {
		JNIEnv *env;
		jvm->AttachCurrentThread(reinterpret_cast<void **>(&env), &env);
		auto cls = env->FindClass("jni/Console");
		auto method_id = env->GetStaticMethodID(cls, "handleClosing", "()V");
		env->CallStaticVoidMethod(cls, method_id);
		jvm->DetachCurrentThread();
		return TRUE;
	}

	return FALSE;
}

void __stdcall Java_jni_Console_init(JNIEnv* env, jclass)
{
	env->GetJavaVM(&jvm);
	SetConsoleCtrlHandler(handler_routine, TRUE);
}