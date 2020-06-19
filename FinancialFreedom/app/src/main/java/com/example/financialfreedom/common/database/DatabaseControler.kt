package com.example.financialfreedom.common.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.financialfreedom.StockData
import java.lang.NullPointerException

class DatabaseControler(val context: Context, val name: String, val versin: Int) {

    lateinit var dbHelper: DataBaseHelper
    private val tag = "DatabaseControler"

    fun create(){
        dbHelper = DataBaseHelper(context, name, versin)
        dbHelper.writableDatabase
    }

    fun addData(stockData: StockData){
        val db = dbHelper.writableDatabase
        if (db == null){
            Log.e(tag, "dbHelper is null, please create database first!")
            throw NullPointerException()
        }
        val value = ContentValues().apply {
            put("股票名称", stockData.name)
            put("当前价格", stockData.nowPrice)
            put("TTM市盈率好价格", stockData.ttmPrice)
            put("动态股息率好价格", stockData.drcPrice)
        }
        db.insert("", null, value)
    }
}