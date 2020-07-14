package com.example.financialfreedom

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.example.financialfreedom.common.database.StockDatabaseControl
import com.example.financialfreedom.utils.BaseActivity
import com.example.financialfreedom.utils.HttpUtils
import com.example.financialfreedom.utils.parseOkHttpStockData
import kotlinx.android.synthetic.main.detailed_data.*
import java.lang.Exception
import kotlin.concurrent.thread

/**
 *  具体数据展示页面的activity
 */
class DetailActivity : BaseActivity(){

    /* 类tag */
    val tag : String = "DetailActivity"
    /* 数据库中的位置 */
    var position : Int = -1
    /* UI更新标志:所有的EditView控件触发输入监听都将停止UI更新，直到保存数据之后才会更新 */
    var uiUpdateFlag = true
    /* 消息集 */
    val updateDataFromInternet = 1
    /* 线程停止标志 */
    private var threadRun = true

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

            /* 告知用户数据刷新成功 */
            Toast.makeText(this, "刷新成功！", Toast.LENGTH_SHORT).show()
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
            uiUpdateFlag = true
            /* 告知用户数据保存成功 */
            Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show()
        }

        /*
         * nowprice触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_nowprice.setOnTouchListener { _, _ ->
            detail_nowprice.setText("")
            detail_nowprice.isCursorVisible = true
            uiUpdateFlag = false
            false
        }

        /*
         * perDividend触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_perDividend.setOnTouchListener{ _, _ ->
            detail_perDividend.setText("")
            detail_perDividend.isCursorVisible = true
            uiUpdateFlag = false
            false
        }

        /*
         * ttmPERatio触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_ttmPERatio.setOnTouchListener{ _, _ ->
            detail_ttmPERatio.setText("")
            detail_ttmPERatio.isCursorVisible = true
            uiUpdateFlag = false
            false
        }

        /*
         * tenYearNationalDebt触摸事件监听：清空内容 + 隐藏光标
         * TODO:这个lambda表达式原理？
         */
        detail_tenYearNationalDebt.setOnTouchListener{ _, _ ->
            detail_tenYearNationalDebt.setText("")
            detail_tenYearNationalDebt.isCursorVisible = true
            uiUpdateFlag = false
            false
        }

        /**
         *  消息处理：获取传递过来的数据并更新UI
         */
        val handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    updateDataFromInternet -> {
                        val bundle = msg.data
                        val nowPrice = bundle.getDouble("nowPrice")
                        val nowStockData = StockData(targetData!!.stockCode, targetData.stockName,
                        nowPrice, targetData.ttmPERatio, targetData.perDividend, targetData.tenYearNationalDebt)

                        putDataToView(nowStockData)
                    }
                }
            }
        }

        /**
         *  线程每2s查询一次服务器数据，并使用消息机制更新UI
         *  TODO:暂不处理用户点击保存数据之后刷新会导致某些值还原的问题
         */
        thread {
            while (threadRun){
                try {
                    /*
                     * 从股票代码识别是上市还是深市
                     */
                    val shOrSz = when (targetData?.stockCode.toString()[0]){
                        '6' -> "sh"
                        else -> "sz"
                    }
                    /*
                     * 拼组URL
                     */
                    val url = "http://hq.sinajs.cn/list=" + shOrSz + targetData?.stockCode.toString()
                    /* 使用OkHttp进行网络数据请求 */
                    val responseData = HttpUtils.getInternetResponse(url)
                    if (responseData != null){
                        if (uiUpdateFlag == true){
                            /*
                             * Msg中通过bundle携带数据
                             */
                            val msg = Message()
                            val bundle = Bundle()
                            bundle.putDouble("nowPrice", parseOkHttpStockData(responseData))
                            msg.what = updateDataFromInternet
                            msg.data = bundle
                            handler.sendMessage(msg)
                        }

                    } else {
                        Log.d(tag, "Don't query stockData, Please ensure stockCode:${targetData?.stockCode}")
                    }

                } catch (e: Exception){
                    e.printStackTrace()
                }
                Thread.sleep(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /*
         * 停止线程
         */
        threadRun = false
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