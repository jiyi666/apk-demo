package com.example.financialfreedom

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private val stockList = ArrayList<StockData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //去掉标题栏的代码
        setContentView(R.layout.activity_main)

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
    }

    /**
     * 初始化stock整个list的数据
     */
    private fun initStockData(){
        stockList.add(StockData("安图生物", 149.84,
            26.00, 33.28, "详情>>"))
        stockList.add(StockData("海天味业", 116.41,
            25.41, 33.28, "详情>>"))
        stockList.add(StockData("贵州茅台", 1358.00,
            629.62, 1358.00, "详情>>"))
        stockList.add(StockData("浙江美大", 10.74,
            9.41, 20.04, "详情>>"))
        stockList.add(StockData("济川药业", 21.78,
            29.03, 45.49, "详情>>"))
        stockList.add(StockData("伟星新材", 12.70,
            8.96, 18.49, "详情>>"))
        stockList.add(StockData("伟明环保", 30.10,
            14.61, 11.46, "详情>>"))
        stockList.add(StockData("老板电器", 30.07,
            23.18, 18.49, "详情>>"))
        stockList.add(StockData("联美控股", 15.30,
            11.21, 7.77, "详情>>"))
        stockList.add(StockData("爱尔眼科", 50.02,
            5.61, 5.55, "详情>>"))
        stockList.add(StockData("洋河股份", 104.43,
            73.31, 110.95, "详情>>"))
        stockList.add(StockData("东港股份", 10.40,
            6.41, 14.79, "详情>>"))

        /*
        stockList.add(StockData("安图生物", 149.84))
        stockList.add(StockData("海天味业", 116.4))
        stockList.add(StockData("贵州茅台", 1358.00))
        stockList.add(StockData("浙江美大", 10.74))
        stockList.add(StockData("济川药业", 21.78))
        stockList.add(StockData("伟星新材", 12.70))
        stockList.add(StockData("伟明环保", 30.10))
        stockList.add(StockData("老板电器", 30.07))
        stockList.add(StockData("联美控股", 15.30))
        stockList.add(StockData("爱尔眼科", 50.02))
        stockList.add(StockData("洋河股份", 104.43))
        stockList.add(StockData("东港股份", 10.40))

         */
    }
}
