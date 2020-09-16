package com.jiuzhouweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class JiuzhouWeatherApplication : Application() {
    companion object{
        /* 因为获取的context是全局的，仅存在一个，所以加注释告知系统不会存在内存泄漏的风险 */
        @SuppressLint("StaticFiledLeak")
        lateinit var context: Context
        /* 从彩云科技获取的令牌值 */
        const val TOKEN = "4tiSkthHdG8DV2rP"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}