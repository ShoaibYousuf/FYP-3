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
import com.example.swiftbay.helper.getFormattedAmount
import com.example.swiftbay.helper.isEmptyField
import java.util.ArrayList

class OrderProductsAdapter(
    private var context: Context,
    var results: ArrayList<Product>
) : RecyclerView.Adapter<OrderProductsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.orders_product_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = results[holder.adapterPosition]
        if (product.productImage != null && product.productImage != "") {
            Glide.with(context).load(product.productImage).into(holder.imagecontact)
        }
        if (product.productName != null && !isEmptyField(product.productName.toString())) {
            holder.product_name.text = product.productName
        }
        if (product.price != null && !isEmptyField(product.price.toString())) {
            holder.product_price.text = context.getFormattedAmount(product.price)
        }
        if (product.description != null && !isEmptyField(product.description.toString())) {
            holder.product_description.text = product.description
        }
    }

    fun addAllItems(results: ArrayList<Product>?) {
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

    var allItems: ArrayList<Product>
        get() = results
        set(allItems) {
            results = allItems
        }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagecontact: ImageView
        var product_name: TextView
        var product_description: TextView
        var product_price: TextView


        init {
            imagecontact = itemView.findViewById(R.id.productimg)
            product_name = itemView.findViewById(R.id.name)
            product_description = itemView.findViewById(R.id.description)
            product_price = itemView.findViewById(R.id.price)

        }
    }
}