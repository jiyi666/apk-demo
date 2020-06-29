package com.example.financialfreedom.common.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.financialfreedom.StockData
import java.lang.NullPointerException

/**
 *  数据库封装类，用于其他activity调用以操作数据库
 */
class DatabaseControler(val context: Context, val name: String, val versin: Int) {

    lateinit var dbHelper: DataBaseHelper
    private val tag = "DatabaseControler"

    /*
     * 创建数据库
     */
    fun create(){
        dbHelper = DataBaseHelper(context, name, versin)
        dbHelper.writableDatabase
    }

    /*
     * 添加数据
     */
    fun addData(stockData: StockData){
        val db = dbHelper.writableDatabase

        /* 因为dbHelper是lateinit变量，所以可能会出现未初始化的情况，故需要做判空处理 */
        if (db == null){
            Log.e(tag, "dbHelper is null, please create database first!")
            throw NullPointerException()
        }

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

    fun queryData(name: String, position: Int) : StockData?{
        Log.d(tag, "remark0!")
        val db = dbHelper.writableDatabase
        Log.d(tag, "remark1!")
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)
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
        return StockData("?", "?", 0.00, 0.00, 0.00, 0.00)
    }
}