package com.example.financialfreedom

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.common.database.StockDatabaseControl
import com.example.financialfreedom.utils.BaseActivity
import com.example.financialfreedom.utils.getInternetResponse
import com.example.financialfreedom.utils.parseOkHttpStockData
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : BaseActivity() {

    private val tag = "MainActivity"
    private val stockList = ArrayList<StockData>()
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
        if (databaseStock.queryData("StockData", 1) == null){
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
        } else {
            /*
             * 如果数据库存在，就从数据库中读取最新数据
             */
            stockList.clear()
            for (i in 1..16){
                val tmpData = databaseStock.queryData("StockData", i)
                if (tmpData != null){
                    stockList.add(tmpData)
                }
            }
        }

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
        for (i in 1..16){
            val tmpData = databaseStock.queryData("StockData", i)
            if (tmpData != null){
                stockList.add(tmpData)
            }
        }

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
         *  消息处理：获取传递过来的数据并更新UI
         */
        val handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    updateDataFromInternet -> {
                        val bundle = msg.data
                        val position = bundle.getInt("position")
                        val nowPrice = bundle.getString("nowPrice")
                        val ttmPrice = bundle.getString("ttmPrice")
                        val drcPrice = bundle.getString("drcPrice")
                        val realData = "nowPrice:" + nowPrice +
                                ",ttmPrice:" + ttmPrice +
                                ",drcPrice:" + drcPrice
                        adapter.notifyItemChanged(position - 1, realData)
                    }
                }
            }
        }
        /**
         *  线程每2s查询一次数据并发送消息进行UI更新
         */
        thread {
            while (threadRun){
                for (position in 1..16){
                    /* 从数据库中读取需要查询的数据 */
                    val targetData = databaseStock.queryData("StockData", position)
                    /* 通过网络查询最新的数据 */
                    val tmpData = when (getInternetResponse(targetData)){
                        null -> StockData("?", "?", 0.00, 0.00, 0.00, 0.00)
                        else -> StockData(targetData!!.stockCode, targetData.stockName, parseOkHttpStockData(getInternetResponse(targetData)),
                            targetData.ttmPERatio, targetData.perDividend, targetData.tenYearNationalDebt)
                    }

                    /* 将最新数据写入数据库 */
                    databaseStock.updateData(tmpData, position)
                    /*
                     * Msg中通过bundle携带数据
                     */
                    val msg = Message()
                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    bundle.putString("nowPrice", tmpData.nowPrice.toString())
                    bundle.putString("ttmPrice", tmpData.ttmPrice)
                    bundle.putString("drcPrice", tmpData.drcPrice)
                    msg.what = updateDataFromInternet
                    msg.data = bundle
                    handler.sendMessage(msg)
                }
                Thread.sleep(2000)
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

        stockList.add(StockData("603658","安图生物", 171.47,
            98.94,0.9, 0.029))
        stockList.add(StockData("603288","海天味业", 119.39,
            70.478,0.9, 0.029))
        stockList.add(StockData("600519","贵州茅台", 1439.84,
            41.986,17.025, 0.029))
        stockList.add(StockData("002677","浙江美大", 11.08,
            17.671,0.542, 0.029))
        stockList.add(StockData("600566","济川药业", 24.68,
            12.754,1.23, 0.029))
        stockList.add(StockData("002372","伟星新材", 12.07,
            20.217,0.5, 0.029))
        stockList.add(StockData("603568","伟明环保", 29.3,
            30.175,0.31, 0.029))
        stockList.add(StockData("002508","老板电器", 32.25,
            20.19,0.5, 0.029))
        stockList.add(StockData("600167","联美控股", 14.19,
            19.00,0.21, 0.029))
        stockList.add(StockData("300015","爱尔眼科", 46.30,
            160.76,0.115, 0.029))
        stockList.add(StockData("002304","洋河股份", 108.29,
            22.16,3.00, 0.029))
        stockList.add(StockData("002117","东港股份", 11.29,
            26.44,0.4, 0.029))
        stockList.add(StockData("600660","福耀玻璃", 21.45,
            19.55,0.75, 0.029))
        stockList.add(StockData("603444","吉比特", 453.90,
            31.96,5.00, 0.029))
        stockList.add(StockData("603429","集友股份", 31.70,
            73.55,0.186, 0.029))
        stockList.add(StockData("603886","元祖股份", 17.08,
            35.54,1.2, 0.029))
    }

    /**
     *  MainActivity的单实例，用于供外部类调用的static方法等
     */
    companion object{

        const val STARTDETAILACTIVITY = "startdetailactivity"
        const val INPUTPRICE          = "inputprice"
        lateinit var  mainActivity : BaseActivity   //静态对象，用于适配器调用activity的相关操作

        /**
         * mainActivityTodo由外部类回调MainActivity操作
         * event：需要执行的操作
         * position：响应控件对应数据库的id
         */
        @JvmStatic
        fun mainActivityTodo(event: String, position: Int){
            when (event){
                STARTDETAILACTIVITY -> {
                    /*
                     * 通过MainAvtivity的静态对象调用相关的方法
                     */
                    val intent = Intent(mainActivity, DetailActivity::class.java)
                    intent.putExtra("database_position", position)  //向活动传递数据
                    mainActivity.startActivity(intent) //开启活动
                }

                INPUTPRICE -> {
                }
            }
        }
    }
}
