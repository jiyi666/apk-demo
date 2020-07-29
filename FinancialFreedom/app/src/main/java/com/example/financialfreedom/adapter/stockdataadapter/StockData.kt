package com.example.financialfreedom.adapter.stockdataadapter

/**
 * stockCode:股票代码
 * stockName:股票简称
 * nowPrice:当前价格
 * ttmPERatio：当前TTM市盈率
 * perEarnings：每股收益
 * perDividend：每股股息
 * tenYearNationalDebt：十年期国债收益率
 * tenYearNationalDebtDevide3：三分之一十年期国债收益率
 * drcDividendRatio:动态股息率
 * ttmPrice：TTM市盈率好价格
 * drcPrice：动态股息率好价格
 * detailes：详情
 * finalPrice：最终好价格
 */
class StockData(val stockCode: String, val stockName: String, val nowPrice: Double,
                val ttmPERatio: Double, val perDividend: Double, val tenYearNationalDebt: Double){
    /* TTM市盈率好价格：15 * 每股收益(两位小数）*/
    var ttmPrice : String = String.format("%.2f", 15 * (nowPrice / ttmPERatio))
    /* 动态股息率好价格：每股股息 / 十年期国债收益率（两位小数）*/
    var drcPrice : String = String.format("%.2f", perDividend / tenYearNationalDebt)
    /* 每股收益：当前价格/当前TTM市盈率三位小数）*/
    val perEarnings : String = String.format("%.3f", nowPrice / ttmPERatio)
    /* 三分之一十年国债收益率：十年期国债收益率 / 3(五位小数) */
    val tenYearNationalDebtDavide3 : String = String.format("%.5f", tenYearNationalDebt / 3)
    /* 动态股息率：每股股息 / 当前价格（五位小数）*/
    val drcDividendRatio : String = String.format("%.5f", perDividend / nowPrice)
    val finalPrice : String
    val detailes : String = "详细>>"

    init {
        /*
         * 最终好价格：TTM市盈率好价格与动态股息率好价格二者取最小值 （两位小数）
         */
        finalPrice = if (ttmPrice > drcPrice) drcPrice else ttmPrice
    }

    override fun toString(): String {
        return "[stockCode(股票代码)：$stockCode, stockName(股票简称)：$stockName, " +
                "nowPrice(当前价格)：$nowPrice, ttmPERatio(当前TTM市盈率)：$ttmPERatio, " +
                "perEarnings(每股收益)：$perEarnings, perDividend(每股股息)：$perDividend, " +
                "tenYearNationalDebt(十年期国债收益率)：$tenYearNationalDebt, " +
                "tenYearNationalDebtDevide3(三分之一十年期国债收益率)：$tenYearNationalDebtDavide3, " +
                "drcDividendRatio(动态股息率)：$drcDividendRatio, ttmPrice(TTM市盈率好价格)：$ttmPrice, " +
                "drcPrice(动态股息率好价格)：$drcPrice, finalPrice(最终好价格)：$finalPrice"
    }
}
