package com.example.swiftbay.adapter

import android.content.Context
import com.example.swiftbay.helper.GeneralCallBack
import com.example.swiftbay.Model.Product
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.swiftbay.R
import com.bumptech.glide.Glide
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.helper.getFormattedAmount
import com.example.swiftbay.helper.isEmptyField
import java.util.ArrayList

class OrderAdapter(
    private var context: Context,
    var results: ArrayList<OrderData>
) : RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.orders_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val OrderData = results[holder.adapterPosition]
        if(OrderData.products.size > 0){

            val orderAdapter = OrderProductsAdapter(
                context,
                OrderData.products
            )
            holder.recyclerView.adapter = orderAdapter
            orderAdapter.notifyDataSetChanged()
        }
        if (OrderData.total != null && !isEmptyField(OrderData.total.toString())) {
            holder.total.text = "PKR" + OrderData.total.toString()
        }else{
            holder.total.text = ""
        }
        if (OrderData.status != null && !isEmptyField(OrderData.status.toString())) {
            holder.status.text = OrderData.status.toString()
        }else{
            holder.status.text = ""
        }
        if (OrderData.timeRemaining != null && !isEmptyField(OrderData.timeRemaining.toString())) {
            holder.time.text = "Remaining Mins " + OrderData.timeRemaining.toString()
        }else{
            holder.time.text = ""
        }

        if (OrderData._id != null && !isEmptyField(OrderData._id.toString())) {
            holder.orderId.text = OrderData._id
        }

    }

    fun addAllItems(results: ArrayList<OrderData>?) {
        this.results.addAll(results!!)
        notifyDataSetChanged()
    }

    fun deletedItem(Position: Int) {
        results.removeAt(Position)
        notifyDataSetChanged()
    }

    fun clearAllData() {
        results.clear()
        notifyDataSetChanged()
    }

    var allItems: ArrayList<OrderData>
        get() = results
        set(allItems) {
            results = allItems
        }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        var orderId: TextView = itemView.findViewById(R.id.orderId)
        var total: TextView = itemView.findViewById(R.id.totalTv)
        var status: TextView = itemView.findViewById(R.id.status)
        var time: TextView = itemView.findViewById(R.id.time)
    }
}