package com.example.financialfreedom.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.myoptionaladapter.MyOptionalAdapter
import com.example.financialfreedom.adapter.stockdataadapter.StockData
import com.example.financialfreedom.database.myoptional.MyOptionalBaseControl
import com.example.financialfreedom.internet.HttpUtils
import com.example.financialfreedom.internet.parseOkHttpStockDataForNowPrice
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_my_optional.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.thread

/**
 * MyOptionalActivity设计实现逻辑：
 * 采用和MainActivity一样的UI设计模式，上面的添加按钮为跳转到具体的输入页面，其页面和detailactivity的
 * 布局一样，输入页面和详细页面为同一个activity，通过查询数据库中数据是否存在来判断用户是添加新的数据还是
 * 更新之前的数据
 */
class MyOptionalActivity : BaseActivity() {

    private val tag = "MyOptionalActivity"
    /* 线程运行标志 */
    private var threadRun = true
    /* MyOptionalActivity的ArrayList */
    private var myOptionalStockList = ArrayList<StockData>()
    /* 使用数据库 */
    val myOptionalStockbase = MyOptionalBaseControl(this, "MyOptionalStockData", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_optional)

        myOptionalActivity = this

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

        /* 线程：每1s更新一次ArrayList中的所有数据（数据库 + UI）*/
        thread {
            while (threadRun){
                for (i in 0 until myOptionalStockList.size){
                    /* 从ArrayList中读取需要查询的数据 */
                    val targetData = myOptionalStockList.get(i)
                    /* 从股票代码识别是上市还是深市 */
                    var url = "http://hq.sinajs.cn/list="
                    val shOrSz = when (targetData.stockCode[0]){
                        '6' -> "sh"
                        '5' -> "sh"
                        else -> "sz"
                    }
                    /* 拼组URL */
                    url = url + shOrSz + targetData.stockCode

                    /* 使用网络访问 */
                    HttpUtils.sendOkHttpRequest(url, object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            /* 进行网络访问 */
                            val responseData = response.body?.string()
                            /* 解析数据:得到当前价格 */
                            val nowPrice = parseOkHttpStockDataForNowPrice(responseData)
                            /* 代入当前价格，计算出最新的类对象 */
                            val tmpData = StockData(targetData.stockCode, targetData.stockName,
                            nowPrice, targetData.ttmPERatio, targetData.perDividend, targetData.tenYearNationalDebt)
                            /* 将最新数据写入数据库 */
                            myOptionalStockbase.updateData(tmpData)
                            /* UI更新 */
                            runOnUiThread {
                                val finalData = "nowPrice:" + nowPrice +
                                        ",ttmPrice:" + tmpData.ttmPrice +
                                        ",drcPrice:" + tmpData.drcPrice
                                adapter.notifyItemChanged(i, finalData)
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                        }
                    })
                }
                Thread.sleep(1000)
            }
        }
    }
    override fun onPause() {
        Log.d(tag, "onPause")
        super.onPause()
    }
    override fun onStop() {
        Log.d(tag, "onStop")
        super.onStop()
        /* onStop生命周期需要停止线程 */
        threadRun = false
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

    /**
     *  MainActivity的单实例，用于供外部类调用的static方法等
     */
    companion object{

        const val STARTDETAILACTIVITY = "startdetailactivity"
        lateinit var  myOptionalActivity : BaseActivity   //静态对象，用于适配器调用activity的相关操作

        /**
         * myOptionalActivityTodo由外部类回调MyOptionalActivity操作
         * event：需要执行的操作
         * stockCode：响应控件对应的股票代码
         */
        @JvmStatic
        fun myOptionalActivityTodo(event: String, stockCode: String){
            when (event){
                STARTDETAILACTIVITY -> {
                    /*
                     * 通过MyOptionalActivity的静态对象调用相关的方法
                     */
                    val intent = Intent(myOptionalActivity, MyOptionalDetailActivity::class.java)
                    /* 向活动传递数据：注意为Int型 */
                    intent.putExtra("stock_code", stockCode.toInt())
                    myOptionalActivity.startActivity(intent) //开启活动
                    Log.d("MyOptionalActivity", "click stockCode:$stockCode")
                }
            }
        }
    }
}
