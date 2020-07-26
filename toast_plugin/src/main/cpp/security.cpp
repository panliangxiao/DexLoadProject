
#include <string>
#include "security.h"

using namespace std;

static const JNINativeMethod gMethods[] = {
        {"doNativeCommand", "(I[Ljava/lang/Object;)Ljava/lang/Object;", (void *)do_native_command}
};


jobject do_native_command(JNIEnv *env, jclass clazz, jint param_int, jobjectArray param_var_args){
    std::string result = "native string ! ";

    return env->NewStringUTF(result.c_str());
}


JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved){

    JNIEnv* env = NULL;
    if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK) //从JavaVM获取JNIEnv，一般使用1.4的版本
        return -1;
    //指定类的路径，通过FindClass 方法来找到对应的类
    const char* class_name  = "com/plx/android/plugin/JNICLibrary";
    jclass clazz = env->FindClass(class_name);
    if (!clazz){
        return -1;
    }
    if(env->RegisterNatives(clazz, gMethods, sizeof(gMethods)/sizeof(gMethods[0])))
    {
        return -1;
    }
    return JNI_VERSION_1_4;
}
