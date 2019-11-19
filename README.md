# apk-demo
simple apk demo

#AudioTrackPlayer
audiotrack的播放，播放文件指定为sd卡下面的test.wav文件，暂不支持多track播放，且播放模式为stream；
码流文件默认路径为：/storage，apk未带测试码流，请自行存放，码流修改为：PlayThread类getFileStream方法，audiotrack的api操作
在TrackAudio类中；

#JNITest
JNI编程的演示demo，演示了java调用JNI和JNI回调java方法的实现；

#MediaCodecSample
mediacodec的简单sample，仅仅支持视频渲染，用于熟悉mediacodec通路是怎么玩的，sample摘自何俊林