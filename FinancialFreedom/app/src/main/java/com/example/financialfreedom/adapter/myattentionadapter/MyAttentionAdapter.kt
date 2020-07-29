package com.example.financialfreedom.adapter.myattentionadapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialfreedom.R
import com.example.financialfreedom.activity.MyAttentionActivity

/**
 *  TODO:长按删除的listener，到底应该在onCreateViewHolder中还是在
 *  TODO:onBindViewHolder中?目前在两个函数中都写了此函数，似乎都可以
 *
 */
class MyAttentionAdapter(list: ArrayList<RealTimeStock>) :
    RecyclerView.Adapter <MyAttentionAdapter.ViewHolder>(){

    private var realTimeStockList = ArrayList<RealTimeStock>()

    init {
        realTimeStockList = list
    }

    /* 用于获取最外层布局的及控件的实例 */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val stockName: TextView = view.findViewById(R.id.stockName)
        val stockNowPrice: TextView = view.findViewById(R.id.stockNowPrice)
        val upAndDown: TextView = view.findViewById(R.id.upAndDown)
        val upAndDownRate: TextView = view.findViewById(R.id.upAndDownRate)
    }

    /* 加载my_attention布局 */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_attention, parent, false)

        val viewHolder = ViewHolder(view)
        /**
         *  长按监听
         */
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.adapterPosition
            /* 将长按item对应的股票代码发送至MyAttentionActivity */
            MyAttentionActivity.myAttentionActivityTodo(MyAttentionActivity.HANDLELONGCLIECK,
                realTimeStockList[position].stockCode)
            /* 在ArrayList中移除此股 */
            realTimeStockList.remove(realTimeStockList[position])
            /* 通知移除该item */
            notifyItemRemoved(position)
            /* 通知调制ArrayList顺序(此句删除也无影响) */
            notifyItemRangeChanged(position, realTimeStockList.size)
            false
        }
        return viewHolder //注意这里要返回viewHolder，因为有各种点击事件
    }

    /**
     * 调用adapter的notifyItemChanged时会调用此函数，用于更新局部控件
     */
    @SuppressLint("SetTextI18n")
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
            color = when {
                upAndDown.toDouble() > 0 -> {
                    Color.RED
                }
                upAndDown.toDouble() == 0.00 -> {
                    Color.GRAY
                }
                else -> {
                    Color.GREEN
                }
            }
            holder.upAndDown.setTextColor(color)
            holder.upAndDownRate.setTextColor(color)

            /* 文本更新 */
            holder.stockNowPrice.text = nowPrice
            holder.upAndDown.text = upAndDown
            holder.upAndDownRate.text = String.format("%.2f", upAndDownRate.toDouble() * 100.0) + "%"
        }
    }

    /* 对RecyclerView滚入屏幕的子项数据赋值 */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realTimeStock = realTimeStockList[position]
        holder.stockName.text = realTimeStock.stockName
        holder.stockNowPrice.text = realTimeStock.nowPrice.toString()
        holder.upAndDown.text = realTimeStock.upAndDown
        holder.upAndDownRate.text = (realTimeStock.upAndDownRate.toDouble() * 100.0).toString() + "%"

        /*
         * 文本颜色设置:
         * 涨跌大于0 -> 红色
         * 涨跌等于0 -> 灰色
         * 涨跌小于0 -> 绿色
         */
        val color: Int
        color = when {
            realTimeStock.upAndDown.toDouble() > 0 -> {
                Color.RED
            }
            realTimeStock.upAndDown.toDouble() == 0.00 -> {
                Color.GRAY
            }
            else -> {
                Color.GREEN
            }
        }
        holder.upAndDown.setTextColor(color)
        holder.upAndDownRate.setTextColor(color)
    }

    /* 返回数据源长度 */
    override fun getItemCount() = realTimeStockList.size
}