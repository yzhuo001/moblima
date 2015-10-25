#include "jni_Console.h"
#include <conio.h>
#include <windows.h>
#include <iostream>

JNIEXPORT jchar JNICALL Java_jni_Console_getChar(JNIEnv *, jclass)
{
	return static_cast<jchar>(_getch());
}

JNIEXPORT void JNICALL Java_jni_Console_goToXY(JNIEnv *, jclass, jint x, jint y)
{
	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleCursorPosition(std_out, COORD{static_cast<short>(x), static_cast<short>(y)});
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

void __stdcall Java_jni_Console_clear(JNIEnv*, jclass, jint x, jint y, jint width, jint height)
{
	COORD home_coord = {static_cast<short>(x), static_cast<short>(y)};

	auto std_out = GetStdHandle(STD_OUTPUT_HANDLE);

	//get console area
	CONSOLE_SCREEN_BUFFER_INFO buffer_info; 
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	if (width < 0) {
		width = buffer_info.dwSize.X - x;
	}

	if (height < 0) {
		height = buffer_info.dwSize.Y - y;
	}

	auto clear_area = static_cast<short>(width * height);

	//clear characters
	DWORD written_char_count;
	FillConsoleOutputCharacterW(std_out, L' ', clear_area, home_coord, &written_char_count);

	//clear attributes
	GetConsoleScreenBufferInfo(std_out, &buffer_info);
	FillConsoleOutputAttribute(std_out, buffer_info.wAttributes, clear_area, home_coord, &written_char_count);

	// put the cursor at home
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
	env->SetIntField(obj, env->GetFieldID(cls, "width", "I"), buffer_info.dwSize.X);
	env->SetIntField(obj, env->GetFieldID(cls, "height", "I"), buffer_info.dwSize.Y);
	env->SetIntField(obj, env->GetFieldID(cls, "cursorX", "I"), buffer_info.dwCursorPosition.X);
	env->SetIntField(obj, env->GetFieldID(cls, "cursorY", "I"), buffer_info.dwCursorPosition.Y);

	return obj;
}

std::pair<int, int> charToVk(jchar ch)
{
	auto modifier_vk = VkKeyScanW(ch);
	auto vk = modifier_vk & 0xff; //low
	auto modifier = (modifier_vk >> 8) & 0xff; //hi
	return std::make_pair(vk, modifier);
}

jboolean __stdcall Java_jni_Console_charPressed(JNIEnv*, jclass, jchar ch)
{
	auto vk_modifier_pair = charToVk(ch);
	
	return (GetAsyncKeyState(vk_modifier_pair.first) & 0x8000)
		&& (!(vk_modifier_pair.second & 1) || (GetAsyncKeyState(VK_SHIFT) & 0x8000))
		&& (!(vk_modifier_pair.second & 2) || (GetAsyncKeyState(VK_CONTROL) & 0x8000));
}

void __stdcall Java_jni_Console_putCharIn(JNIEnv*, jclass, jchar ch)
{
	INPUT_RECORD records[2];
	KEY_EVENT_RECORD key_event;
	auto vk_modifier_pair = charToVk(ch);
	key_event.bKeyDown = TRUE;
	key_event.wRepeatCount = 1;
	key_event.wVirtualKeyCode = vk_modifier_pair.first;
	key_event.wVirtualScanCode = MapVirtualKeyW(vk_modifier_pair.first, MAPVK_VK_TO_VSC);
	key_event.uChar.UnicodeChar = static_cast<wchar_t>(ch);
	auto control_key_state = 0u;

	if (vk_modifier_pair.second & 1) {
		control_key_state |= SHIFT_PRESSED;
	}

	if (vk_modifier_pair.second & 2) {
		control_key_state |= LEFT_CTRL_PRESSED;
	}

	key_event.dwControlKeyState = control_key_state;
	records[0].EventType = KEY_EVENT;
	records[0].Event.KeyEvent = key_event;
	records[1].EventType = KEY_EVENT;
	key_event.bKeyDown = FALSE;
	records[1].Event.KeyEvent = key_event;

	DWORD written_event_count;
	WriteConsoleInput(GetStdHandle(STD_INPUT_HANDLE), records, sizeof(records)/ sizeof(*records), &written_event_count);
}
