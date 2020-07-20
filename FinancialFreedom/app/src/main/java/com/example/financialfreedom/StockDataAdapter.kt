package com.example.financialfreedom

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.financialfreedom.activity.MainActivity
import com.example.financialfreedom.activity.MainActivity.Companion.STARTDETAILACTIVITY

/** RecyclerView的适配器 */
class StockDataAdapter(val stockdataList : List<StockData>) :
    RecyclerView.Adapter <StockDataAdapter.ViewHolder>() {

    /*
     * 用于获取最外层布局的及控件的实例
     */
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
         * TextView：股票当前价格点击事件
         */
        viewHolder.stockNowPrice.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.nowPrice}",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * TextView：股票TTM好价格点击事件
         */
        viewHolder.stockTtmPrice.setOnClickListener{
            val position = viewHolder.adapterPosition
            val stockData = stockdataList[position]
            Toast.makeText(parent.context, "you click ${stockData.ttmPrice}",
                Toast.LENGTH_SHORT).show()
        }

        /*
         * TextView：股票动态股息率好价格点击事件
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
            val position = (viewHolder.adapterPosition + 1) //数据库的id是从1开始索引
            MainActivity.mainActivityTodo(STARTDETAILACTIVITY, position)  //跳转去MainActivity执行相关操作
        }

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
            val ttmPrice = priceList[1].split(":")[1]
            val drcPrice = priceList[2].split(":")[1]
            /* 设置文本颜色:由价格比较得出 */
            val color : Int
            if ((nowPrice.toDouble() > ttmPrice.toDouble()) &&
                (nowPrice.toDouble() > drcPrice.toDouble())){
                color = Color.RED
            } else if ((nowPrice.toDouble() < ttmPrice.toDouble()) &&
                (nowPrice.toDouble() < drcPrice.toDouble())){
                color = Color.GREEN
            } else {
                color = Color.BLUE
            }
            holder.stockNowPrice.setTextColor(color)
            holder.stockNowPrice.text = nowPrice
            holder.stockTtmPrice.text = ttmPrice
            holder.stockDrcPrice.text = drcPrice
        }
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

    /*
     * 返回数据源长度
     */
    override fun getItemCount() = stockdataList.size
}