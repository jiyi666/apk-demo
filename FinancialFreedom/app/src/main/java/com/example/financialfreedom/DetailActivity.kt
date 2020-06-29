package com.example.financialfreedom

import android.os.Bundle
import android.util.Log
import com.example.financialfreedom.common.database.DatabaseControler
import com.example.financialfreedom.utils.BaseActivity

/**
 *  具体数据展示页面的activity
 */
class DetailActivity : BaseActivity(){

    val tag : String = "DetailActivity"
    var position : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_data)
        /*
         * 接收MainActivity传递过来的数据
         */
        position = intent.getIntExtra("database_position", -1)
        Log.d(tag, "position:$position")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy!")
    }

    fun getDetailData(position: Int){
        val databaseControler = DatabaseControler(this, "StockData", 1)
        val targetData = databaseControler.queryData("StockData", position)
        //Toast.makeText(this, targetData.toString(), Toast.LENGTH_LONG).show()
    }

}