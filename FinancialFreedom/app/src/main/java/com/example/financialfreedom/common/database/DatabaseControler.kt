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
            put("stockName", stockData.name)
            put("nowPrice", stockData.nowPrice)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
        }
        db.insert(name, null, value)

    }
}