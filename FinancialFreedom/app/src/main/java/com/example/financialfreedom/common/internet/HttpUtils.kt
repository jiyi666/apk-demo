package com.example.financialfreedom.common.internet

import com.example.financialfreedom.adapter.myattentionadapter.RealTimeStock
import okhttp3.*

/**
 * 顶层函数：服务器数据解析
 * param0->data:服务器响应的原始数据
 * param1->positon:需要获取的股票价格，这里使用默认参数方便单股查询
 * return：目前只需要返回当前价格
 */
fun parseOkHttpStockData(data: String?, position: Int = 0) : Double{
    /* 1.去掉数据中的"双引号" */
    val responseData = data?.replace("\"", "")
    /* 2.通过识别";"来切割字符串，拿到每一股的完整数据 */
    val perStockData: List<String> = responseData!!.split(";")
    /* 3.通过识别"="来切割字符串 */
    val splitTmp: List<String> = perStockData[position].split("=")
    /* 4.将上文"="右边的子串再切割，通过识别","来切割整个子串 */
    val targetList: List<String> = splitTmp[1].split(",")

    /* 返回当前股价 */
    return targetList[3].toDouble()
}

/**
 * 顶层函数：服务器数据解析
 * param0->data:股票代码简称，用于返回实例化对象
 * param1->data:服务器响应的原始数据
 * param2->positon:需要获取的股票价格，这里使用默认参数方便单股查询
 * return：目前只需要返回当前价格
 */
fun parseRealTimeStockData(stockCode: String, data: String?, position: Int = 0) : RealTimeStock{
    /* 1.去掉数据中的"双引号" */
    val responseData = data?.replace("\"", "")
    /* 2.通过识别";"来切割字符串，拿到每一股的完整数据 */
    val perStockData: List<String> = responseData!!.split(";")
    /* 3.通过识别"="来切割字符串 */
    val splitTmp: List<String> = perStockData[position].split("=")
    /* 4.将上文"="右边的子串再切割，通过识别","来切割整个子串 */
    val targetList: List<String> = splitTmp[1].split(",")

    /* 返回当前RealTimeStock实例化对象  */
    return RealTimeStock(stockCode, targetList[0], targetList[3].toDouble(), targetList[2].toDouble())
}

/**
 * 顶层函数：获取服务器数据
 * param:数据库中的数据
 * return：服务器返回的应答
 * Todo:入参之所以传入数据库中的数据是因为如果传入的stockcode以0开头的话，就会
 * Todo:导致函数自动将6位股票代码变动为4位，比如浙江美大(002677)会变成2677
 */
object HttpUtils{
    private val client = OkHttpClient()

    /**
     *  同步方式
     */
    fun getInternetResponse(url: String) : String?{
        /*
         * 进行网络访问，得到服务器数据
         */
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }
    /**
     *  异步方式
     */
    fun sendOkHttpRequest(url: String, callback: okhttp3.Callback){
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(callback)
    }
}

