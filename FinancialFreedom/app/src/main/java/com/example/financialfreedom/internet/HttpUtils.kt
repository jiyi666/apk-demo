package com.example.financialfreedom.internet

import com.example.financialfreedom.adapter.myattentionadapter.RealTimeStock
import okhttp3.*

/**
 * 顶层函数：获取新浪服务器访问的URL
 * param0->stockCode:需要访问的股票代码
 * return：待访问的完整URL
 */
fun getSinaQueryUrl(stockCode: String): String{

    val url = "http://hq.sinajs.cn/list="
    /*
     * 从股票代码识别是上市还是深市
     */
    val shOrSz = when (stockCode[0]){
        '6' -> "sh"
        '5' -> "sh"
        else -> "sz"
    }
    /* 拼组URL */
    return url + shOrSz + stockCode
}

/**
 * 顶层函数：服务器数据解析
 * param0->data:服务器响应的原始数据
 * param1->positon:需要获取的股票价格，这里使用默认参数方便单股查询
 * return：目前只需要返回当前价格
 */
fun parseOkHttpStockDataForNowPrice(data: String?, position: Int = 0) : Double{
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
 * param0->data:服务器响应的原始数据
 * param1->positon:需要获取的股票简称，这里使用默认参数方便单股查询
 * return：返回当前股票简称
 */
fun parseOkHttpStockDataForStockName(data: String?, position: Int = 0) : String?{
    /* 1.去掉数据中的"双引号" */
    val responseData = data?.replace("\"", "")
    /* 2.通过识别";"来切割字符串，拿到每一股的完整数据 */
    val perStockData: List<String> = responseData!!.split(";")
    /* 3.通过识别"="来切割字符串 */
    val splitTmp: List<String> = perStockData[position].split("=")
    if (splitTmp[1] == ""){
        return null
    }
    /* 4.将上文"="右边的子串再切割，通过识别","来切割整个子串 */
    val targetList: List<String> = splitTmp[1].split(",")

    /* 返回股票简称 */
    return targetList[0]
}

/**
 * 顶层函数：服务器数据解析
 * param0->data:股票代码简称，用于返回实例化对象
 * param1->data:服务器响应的原始数据
 * param2->positon:需要获取的股票价格，这里使用默认参数方便单股查询
 * return：目前只需要返回当前价格
 */
fun parseRealTimeStockData(stockCode: String, data: String?, position: Int = 0) : RealTimeStock?{
    /* 1.去掉数据中的"双引号" */
    val responseData = data?.replace("\"", "")
    /* 2.通过识别";"来切割字符串，拿到每一股的完整数据 */
    val perStockData: List<String> = responseData!!.split(";")
    /* 3.通过识别"="来切割字符串 */
    val splitTmp: List<String> = perStockData[position].split("=")
    /* 判断：如果"="右边没有数据，说明股票代码深市或者沪市弄错或者不存在该股票 */
    if (splitTmp[1] == ""){
        return null
    }
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
    fun sendOkHttpRequest(url: String, callback: Callback){
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(callback)
    }
}

