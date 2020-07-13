package com.example.financialfreedom.utils

import com.example.financialfreedom.StockData
import okhttp3.*

/**
 * 顶层函数：服务器数据解析
 * param:服务器响应的原始数据
 * return：目前只需要返回当前价格
 */
fun parseOkHttpStockData(data: String?) : Double{
    /* 1.去掉数据中的双引号 */
    val responseData = data?.replace("\"", "")
    /* 2.通过识别等号来切割字符串，只需要使用切割后的第二个子串 */
    val splitTmp: List<String> = responseData!!.split("=")
    /* 3.通过识别逗号来切割整个子串 */
    val targetList: List<String> = splitTmp[1].split(",")

    /* 返回当前股价 */
    return targetList[3].toDouble()
}

/**
 * 顶层函数：获取服务器数据
 * param:数据库中的数据
 * return：服务器返回的应答
 * Todo:入参之所以传入数据库中的数据是因为如果传入的stockcode以0开头的话，就会
 * Todo:导致函数自动将6位股票代码变动为4位，比如浙江美大(002677)会变成2677
 */
fun getInternetResponse(stockData: StockData?) : String?{
    /*
     * 从股票代码识别是上市还是深市
     */
    val shOrSz = when (stockData?.stockCode.toString()[0]){
        '6' -> "sh"
        else -> "sz"
    }
    /*
     * 拼组URL
     */
    val url = "http://hq.sinajs.cn/list=" +
            shOrSz + stockData?.stockCode.toString()
    /*
     * 进行网络访问，得到服务器数据
     */
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    val response = client.newCall(request).execute()
    return response.body?.string()

}
