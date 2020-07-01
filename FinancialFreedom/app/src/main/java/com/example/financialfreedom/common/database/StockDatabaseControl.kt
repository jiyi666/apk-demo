package com.example.financialfreedom.common.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.financialfreedom.StockData
import java.lang.NullPointerException

/**
 *  数据库封装类，用于其他activity调用以操作数据库
 */
class StockDatabaseControl(val context: Context, val name: String, val versin: Int) {

    private val tag = "DatabaseControler"
    private var dbHelper : MyDataBaseHelper

    init {
         dbHelper = MyDataBaseHelper(context, name, versin)
    }

    /*
     * 创建数据库
     */
    fun create(){
        dbHelper.writableDatabase
    }

    /*
     * 添加数据
     */
    fun addData(stockData: StockData){
        val db = dbHelper.writableDatabase

        /*
         * 这里写入数据库的操作没有进行编码，中文读出来就可能是乱码
         */
        val value = ContentValues().apply {
            put("stockCode", stockData.stockCode)
            put("stockName", stockData.stockName)
            put("nowPrice", stockData.nowPrice)
            put("ttmPERatio", stockData.ttmPERatio)
            put("perEarnings", stockData.perEarnings)
            put("perDividend", stockData.perDividend)
            put("tenYearNationalDebt", stockData.tenYearNationalDebt)
            put("tenYearNationalDebtDevide3", stockData.tenYearNationalDebtDevide3)
            put("drcDividendRatio", stockData.drcDividendRatio)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
            put("finalPrice", stockData.finalPrice)
        }
        db.insert(name, null, value)

    }

    /*
     * 查询数据：通过数据库的id索引对应的股票数据
     * position：目标id
     */
    fun queryData(name: String, position: Int) : StockData?{
        val db = dbHelper.writableDatabase
        /*
         * 数据库索引规则：全库搜索
         */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)
        /*
         * 数据库遍历
         */
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                if (position == id.toInt()){
                    val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                    val stockName = cursor.getString(cursor.getColumnIndex("stockName"))
                    val nowPrice = cursor.getString(cursor.getColumnIndex("nowPrice")).toDouble()
                    val ttmPERatio = cursor.getString(cursor.getColumnIndex("ttmPERatio")).toDouble()
                    val perDividend = cursor.getString(cursor.getColumnIndex("perDividend")).toDouble()
                    val tenYearNationalDebt = cursor.getString(cursor.getColumnIndex("tenYearNationalDebt")).toDouble()
                    cursor.close()
                    return StockData(stockCode, stockName, nowPrice, ttmPERatio, perDividend, tenYearNationalDebt)
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        /*
         * 如果未编译到目标数据，则返回null
         */
        return null
    }

    /*
     * 更新数据：通过数据库的id索引对应的股票数据
     * position：目标id
     */
    fun updateData(stockData: StockData, position: Int) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("stockCode", stockData.stockCode)
            put("stockName", stockData.stockName)
            put("nowPrice", stockData.nowPrice)
            put("ttmPERatio", stockData.ttmPERatio)
            put("perEarnings", stockData.perEarnings)
            put("perDividend", stockData.perDividend)
            put("tenYearNationalDebt", stockData.tenYearNationalDebt)
            put("tenYearNationalDebtDevide3", stockData.tenYearNationalDebtDevide3)
            put("drcDividendRatio", stockData.drcDividendRatio)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
            put("finalPrice", stockData.finalPrice)
        }
        db.update(name, value, "id = ?", arrayOf(position.toString()))
    }
}