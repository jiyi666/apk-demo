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
         * 刷新数据到View
         */
        if (targetData != null){
            putDataToView(targetData)
        } else {
            /*
             * 如果是null，则送显一组魔鬼数据
             */
            putDataToView(StockData("?", "?", 0.00, 0.00, 0.00, 0.00))
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
        detail_stockcode.setText(stockData.stockCode)
        detail_stockname.setText(stockData.stockName)
        detail_nowprice.setText(stockData.nowPrice.toString())
        detail_ttmPERatio.setText(stockData.ttmPERatio.toString())
        detail_perEarnings.setText(stockData.perEarnings)
        detail_tenYearNationalDebt.setText(stockData.tenYearNationalDebt.toString())
        detail_tenYearNationalDebtDevide3.setText(stockData.tenYearNationalDebtDevide3.toString())
        detail_perDividend.setText(stockData.perDividend.toString())
        detail_drcDividendRatio.setText(stockData.drcDividendRatio)
        detail_ttmPrice.setText(stockData.ttmPrice)
        detail_drcPrice.setText(stockData.drcPrice)
        detail_finalPrice.setText(stockData.finalPrice)
        detail_nowprice1.setText(stockData.nowPrice.toString())
    }

}