package com.example.financialfreedom.adapter.myattentionadapter

/**
 * stockCode:股票代码
 * stockName:股票简称
 * nowPrice:当前价格
 * basicPrice:前一天收盘价
 * upAndDown:涨跌
 * upAndDownRate:涨幅
 */
class RealTimeStock(val stockCode: String, val stockName: String, val nowPrice: Double,
                    val basicPrice: Double) {

    val upAndDown: String
    val upAndDownRate : String

    init {
        upAndDown = String.format("%.3f", nowPrice - basicPrice)
        upAndDownRate = String.format("%.4f",  upAndDown.toDouble() / basicPrice)
    }

    override fun toString(): String {
        return "[stockCode(股票代码)：$stockCode, stockName(股票简称)：$stockName, " +
                "nowPrice(当前价格)：$nowPrice, basicPrice(基准价格)：$basicPrice, " +
                "upAndDown(涨幅): ${upAndDown.toDouble()}" +
                "upAndDownRate(涨幅率): ${upAndDownRate.toDouble() * 100.0}%"
    }
}