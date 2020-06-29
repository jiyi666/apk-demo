package com.example.financialfreedom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.financialfreedom.MainActivity.Companion.STARTDETAILACTIVITY

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

        val viewHolder = ViewHolder(view)
        /*
         * 最外层布局空白处点击事件
         */
        viewHolder.itemView.setOnClickListener{
            Toast.makeText(parent.context, "you click what?",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * TextView：股票名字点击事件
         */
        viewHolder.stockName.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.stockName}",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * EditText：股票当前价格点击事件
         */
        viewHolder.stockNowPrice.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.nowPrice}",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * EditText：股票TTM好价格点击事件
         */
        viewHolder.stockTtmPrice.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.ttmPrice}",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * EditText：股票动态股息率好价格点击事件
         */
        viewHolder.stockDrcPrice.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.drcPrice}",
                Toast.LENGTH_SHORT).show()
        }

        /*
        * TextView：股票详情页面点击事件
        */
        viewHolder.stockDetails.setOnClickListener{
            MainActivity.mainActivityTodo(STARTDETAILACTIVITY)  //跳转去MainActivity执行相关操作
        }

        return viewHolder //注意这里要返回viewHolder，因为有各种点击事件
    }

    /*
     * 对RecyclerView滚入屏幕的子项数据赋值
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stockData = stockdataList[position]
        holder.stockName.text = stockData.stockName
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