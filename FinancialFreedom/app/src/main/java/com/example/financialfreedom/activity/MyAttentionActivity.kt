package com.example.financialfreedom.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.myattentionadapter.MyAttentionAdapter
import com.example.financialfreedom.adapter.myattentionadapter.RealTimeStock
import com.example.financialfreedom.database.myattention.MyAttentionBaseControl
import com.example.financialfreedom.internet.HttpUtils
import com.example.financialfreedom.internet.parseRealTimeStockData
import com.example.financialfreedom.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_my_attention.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.thread

/** 长按删除Flag */
internal var onLongClickFlag = false
/** 长按删除的股票代码 */
internal var removeStockCode = ""

/**
 * MyAttentionActivity设计思路及需要注意的地方：
 * 1.增加了线程暂停标志，当用户在输入股票进行查询时，线程将一直处于睡眠而不去进行网络请求，因为网络请求之后
 * 还会去更新UI，会导致整个界面崩溃；
 * 2.长按删除事件监听到之后，adapter会发送对应的股票代码到此活动，之所以选择发送股票代码，是因为发送position
 * 等值会导致在删除ArrayList和数据库值时大大增加操作难度，比如ArrayList的值已经在adapter中删除了，那么ArrayList
 * 此时的position对应的股票代码并不是数据库中对应position的股票代码，而是数据库中当前position的下一个；
 * 3.实现adapter和此活动的长按删除其共同操作的是ArrayList，每次添加和删除等操作都是先操作了ArrayList之后再去
 * 操作数据库，并且线程中每只股票的网络请求都不会从数据库中读取，而是ArrayList，总之，ArrayList总是adapter和
 * activity的首选操作目标；
 * TODO:1.添加和删除操作时，没有提供相同股票的识别功能；2.ArrayList没有进行判空操作，否则activity会崩溃；
 */
class MyAttentionActivity : BaseActivity() {

    private val tag = "MyAttentionActivity"
    /* 线程停止标志 */
    private var threadRun = true
    /* 线程暂停标志 */
    private var threadPause = false
    /* 启用ArrayList */
    private var realTimeStockList = ArrayList<RealTimeStock>()
    /* 启用数据库 */
    private val realTimeStockBase = MyAttentionBaseControl(this, "RealTimeStock", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_attention)

        myAttentionActivity = this

        /* 如果数据库不存在：创建 + 添加初始数据 */
        if (realTimeStockBase.queryAllData("RealTimeStock").size == 0){
            Log.d(tag, "create!")
            /* 创建数据库 */
            realTimeStockBase.create()
            /* 初始化stock数据 */
            initRealTimeStock()
            /* 添加数据 */
            for (i in 0 until (realTimeStockList.size)){
                realTimeStockBase.addData(realTimeStockList.get(index = i))
            }
        } else {
            /* 如果数据库存在，就从数据库中读取最新数据 */
            realTimeStockList.clear()
            realTimeStockList = realTimeStockBase.queryAllData("RealTimeStock")
        }
        Log.d(tag, "realTimeStockList size:${realTimeStockList.size}")
        /* 获取layoutManager */
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        /* 获取适配器 */
        val adapter = MyAttentionAdapter(realTimeStockList)
        recyclerView.adapter = adapter

        /* EditView触摸监听 */
        editView.setOnTouchListener { _, _ ->
            threadPause = true
            false
        }
        /* 按键监听 */
        addNewItem.setOnClickListener {
            val inputText = editView.text.toString()
            if (inputText != ""){
                var url = "http://hq.sinajs.cn/list="
                /* 从股票代码识别是上市还是深市 */
                val shOrSz = when (inputText[0]){
                    '6' -> "sh"
                    '5' -> "sh" //上市基金
                    else -> "sz"
                }
                /* 拼组URL */
                url = url + shOrSz + inputText
                /* 使用网络访问 */
                HttpUtils.sendOkHttpRequest(url, object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        /* 进行网络访问 */
                        val responseData = response.body?.string()
                        /* 解析数据 */
                        val tmpData = parseRealTimeStockData(inputText, responseData)
                        if (tmpData != null){
                            /* 将最新数据写入数据库和ArrayList */
                            realTimeStockList.add(tmpData)
                            realTimeStockBase.addData(tmpData)
                            threadPause = false
                        } else {
                            /* 在主线程进行吐司提醒 */
                            runOnUiThread {
                                takeToast("未查询到相关股票信息，请重新输入！")
                            }
                        }
                    }
                    override fun onFailure(call: Call, e: IOException) {
                    }
                })
            } else {
                Toast.makeText(this, "请输入有效代码！", Toast.LENGTH_LONG).show()
            }
        }

        thread {
            while (threadRun){
                if (threadPause){
                    Thread.sleep(2000)
                } else {
                    /* 处理删除item事件 */
                    if (onLongClickFlag == true){
                        if (removeStockCode != ""){
                            /* 删除数据库中的对应数据，注意，这里不需要删除ArrayList里面的了，因为
                             * adapter中已经删除了
                             */
                            realTimeStockBase.deleteData(removeStockCode)
                        }
                        onLongClickFlag = false
                        removeStockCode = ""
                        Thread.sleep(500)
                    }
                    Log.d(tag, "Now ArrayList size:${realTimeStockList.size}")
                    for (i in 0 until realTimeStockList.size){
                        /* 从ArrayList中读取需要查询的数据 */
                        val targetData = realTimeStockList.get(i)
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
                                /* 解析数据 */
                                val tmpData = parseRealTimeStockData(targetData.stockCode, responseData)
                                if (tmpData != null){
                                    /* 将最新数据写入数据库 */
                                    realTimeStockBase.updateData(tmpData)
                                    /* UI更新 */
                                    runOnUiThread {
                                        val realData = "nowPrice:" + tmpData.nowPrice +
                                                ",upAndDown:" + tmpData.upAndDown +
                                                ",upAndDownRate:" + tmpData.upAndDownRate
                                        adapter.notifyItemChanged(i, realData)
                                    }
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
    }

    override fun onStop() {
        super.onStop()
        /* 处于onStop生命周期时，将停止网络服务查询和UI更新 */
        threadRun = false
    }

    /* 初始化实时数据 */
    private fun initRealTimeStock(){
        realTimeStockList.add(RealTimeStock("159905", "深红利", 2.313,2.246))
        realTimeStockList.add(RealTimeStock("510880", "红利ETF", 2.313,2.246))
        realTimeStockList.add(RealTimeStock("510300", "300ETF", 2.313,2.246))
    }

    /* 用于UI更新时的吐司动作 */
    private fun takeToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    /**
     *  MainActivity的单实例，用于供外部类调用的static方法等
     */
    companion object{

        const val HANDLELONGCLIECK = "handlelongclick"
        lateinit var  myAttentionActivity : BaseActivity   //静态对象，用于适配器调用activity的相关操作

        /**
         * mainActivityTodo由外部类回调MainActivity操作
         * event：需要执行的操作
         * stockCode：响应控件对应数据库的id
         */
        @JvmStatic
        fun myAttentionActivityTodo(event: String, stockCode: String){
            when (event){
                HANDLELONGCLIECK -> {
                    onLongClickFlag = true
                    removeStockCode = stockCode
                    Log.d("MyAttentionActivity", "remove stockCode:$stockCode")
                }

            }
        }
    }
}
