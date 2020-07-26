//
// Created by 潘良晓 on 2020-06-16.
//

#ifndef SIGNPROJECT_SECURITY_H
#define SIGNPROJECT_SECURITY_H
#ifdef  __cplusplus
extern "C" {
#endif /* __cplusplus */
#include <jni.h>


jobject do_native_command(JNIEnv *env, jclass clazz, jint param_int, jobjectArray param_var_args) __attribute__((section(".command")));

#ifdef  __cplusplus
}
#endif
#endif //SIGNPROJECT_SECURITY_H
