package com.example.financialfreedom.adapter.myoptionaladapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialfreedom.R
import com.example.financialfreedom.adapter.stockdataadapter.StockData

class MyOptionalAdapter(list: ArrayList<StockData>) :
    RecyclerView.Adapter <MyOptionalAdapter.ViewHolder>() {
    var stockDataList = ArrayList<StockData>()

    init {
        stockDataList = list
    }

    /* 用于获取最外层布局的及控件的实例 */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val stockName : TextView = view.findViewById(R.id.stockName)
        val stockNowPrice : TextView = view.findViewById(R.id.stockNowPrice)
        val stockTtmPrice : TextView = view.findViewById(R.id.stockTtmPrice)
        val stockDrcPrice : TextView = view.findViewById(R.id.stockDrcPrice)
        val stockDetails : TextView = view.findViewById(R.id.stockDetails)
    }

    /*
     * 加载main_data布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOptionalAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_data, parent, false)

        val viewHolder = ViewHolder(view)


        return viewHolder //注意这里要返回viewHolder，因为有各种点击事件
    }

    /*
     * 对RecyclerView滚入屏幕的子项数据赋值
     */
    override fun onBindViewHolder(holder: MyOptionalAdapter.ViewHolder, position: Int) {
        val stockData = stockDataList[position]
        holder.stockName.text = stockData.stockName
        holder.stockNowPrice.setText(stockData.nowPrice.toString())
        holder.stockTtmPrice.setText(stockData.ttmPrice.toString())
        holder.stockDrcPrice.setText(stockData.drcPrice.toString())
        holder.stockDetails.text = stockData.detailes

        /*
         * 文本颜色设置:
         * 当前价格均大于TTM市盈率好价格和动态股息率好价格 -> 红色
         * 当前价格均小于TTM市盈率好价格和动态股息率好价格 -> 绿色
         * 当前价格介于TTM市盈率好价格和动态股息率好价格间 -> 蓝色
         */
        val color : Int
        if ((stockData.nowPrice > stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice > stockData.drcPrice.toDouble())){
            color = Color.RED
        } else if ((stockData.nowPrice < stockData.ttmPrice.toDouble()) &&
            (stockData.nowPrice < stockData.drcPrice.toDouble())){
            color = Color.GREEN
        } else {
            color = Color.BLUE
        }
        holder.stockNowPrice.setTextColor(color)

    }

    /* 返回数据源长度 */
    override fun getItemCount() = stockDataList.size
}