package com.example.financialfreedom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.myattentionadapter.MyAttentionAdapter
import com.example.financialfreedom.adapter.myattentionadapter.RealTimeStock
import com.example.financialfreedom.common.database.myattention.MyAttentionBaseControl
import com.example.financialfreedom.common.internet.HttpUtils
import com.example.financialfreedom.common.internet.parseRealTimeStockData
import kotlinx.android.synthetic.main.activity_my_attention.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.thread

class MyAttentionActivity : AppCompatActivity() {

    /* 线程停止标志 */
    private var threadRun = true
    private val realTimeStockList = ArrayList<RealTimeStock>()
    /* 使用数据库 */
    val realTimeStockBase = MyAttentionBaseControl(this, "RealTimeStock", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_attention)

        /* 如果数据库不存在：创建 + 添加初始数据 */
        if (realTimeStockBase.queryData("RealTimeStock", 1) == null){
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
            for (i in 1..realTimeStockBase.getDataLengh()){
                val tmpData = realTimeStockBase.queryData("RealTimeStock", i)
                if (tmpData != null){
                    realTimeStockList.add(tmpData)
                }
            }
        }
        /* 获取layoutManager */
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        /* 获取适配器 */
        val adapter = MyAttentionAdapter(realTimeStockList)
        recyclerView.adapter = adapter

        thread {
            while (threadRun){
                var url = "http://hq.sinajs.cn/list="
                for (position in 1..realTimeStockBase.getDataLengh()){
                    /* 从数据库中读取需要查询的数据 */
                    val targetData = realTimeStockBase.queryData("RealTimeStock", position)
                    /* 从股票代码识别是上市还是深市 */
                    val shOrSz = when (targetData?.stockCode.toString()[0]){
                        '6' -> "sh"
                        else -> "sz"
                    }
                    /* 拼组URL */
                    url = url + shOrSz + targetData?.stockCode.toString()
                    /* 使用网络访问 */
                    HttpUtils.sendOkHttpRequest(url, object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            /* 进行网络访问 */
                            val responseData = response.body?.string()
                            /* 解析数据 */
                            val tmpData = parseRealTimeStockData(targetData!!.stockCode, responseData)
                            /* 将最新数据写入数据库 */
                            realTimeStockBase.updateData(tmpData, position)
                            /* UI更新 */
                            runOnUiThread {
                                val realData = "nowPrice:" + tmpData.nowPrice +
                                        ",upAndDown:" + tmpData.upAndDown +
                                        ",upAndDownRate:" + tmpData.upAndDownRate
                                adapter.notifyItemChanged(position - 1, realData)
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                        }
                    })
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

    /* 初始化实时数据 */
    private fun initRealTimeStock(){
        realTimeStockList.add(RealTimeStock("159905", "深红利", 2.313,2.246))
    }
}
