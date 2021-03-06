package com.example.financialfreedom.activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.stockdataadapter.StockData
import com.example.financialfreedom.database.myoptional.MyOptionalBaseControl
import com.example.financialfreedom.internet.HttpUtils
import com.example.financialfreedom.internet.getSinaQueryUrl
import com.example.financialfreedom.internet.parseOkHttpStockDataForNowPrice
import com.example.financialfreedom.internet.parseOkHttpStockDataForStockName
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.myoptional_detailed_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MyOptionalDetailActivity : BaseActivity() {

    private val tag = "MyOptionalDetail"
    /* 消息集 */
    private val updateUi = 1
    /* 使用数据库 */
    private val stockbase = MyOptionalBaseControl(this, "MyOptionalStockData", 1)
    /* 需要存入数据库的数据 */
    var saveStockData = StockData("?", "?", 0.0, 0.0, 0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myoptional_detailed_data)

        /**
         *  消息处理：获取传递过来的数据并更新UI
         */
        val handler = @SuppressLint("HandlerLeak")
        object : Handler(){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    updateUi -> {
                        val bundle = msg.data
                        val stockCode = bundle.getString("stockCode")
                        val stockName = bundle.getString("stockName")
                        val nowPrice = bundle.getDouble("nowPrice")
                        val ttmPERatio = bundle.getDouble("ttmPERatio")
                        val perDividend = bundle.getDouble("perDividend")
                        val tenYearNationalDebt = bundle.getDouble("tenYearNationalDebt")

                        val nowStockData = StockData(stockCode.toString(), stockName.toString(), nowPrice,
                            ttmPERatio, perDividend, tenYearNationalDebt)
                        Log.d(tag, "new stockinfo:${nowStockData}")
                        putDataToView(nowStockData)
                        /* 暂存数据 */
                        saveStockData = nowStockData
                    }
                }
            }
        }

        /*
         * 刷新用户输入数据
         * Note：对所有的数据进行判空输入处理，如果有为空则吐司进行提醒
         */
        updateData.setOnClickListener {
            val stockCode = when (detail_stockcode.text.toString()){
                "" -> {
                    Toast.makeText(this, "请输入股票代码！", Toast.LENGTH_SHORT).show()
                    null
                }
                else -> detail_stockcode.text.toString()
            }
            val ttmPERatio = when (detail_ttmPERatio.text.toString()){
                "" -> {
                    Toast.makeText(this, "请输入TTM市盈率！", Toast.LENGTH_SHORT).show()
                    null
                }
                else -> detail_ttmPERatio.text.toString()
            }
            val perDividend = when (detail_perDividend.text.toString()){
                "" -> {
                    Toast.makeText(this, "请输入每股股息！", Toast.LENGTH_SHORT).show()
                    null
                }
                else -> detail_perDividend.text.toString()
            }
            val tenYearNationalDebt = when (detail_tenYearNationalDebt.text.toString()){
                "" -> {
                    Toast.makeText(this, "请输入十年期国债收益率！", Toast.LENGTH_SHORT).show()
                    null
                }
                else -> detail_tenYearNationalDebt.text.toString()
            }
            if ((stockCode != null) && (ttmPERatio != null) && (perDividend != null) && (tenYearNationalDebt != null)){
                /* 使用网络访问 */
                HttpUtils.sendOkHttpRequest(getSinaQueryUrl(stockCode), object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body?.string()
                        /* 解析数据 */
                        val stockName = parseOkHttpStockDataForStockName(responseData)
                        /* 能正确解析出股票简称说明输入的股票代码有效 */
                        if (stockName != null){
                            val nowPrice = parseOkHttpStockDataForNowPrice(responseData)
                            /* Msg中通过bundle携带数据 */
                            val msg = Message()
                            val bundle = Bundle()
                            bundle.putString("stockCode", stockCode)
                            bundle.putString("stockName", stockName)
                            bundle.putDouble("nowPrice", nowPrice)
                            bundle.putDouble("ttmPERatio", ttmPERatio.toString().toDouble())
                            bundle.putDouble("perDividend", perDividend.toString().toDouble())
                            bundle.putDouble("tenYearNationalDebt", tenYearNationalDebt.toString().toDouble())
                            msg.what = updateUi
                            msg.data = bundle
                            handler.sendMessage(msg)
                        } else {
                            runOnUiThread {
                                takeToast("没有查到相关股票信息，请重新输入！")
                            }
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                    }
                })
            }
        }

        /* 存储数据:数据有效（stockCode不为"?"）才保存，且不会连续保存进数据库 */
        saveData.setOnClickListener {
            if (saveStockData.stockCode != "?"){
                /* 若数据库中无该数据，则添加数据，否则更新数据 */
                saveStockData = if (stockbase.queryData(saveStockData.stockCode) == null){
                    stockbase.addData(saveStockData)
                    takeToast("数据保存成功！")
                    StockData("?", "?", 0.0, 0.0, 0.0, 0.0)
                } else {
                    stockbase.updateData(saveStockData)
                    takeToast("查询到数据库中有该股票信息，已更新成功！")
                    StockData("?", "?", 0.0, 0.0, 0.0, 0.0)
                }

            } else {
                takeToast("数据无效，不能保存！")
            }

        }

        /* MyOptionalActivity若是"添加"开启此活动，则什么也不做，若是通过股票代码开启此活动，则查询数据库并送显 */
        val targetStockCode = intent.getStringExtra("stock_code")
        if (targetStockCode != null){
            val stockData = stockbase.queryData(targetStockCode)
            if (stockData != null){
                putDataToView(stockData)
            }
        }


    }

    /* 用于UI更新时的吐司动作 */
    private fun takeToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    /**
     * 将数据显示到view
     * stockData：待显示的股票信息
     */
    fun putDataToView(stockData: StockData){
        detail_stockcode.setText(stockData.stockCode)
        detail_stockname.text = stockData.stockName
        detail_nowprice.text = stockData.nowPrice.toString()
        detail_ttmPERatio.setText(stockData.ttmPERatio.toString())
        detail_perEarnings.text = stockData.perEarnings
        detail_tenYearNationalDebt.setText(stockData.tenYearNationalDebt.toString())
        detail_tenYearNationalDebtDevide3.text = stockData.tenYearNationalDebtDavide3
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
        val color : Int = if ((stockData.nowPrice > stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice > stockData.drcPrice.toDouble())){
            Color.RED
        } else if ((stockData.nowPrice < stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice < stockData.drcPrice.toDouble())){
            Color.GREEN
        } else {
            Color.BLUE
        }
        detail_nowprice1.setTextColor(color)
    }

    /* 对外启动活动函数，附带信息为股票代码 */
    companion object{
        fun startActivity(context: Context, stockCode: String){
            val intent = Intent(context, MyOptionalDetailActivity::class.java).apply {
                putExtra("stock_code", stockCode)
            }
            context.startActivity(intent)
        }
    }
}
