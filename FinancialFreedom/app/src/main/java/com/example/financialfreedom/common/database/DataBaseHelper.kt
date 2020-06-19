package com.example.financialfreedom.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DataBaseHelper(val context: Context, val name: String, val versin: Int) :
    SQLiteOpenHelper(context, name, null, versin) {
    private val createStockData = "create table stockData (" +
            "id integer primary key autoincrement, " +
            "股票名称 text," +
            "当前价格 real," +
            "TTM市盈率好价格 real," +
            "动态股息率好价格 real)"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createStockData)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}