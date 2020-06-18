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

/*
class StockData(val name: String, val nowPrice: Double){
    var ttmPrice : String
    var drcPrice : String
    val detailes : String = "更多>>"
    val nowTTM : Double = 1.30
    val perDividend : Double = 0.02704
    val tenYearNationalDebt : Double = 1.00

    init {
        ttmPrice = String.format("%.2f", 15 * (nowPrice / nowTTM))
        drcPrice = String.format("%.2f", perDividend / tenYearNationalDebt)
    }
}

 */