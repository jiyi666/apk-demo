LOCAL_PATH := $(call my-dir)    #接收当前Android.mk文件的绝对路径

include $(CLEAR_VARS)           #类似于工具初始化的操作

LOCAL_MODULE := libhellojni     #模块名
LOCAL_SRC_FILES := hello.cpp    #依赖的源文件

LOCAL_LDLIBS    := -llog        #引用log对应的库文件

include $(BUILD_SHARED_LIBRARY) #将该模块编译成一个动态库