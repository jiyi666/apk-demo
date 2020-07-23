package com.example.financialfreedom.common.database.myattention

import android.content.ContentValues
import android.content.Context
import com.example.financialfreedom.adapter.myattentionadapter.RealTimeStock


/**
 * 给MyAttentionActivity使用
 */
class MyAttentionBaseControl(val context: Context, val name: String, val versin: Int) {
    private var dbHelper : MyAttentionBaseHelper

    init {
        dbHelper =
            MyAttentionBaseHelper(context, name, versin)
    }

    /* 创建数据库 */
    fun create(){
        dbHelper.writableDatabase
    }

    /* 添加数据 */
    fun addData(realTimeStock: RealTimeStock){
        val db = dbHelper.writableDatabase

        val value = ContentValues().apply {
            put("stockCode", realTimeStock.stockCode)
            put("stockName", realTimeStock.stockName)
            put("nowPrice", realTimeStock.nowPrice)
            put("basicPrice", realTimeStock.basicPrice)
            put("upAndDown", realTimeStock.upAndDown)
            put("upAndDownRate", realTimeStock.upAndDownRate)
        }
        db.insert(name, null, value)

    }

    /*
     * 查询数据：通过数据库的id索引对应的股票数据
     * position：目标id
     */
    fun queryData(name: String, position: Int) : RealTimeStock?{
        val db = dbHelper.writableDatabase

        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)

        /* 遍历数据库 */
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                if (position == id.toInt()){
                    val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                    val stockName = cursor.getString(cursor.getColumnIndex("stockName"))
                    val nowPrice = cursor.getString(cursor.getColumnIndex("nowPrice"))
                    val basicPrice = cursor.getString(cursor.getColumnIndex("basicPrice"))
                    cursor.close()
                    return RealTimeStock(stockCode, stockName, nowPrice.toDouble(), basicPrice.toDouble())
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        /* 如果未遍历到目标数据，则返回null */
        return null
    }

    /*
     * 编译全局数据库
     */
    fun queryAllData(name: String) : ArrayList<RealTimeStock>{
        val db = dbHelper.writableDatabase
        val dataList = ArrayList<RealTimeStock>()

        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)

        /* 遍历数据库 */
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                    val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                    val stockName = cursor.getString(cursor.getColumnIndex("stockName"))
                    val nowPrice = cursor.getString(cursor.getColumnIndex("nowPrice"))
                    val basicPrice = cursor.getString(cursor.getColumnIndex("basicPrice"))
                    dataList.add(RealTimeStock(stockCode, stockName, nowPrice.toDouble(), basicPrice.toDouble()))
            } while (cursor.moveToNext())
            cursor.close()
            return dataList
        }
        cursor.close()
        /* 如果未遍历到目标数据，则返回null */
        return dataList
    }

    /*
     * 更新数据：通过数据库的id索引对应的股票数据
     */
    fun updateData(realTimeStock: RealTimeStock) {
        val db = dbHelper.writableDatabase

        val value = ContentValues().apply {
            put("stockCode", realTimeStock.stockCode)
            put("stockName", realTimeStock.stockName)
            put("nowPrice", realTimeStock.nowPrice)
            put("basicPrice", realTimeStock.basicPrice)
            put("upAndDown", realTimeStock.upAndDown)
            put("upAndDownRate", realTimeStock.upAndDownRate)
        }
        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)
        /* 遍历数据库 */
        if (cursor.moveToFirst()){
            do {
                val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                if (stockCode == realTimeStock.stockCode){
                    db.update(name, value, "stockCode = ?", arrayOf(stockCode))
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    /* 删除数据:根据股票代码来删除 */
    fun deleteData(stockCode: String){
        val db = dbHelper.writableDatabase
        db.delete(name, "stockCode = ?", arrayOf(stockCode))
    }

    /* 获取数据库总数据条数 */
    fun getDataLengh() : Int{
        val db = dbHelper.writableDatabase
        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)

        return cursor.count
    }
}