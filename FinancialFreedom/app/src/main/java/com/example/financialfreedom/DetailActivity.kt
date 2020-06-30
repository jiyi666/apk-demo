package com.example.financialfreedom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.financialfreedom.common.database.StockDatabaseControl
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.detailed_data.*

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

        /*
         * 启动数据库搜索数据
         */
        val databaseControler = StockDatabaseControl(this, "StockData", 1)
        val targetData = databaseControler.queryData("StockData", position)

        /*
         * 显示数据库中存储数据
         */
        if (targetData != null){
            putDataToView(targetData)
        } else {
            /*
             * 如果是null，则送显一组魔鬼数据
             */
            putDataToView(StockData("?", "?", 0.00, 0.00, 0.00, 0.00))
        }

        /*
         * 刷新用户输入数据
         */
        updateData.setOnClickListener {
            val nowPrice = detail_nowprice.text.toString().toDouble()
            val ttmPERatio = detail_ttmPERatio.text.toString().toDouble()
            val tenYearNationalDebt = detail_tenYearNationalDebt.text.toString().toDouble()
            val perDividend = detail_perDividend.text.toString().toDouble()

            putDataToView(
                StockData(targetData!!.stockCode, targetData.stockName,
                    nowPrice, ttmPERatio, perDividend, tenYearNationalDebt)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy!")
    }

    /**
     * 将数据库读取出来的数据显示到view
     * stockData：待显示的股票信息
     */
    fun putDataToView(stockData: StockData){
        detail_stockcode.text = stockData.stockCode
        detail_stockname.text = stockData.stockName
        detail_nowprice.setText(stockData.nowPrice.toString())
        detail_ttmPERatio.setText(stockData.ttmPERatio.toString())
        detail_perEarnings.text = stockData.perEarnings
        detail_tenYearNationalDebt.setText(stockData.tenYearNationalDebt.toString())
        detail_tenYearNationalDebtDevide3.text = stockData.tenYearNationalDebtDevide3.toString()
        detail_perDividend.setText(stockData.perDividend.toString())
        detail_drcDividendRatio.text = stockData.drcDividendRatio
        detail_ttmPrice.text = stockData.ttmPrice
        detail_drcPrice.text = stockData.drcPrice
        detail_finalPrice.text = stockData.finalPrice
        detail_nowprice1.text = stockData.nowPrice.toString()
    }

}