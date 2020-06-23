package com.example.financialfreedom.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 *  启动SQLite数据库
 */
class DataBaseHelper(val context: Context, val name: String, val versin: Int) :
    SQLiteOpenHelper(context, name, null, versin) {
    private val createStockData = "create table $name (" +
            "id integer primary key autoincrement, " +
            "stockName text," +
            "nowPrice real," +
            "ttmPrice real," +
            "drcPrice real)"


    /*
     * 创建数据库
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createStockData)
        Toast.makeText(context, "Create $name succeeded", Toast.LENGTH_LONG).show()
    }

    /*
     * 数据库升级
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Toast.makeText(context, "upgrade $name succeeded", Toast.LENGTH_LONG).show()
        db.execSQL("drop table if exists $name")
        onCreate(db)
    }
}