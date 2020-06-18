package com.example.financialfreedom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** RecyclerView的适配器 */
class StockDataAdapter(val stockdataList : List<StockData>) :
    RecyclerView.Adapter <StockDataAdapter.ViewHolder>() {

    /*
     * 用于获取最外层布局的及控件的实例
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val stockName : TextView = view.findViewById(R.id.stockName)
        val stockNowPrice : EditText = view.findViewById(R.id.stockNowPrice)
        val stockTtmPrice : EditText = view.findViewById(R.id.stockTtmPrice)
        val stockDrcPrice : EditText = view.findViewById(R.id.stockDrcPrice)
        val stockDetails : TextView = view.findViewById(R.id.stockDetails)
    }

    /*
     * 加载main_data布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.main_data, parent, false)

        return ViewHolder(view)
    }

    /*
     * 对RecyclerView滚入屏幕的子项数据赋值
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stockData = stockdataList[position]
        holder.stockName.text = stockData.name
        holder.stockNowPrice.setText(stockData.nowPrice.toString())
        holder.stockTtmPrice.setText(stockData.ttmPrice.toString())
        holder.stockDrcPrice.setText(stockData.drcPrice.toString())
        holder.stockDetails.text = stockData.detailes
    }

    /*
     * 返回数据源长度
     */
    override fun getItemCount() = stockdataList.size
}