package com.example.financialfreedom.database.stockdata

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.financialfreedom.adapter.stockdataadapter.StockData

/**
 *  数据库封装类，用于其他activity调用以操作数据库
 */
class StockDatabaseControl(val context: Context, val name: String, val versin: Int) {

    private var dbHelper : MyDataBaseHelper = MyDataBaseHelper(context, name, versin)

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
            put("tenYearNationalDebtDevide3", stockData.tenYearNationalDebtDavide3)
            put("drcDividendRatio", stockData.drcDividendRatio)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
            put("finalPrice", stockData.finalPrice)
        }
        db.insert(name, null, value)

    }

    /*
     * 查询数据：通过股票代码索引对应的股票数据
     */
    fun queryData(targetStockCode: String) : StockData? {
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
        if (cursor.moveToFirst()) {
            do {
                val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                val stockName = cursor.getString(cursor.getColumnIndex("stockName"))
                val nowPrice = cursor.getString(cursor.getColumnIndex("nowPrice")).toDouble()
                val ttmPERatio = cursor.getString(cursor.getColumnIndex("ttmPERatio")).toDouble()
                val perDividend = cursor.getString(cursor.getColumnIndex("perDividend")).toDouble()
                val tenYearNationalDebt = cursor.getString(cursor.getColumnIndex("tenYearNationalDebt")).toDouble()

                /* 搜索到对应stockcode */
                if (targetStockCode == stockCode) {
                    cursor.close()
                    return StockData(stockCode, stockName, nowPrice, ttmPERatio, perDividend, tenYearNationalDebt
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        /*
         * 如果未遍历到目标数据，则返回null
         */
        return null
    }

    /*
     * 遍历全局数据库
     */
    fun queryAllData(name: String) : ArrayList<StockData>{
        val db = dbHelper.writableDatabase
        val dataList = ArrayList<StockData>()

        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)

        /* 遍历数据库 */
        if (cursor.moveToFirst()){
            do {
                val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                val stockName = cursor.getString(cursor.getColumnIndex("stockName"))
                val nowPrice = cursor.getString(cursor.getColumnIndex("nowPrice")).toDouble()
                val ttmPERatio = cursor.getString(cursor.getColumnIndex("ttmPERatio")).toDouble()
                val perDividend = cursor.getString(cursor.getColumnIndex("perDividend")).toDouble()
                val tenYearNationalDebt = cursor.getString(cursor.getColumnIndex("tenYearNationalDebt")).toDouble()
                dataList.add(StockData(stockCode, stockName, nowPrice, ttmPERatio, perDividend, tenYearNationalDebt))
            } while (cursor.moveToNext())
            cursor.close()
            return dataList
        }
        cursor.close()
        /* 如果未遍历到目标数据，则返回null */
        return dataList
    }

    /*
     * 更新数据：通过数据库的id索引对应的股票数据:附加更新所有列的国债收益率
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
            put("tenYearNationalDebtDevide3", stockData.tenYearNationalDebtDavide3)
            put("drcDividendRatio", stockData.drcDividendRatio)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
            put("finalPrice", stockData.finalPrice)
        }
        db.update(name, value, "id = ?", arrayOf(position.toString()))
    }

    /*
     * 更新数据：检索依据为股票代码(唯一)
     */
    fun updateData(stockData: StockData) {
        val db = dbHelper.writableDatabase

        val value = ContentValues().apply {
            put("stockCode", stockData.stockCode)
            put("stockName", stockData.stockName)
            put("nowPrice", stockData.nowPrice)
            put("ttmPERatio", stockData.ttmPERatio)
            put("perEarnings", stockData.perEarnings)
            put("perDividend", stockData.perDividend)
            put("tenYearNationalDebt", stockData.tenYearNationalDebt)
            put("tenYearNationalDebtDevide3", stockData.tenYearNationalDebtDavide3)
            put("drcDividendRatio", stockData.drcDividendRatio)
            put("ttmPrice", stockData.ttmPrice)
            put("drcPrice", stockData.drcPrice)
            put("finalPrice", stockData.finalPrice)
        }
        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)
        /* 遍历数据库 */
        if (cursor.moveToFirst()){
            do {
                val stockCode = cursor.getString(cursor.getColumnIndex("stockCode"))
                if (stockCode == stockData.stockCode){
                    db.update(name, value, "stockCode = ?", arrayOf(stockCode))
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        /* 更新所有列的十年期国债收益率 */
        val value1 = ContentValues().apply {
            put("tenYearNationalDebt", stockData.tenYearNationalDebt)
        }
        db.update(name, value1, "", null)
    }

    /* 获取数据库总数据条数 */
    @SuppressLint("Recycle")
    fun getDataLengh() : Int{
        val db = dbHelper.writableDatabase
        /* 全局搜索 */
        val cursor = db.query(name, null,
            null, null, null,
            null, null, null)

        return cursor.count
    }

    /* 删除数据:根据股票代码来删除 */
    fun deleteData(stockCode: String){
        val db = dbHelper.writableDatabase
        db.delete(name, "stockCode = ?", arrayOf(stockCode))
    }
}