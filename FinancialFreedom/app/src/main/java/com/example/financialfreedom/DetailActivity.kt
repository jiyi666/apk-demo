package com.example.financialfreedom

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
         * Note：对所有的数据进行判空输入处理，如果为空，则按照原来的数据进行计算
         */
        updateData.setOnClickListener {
            val nowPrice = if (detail_nowprice.text.toString() != "")
                detail_nowprice.text.toString().toDouble() else
                targetData!!.nowPrice
            val ttmPERatio = if (detail_ttmPERatio.text.toString() != "")
                detail_ttmPERatio.text.toString().toDouble() else
                targetData!!.ttmPERatio
            val tenYearNationalDebt = if (detail_tenYearNationalDebt.text.toString() != "")
                detail_tenYearNationalDebt.text.toString().toDouble() else
                targetData!!.tenYearNationalDebt
            val perDividend = if (detail_perDividend.text.toString() != "")
                detail_perDividend.text.toString().toDouble() else
                targetData!!.perDividend

            putDataToView(
                StockData(targetData!!.stockCode, targetData.stockName,
                    nowPrice, ttmPERatio, perDividend, tenYearNationalDebt)
            )

            /*
             * 隐藏所有EditView的光标
             */
            detail_nowprice.isCursorVisible = false
            detail_ttmPERatio.isCursorVisible = false
            detail_perDividend.isCursorVisible = false
            detail_tenYearNationalDebt.isCursorVisible = false
        }

        /*
         * 存储数据用户输入及最终计算数据
         * Note：对所有的数据进行判空输入处理，如果为空，则按照原来的数据进行计算
         */
        saveData.setOnClickListener {
            val nowPrice = if (detail_nowprice.text.toString() != "")
                detail_nowprice.text.toString().toDouble() else
                targetData!!.nowPrice
            val ttmPERatio = if (detail_ttmPERatio.text.toString() != "")
                detail_ttmPERatio.text.toString().toDouble() else
                targetData!!.ttmPERatio
            val tenYearNationalDebt = if (detail_tenYearNationalDebt.text.toString() != "")
                detail_tenYearNationalDebt.text.toString().toDouble() else
                targetData!!.tenYearNationalDebt
            val perDividend = if (detail_perDividend.text.toString() != "")
                detail_perDividend.text.toString().toDouble() else
                targetData!!.perDividend
            /*
             * 调用数据库进行数据更新
             */
            databaseControler.updateData(StockData(targetData!!.stockCode, targetData.stockName,
                nowPrice, ttmPERatio, perDividend, tenYearNationalDebt), position)
        }

        /*
         * nowprice触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_nowprice.setOnTouchListener { _, _ ->
            detail_nowprice.setText("")
            detail_nowprice.isCursorVisible = true
            false
        }

        /*
         * perDividend触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_perDividend.setOnTouchListener{ _, _ ->
            detail_perDividend.setText("")
            detail_perDividend.isCursorVisible = true
            false
        }

        /*
         * ttmPERatio触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_ttmPERatio.setOnTouchListener{ _, _ ->
            detail_ttmPERatio.setText("")
            detail_ttmPERatio.isCursorVisible = true
            false
        }

        /*
         * tenYearNationalDebt触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_tenYearNationalDebt.setOnTouchListener{ _, _ ->
            detail_tenYearNationalDebt.setText("")
            detail_tenYearNationalDebt.isCursorVisible = true
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy!")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume!")
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

        /*
         * 文本颜色设置:
         * 当前价格均大于TTM市盈率好价格和动态股息率好价格 -> 红色
         * 当前价格均小于TTM市盈率好价格和动态股息率好价格 -> 绿色
         * 当前价格介于TTM市盈率好价格和动态股息率好价格间 -> 蓝色
        */
        val color : Int
        if ((stockData.nowPrice > stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice > stockData.drcPrice.toDouble())){
            color = Color.RED
        } else if ((stockData.nowPrice < stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice < stockData.drcPrice.toDouble())){
            color = Color.GREEN
        } else {
            color = Color.BLUE
        }
        detail_nowprice1.setTextColor(color)
    }

}