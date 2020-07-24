package com.example.financialfreedom.common.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class QueryStockService : Service() {

    private val mBinder = QueryBinder()

    class QueryBinder : Binder(){
        fun getStockData(){

        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }
}