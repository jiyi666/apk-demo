package com.example.financialfreedom

import android.os.Bundle
import android.util.Log
import com.example.financialfreedom.utils.BaseActivity

/**
 *  具体数据展示页面的activity
 */
class DetailActivity : BaseActivity(){

    val tag : String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy!")
    }

}