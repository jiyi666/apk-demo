package com.example.financialfreedom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.common.database.StockDatabaseControl
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private val stockList = ArrayList<StockData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //去掉标题栏的代码
        setContentView(R.layout.activity_main)

        /*
         * 将当前活动赋值给静态对象
         */
        mainActivity = this

        /*
         * 初始化stock数据
         */
        initStockData()

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

        /*
         * 使用数据库
         */
        val databaseStock = StockDatabaseControl(this, "StockData", 1)
        databaseStock.create()  //创建数据库
        for (i in 0 until (stockList.size)){
            databaseStock.addData(stockList.get(index = i)) //添加数据
        }
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
        stockList.add(StockData("603866","元祖股份", 17.08,
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
