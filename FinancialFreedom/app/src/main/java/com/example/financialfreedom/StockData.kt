package com.example.financialfreedom

/**
 * name:股票名称
 * nowPrice:当前价格
 * ttmPrice：TTM市盈率好价格
 * drcPrice：动态股息率好价格
 * detailes：详情
 */
class StockData(val name: String, val nowPrice: Double,
               val ttmPrice: Double, val drcPrice: Double, val detailes: String) {
}