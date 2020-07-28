package com.example.financialfreedom.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.stockdataadapter.StockData
import com.example.financialfreedom.adapter.stockdataadapter.StockDataAdapter
import com.example.financialfreedom.database.stockdata.StockDatabaseControl
import com.example.financialfreedom.utils.BaseActivity
import com.example.financialfreedom.internet.HttpUtils
import com.example.financialfreedom.internet.parseOkHttpStockDataForNowPrice
import com.example.financialfreedom.utils.onLongClickFlag
import com.example.financialfreedom.utils.removeStockCode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.addNewItem
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_my_optional.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : BaseActivity() {

    private val tag = "MainActivity"
    private var stockList = ArrayList<StockData>()
    /* 线程停止标志 */
    private var threadRun = true
    /* 消息集 */
    val updateDataFromInternet = 1
    /*
     * 使用数据库
     */
    val databaseStock = StockDatabaseControl(this, "StockData", 1)

    /**
     * onCreate:显示数据库中的数据
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //去掉标题栏的代码
        setContentView(R.layout.activity_main)

        /*
         * 将当前活动赋值给静态对象
         */
        mainActivity = this

        /*
         * 如果数据库不存在：创建 + 添加初始数据
         */
        if (databaseStock.queryAllData("StockData").size == 0){
            /* 创建数据库 */
            databaseStock.create()
            /*
             * 初始化stock数据
             */
            initStockData()
            /* 添加数据 */
            for (i in 0 until (stockList.size)){
                databaseStock.addData(stockList.get(index = i))
            }
        }

        /* 添加股票的listener */
        addNewItem.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            this.startActivity(intent) //开启活动
        }
    }

    /**
     *  onResume：从数据库读取最新的数据显示到view
     */
    override fun onResume() {
        super.onResume()
        /*
         * 重新填充stockList数据:一旦从detailed_activity返回之后，有些数据已经保存
         * 在数据库中了，所以需要重新从数据库中读取并刷新UI
         */
        stockList.clear()
        stockList = databaseStock.queryAllData("StockData")

        /*
         * 获取layoutManager
         */
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        /*
         * 获取适配器
         */
        val adapter = StockDataAdapter(stockList)
        recyclerView.adapter = adapter

        threadRun = true

        /**
         *  线程每2s查询一次数据并发送消息进行UI更新
         */
        thread {
            while (threadRun){
                /* 处理删除item事件 */
                if (onLongClickFlag == true){
                    if (removeStockCode != ""){
                        /* 删除数据库中的对应数据，注意，这里不需要删除ArrayList里面的了，因为adapter中已经删除了 */
                        databaseStock.deleteData(removeStockCode)
                    }
                    onLongClickFlag = false
                    removeStockCode = ""
                    Thread.sleep(500)
                }
                for (i in 0 until stockList.size){
                    val targetData = stockList.get(i)
                    var url = "http://hq.sinajs.cn/list="
                    /*
                     * 从股票代码识别是上市还是深市
                     */
                    val shOrSz = when (targetData.stockCode[0]){
                        '6' -> "sh"
                        else -> "sz"
                    }
                    /* 拼组URL */
                    url = url + shOrSz + targetData.stockCode
                    /* 使用OkHttp进行网络数据请求 */
                    HttpUtils.sendOkHttpRequest(url, object : Callback{
                        override fun onResponse(call: Call, response: Response) {
                            /* 进行网络访问 */
                            val responseData = response.body?.string()
                            /* 解析数据:得到当前价格 */
                            val nowPrice = parseOkHttpStockDataForNowPrice(responseData)
                            /* 代入当前价格，计算出最新的类对象 */
                            val tmpData = StockData(targetData.stockCode, targetData.stockName,
                                nowPrice, targetData.ttmPERatio, targetData.perDividend, targetData.tenYearNationalDebt)
                            /* 将最新数据写入数据库 */
                            databaseStock.updateData(tmpData)
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

    override fun onStop() {
        super.onStop()
        /* 处于onStop生命周期时，将停止网络服务查询和UI更新 */
        threadRun = false
    }
    /**
     * 初始化stock整个list的数据
     */
    private fun initStockData(){

        stockList.add(
            StockData(
                "603658", "安图生物", 171.47,
                98.94, 0.9, 0.029
            )
        )
        stockList.add(
            StockData(
                "603288", "海天味业", 119.39,
                70.478, 0.9, 0.029
            )
        )
        stockList.add(
            StockData(
                "600519", "贵州茅台", 1439.84,
                41.986, 17.025, 0.029
            )
        )
        stockList.add(
            StockData(
                "002677", "浙江美大", 11.08,
                17.671, 0.542, 0.029
            )
        )
        stockList.add(
            StockData(
                "600566", "济川药业", 24.68,
                12.754, 1.23, 0.029
            )
        )
        stockList.add(
            StockData(
                "002372", "伟星新材", 12.07,
                20.217, 0.5, 0.029
            )
        )
        stockList.add(
            StockData(
                "603568", "伟明环保", 29.3,
                30.175, 0.31, 0.029
            )
        )
        stockList.add(
            StockData(
                "002508", "老板电器", 32.25,
                20.19, 0.5, 0.029
            )
        )
        stockList.add(
            StockData(
                "600167", "联美控股", 14.19,
                19.00, 0.21, 0.029
            )
        )
        stockList.add(
            StockData(
                "300015", "爱尔眼科", 46.30,
                160.76, 0.115, 0.029
            )
        )
        stockList.add(
            StockData(
                "002304", "洋河股份", 108.29,
                22.16, 3.00, 0.029
            )
        )
        stockList.add(
            StockData(
                "002117", "东港股份", 11.29,
                26.44, 0.4, 0.029
            )
        )
        stockList.add(
            StockData(
                "600660", "福耀玻璃", 21.45,
                19.55, 0.75, 0.029
            )
        )
        stockList.add(
            StockData(
                "603444", "吉比特", 453.90,
                31.96, 5.00, 0.029
            )
        )
        stockList.add(
            StockData(
                "603429", "集友股份", 31.70,
                73.55, 0.186, 0.029
            )
        )
        stockList.add(
            StockData(
                "603886", "元祖股份", 17.08,
                35.54, 1.2, 0.029
            )
        )
    }

    /**
     *  MainActivity的单实例，用于供外部类调用的static方法等
     */
    companion object{

        const val STARTDETAILACTIVITY = "startdetailactivity"
        const val HANDLELONGCLIECK = "handlelongclick"
        lateinit var  mainActivity : BaseActivity   //静态对象，用于适配器调用activity的相关操作

        /**
         * mainActivityTodo由外部类回调MainActivity操作
         * event：需要执行的操作
         * position：响应控件对应数据库的id
         */
        @JvmStatic
        fun mainActivityTodo(event: String, stockCode: String){
            when (event){
                STARTDETAILACTIVITY -> {
                    /*
                     * 通过MainAvtivity的静态对象调用相关的方法
                     */
                    val intent = Intent(mainActivity, DetailActivity::class.java)
                    /* 向活动传递数据：注意为Int型 */
                    intent.putExtra("stock_code", stockCode.toInt())
                    mainActivity.startActivity(intent) //开启活动
                    Log.d("MainActivity", "click stockCode:$stockCode")
                }

                HANDLELONGCLIECK -> {
                    onLongClickFlag = true
                    removeStockCode = stockCode
                    Log.d("MainActivity", "remove stockCode:$stockCode")
                }
            }
        }
    }
}
