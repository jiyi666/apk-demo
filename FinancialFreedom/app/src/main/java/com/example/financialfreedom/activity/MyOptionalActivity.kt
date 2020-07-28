package com.example.financialfreedom.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.myoptionaladapter.MyOptionalAdapter
import com.example.financialfreedom.adapter.stockdataadapter.StockData
import com.example.financialfreedom.database.myoptional.MyOptionalBaseControl
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_my_optional.*

/**
 * MyOptionalActivity设计实现逻辑：
 * 采用和MainActivity一样的UI设计模式，上面的添加按钮为跳转到具体的输入页面，其页面和detailactivity的
 * 布局一样
 */
class MyOptionalActivity : AppCompatActivity() {

    private val tag = "MyOptionalActivity"
    /* MyOptionalActivity的ArrayList */
    private var myOptionalStockList = ArrayList<StockData>()
    /* 使用数据库 */
    val myOptionalStockbase = MyOptionalBaseControl(this, "MyOptionalStockData", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_optional)

        /* 如果数据库不存在：创建 + 添加初始数据 */
        if (myOptionalStockbase.queryAllData("MyOptionalStockData").size == 0){
            Log.d(tag, "create!")
            /* 创建数据库 */
            myOptionalStockbase.create()
            /* 初始化stock数据 */
            initMyOptionalStockData()
            /* 添加数据 */
            for (i in 0 until (myOptionalStockList.size)){
                myOptionalStockbase.addData(myOptionalStockList.get(index = i))
            }
        }

        /* 添加股票的listener */
        addNewItem.setOnClickListener {
            val intent = Intent(this, MyOptionalDetailActivity::class.java)
            this.startActivity(intent) //开启活动
        }
    }

    override fun onResume() {
        Log.d(tag, "onResume")
        super.onResume()

        /* 从数据库中读取出最新的数据写入ArrayList */
        myOptionalStockList.clear()
        myOptionalStockList = myOptionalStockbase.queryAllData("MyOptionalStockData")

        /*
         * 获取layoutManager
         */
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        /*
         * 获取适配器
         */
        val adapter = MyOptionalAdapter(myOptionalStockList)
        recyclerView.adapter = adapter


    }
    override fun onPause() {
        Log.d(tag, "onPause")
        super.onPause()
    }
    override fun onStop() {
        Log.d(tag, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        super.onDestroy()
    }

    /**
     * 初始化stock整个list的数据
     */
    private fun initMyOptionalStockData(){
        myOptionalStockList.add(StockData("600900", "长江电力", 18.67, 19.63, 0.68, 0.02916))
        myOptionalStockList.add(StockData("600036", "招商银行", 34.71, 9.18, 1.2, 0.02916))
        myOptionalStockList.add(StockData("601838", "成都银行", 8.26,5.24, 0.42, 0.02916))
    }
}
