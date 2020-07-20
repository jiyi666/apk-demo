package com.example.financialfreedom.adapter.myattentionadapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialfreedom.R

class MyAttentionAdapter (val realTimeStockList : List<RealTimeStock>) :
    RecyclerView.Adapter <MyAttentionAdapter.ViewHolder>(){

    /* 用于获取最外层布局的及控件的实例 */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val stockName: TextView = view.findViewById(R.id.stockName)
        val stockNowPrice: TextView = view.findViewById(R.id.stockNowPrice)
        val upAndDown: TextView = view.findViewById(R.id.upAndDown)
        val upAndDownRate: TextView = view.findViewById(R.id.upAndDownRate)
    }

    /* 加载my_attention布局 */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAttentionAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_attention, parent, false)

        val viewHolder = ViewHolder(view)

        return viewHolder //注意这里要返回viewHolder，因为有各种点击事件
    }

    /**
     * 调用adapter的notifyItemChanged时会调用此函数，用于更新局部控件
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()){
            onBindViewHolder(holder, position)
        } else {
            /* payloads的size恒为1 */
            val str = payloads.get(index = 0)

            val priceList: List<String> = str.toString().split(",")
            val nowPrice = priceList[0].split(":")[1]
            val upAndDown = priceList[1].split(":")[1]
            val upAndDownRate = priceList[2].split(":")[1]
            /*
             * 文本颜色设置:
             * 涨跌大于0 -> 红色
             * 涨跌等于0 -> 灰色
             * 涨跌小于0 -> 绿色
             */
            val color: Int
            if (upAndDown.toDouble() > 0){
                color = Color.RED
            } else if (upAndDown.toDouble() == 0.00){
                color = Color.GRAY
            } else {
                color = Color.GREEN
            }
            holder.upAndDown.setTextColor(color)
            holder.upAndDownRate.setTextColor(color)

            /* 文本更新 */
            holder.stockNowPrice.text = nowPrice
            holder.upAndDown.text = upAndDown
            holder.upAndDownRate.text = (upAndDownRate.toDouble() * 100).toString() + "%"
        }
    }

    /* 对RecyclerView滚入屏幕的子项数据赋值 */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realTimeStock = realTimeStockList[position]
        holder.stockName.text = realTimeStock.stockName
        holder.stockNowPrice.text = realTimeStock.nowPrice.toString()
        holder.upAndDown.text = realTimeStock.upAndDown
        holder.upAndDownRate.text = (realTimeStock.upAndDownRate.toDouble() * 100).toString() + "%"

        /*
         * 文本颜色设置:
         * 涨跌大于0 -> 红色
         * 涨跌等于0 -> 灰色
         * 涨跌小于0 -> 绿色
         */
        val color: Int
        if (realTimeStock.upAndDown.toDouble() > 0){
            color = Color.RED
        } else if (realTimeStock.upAndDown.toDouble() == 0.00){
            color = Color.GRAY
        } else {
            color = Color.GREEN
        }
        holder.upAndDown.setTextColor(color)
        holder.upAndDownRate.setTextColor(color)

    }

    /* 返回数据源长度 */
    override fun getItemCount() = realTimeStockList.size
}