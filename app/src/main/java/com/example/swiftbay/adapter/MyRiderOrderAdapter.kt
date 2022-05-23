package com.example.swiftbay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.R
import com.example.swiftbay.helper.GeneralCallBack
import com.example.swiftbay.helper.getFormattedAmount
import com.example.swiftbay.helper.isEmptyField

class MyRiderOrderAdapter(
        private var context: Context,
        var callBack: GeneralCallBack,
        var results: ArrayList<OrderData>
): RecyclerView.Adapter<MyRiderOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRiderOrderAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_my_rider_order_adapter, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val OrderData = results[holder.adapterPosition]
        if (OrderData.products.size > 0) {

            val sellerOrdersAdapter = SellerOrderProductsAdapter(
                    context,
                    OrderData.products
            )
            holder.recyclerView.adapter = sellerOrdersAdapter
            sellerOrdersAdapter.notifyDataSetChanged()
        }
        if (OrderData.total != null && !isEmptyField(OrderData.total.toString())) {
            holder.total.text = context.getFormattedAmount(OrderData.total)
        }
        if (OrderData._id != null && !isEmptyField(OrderData._id.toString())) {
            holder.orderId.text = OrderData._id
        }
        holder.showRoute.setOnClickListener { v: View? ->
            callBack.onUpdate(position)
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
        var showRoute: Button = itemView.findViewById(R.id.showRoute)


    }
}