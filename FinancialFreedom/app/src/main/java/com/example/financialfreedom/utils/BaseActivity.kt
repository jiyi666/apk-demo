package com.example.financialfreedom.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/** 长按删除Flag */
internal var onLongClickFlag = false
/** 长按删除的股票代码 */
internal var removeStockCode = ""

open class BaseActivity : AppCompatActivity(){

    /*
     * 常量：类tag
     */
    private val tag = "BaseActivity"

    /*
     * 覆写onCreate方法
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        Log.d(tag, "onCreate " + javaClass.simpleName)    //打印当前类名
    }

    /*
     * 覆写onDestroy方法
     */
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}