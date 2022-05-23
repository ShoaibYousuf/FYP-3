package com.example.swiftbay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.adapter.OrderAdapter
import com.example.swiftbay.adapter.SellerOrdersAdapter
import com.example.swiftbay.helper.dismissDialoog
import com.example.swiftbay.helper.showDialoog
import com.example.swiftbay.helper.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerOrders : AppCompatActivity() {

    var sellerOrdersAdapter: SellerOrdersAdapter? = null
    var recycler: RecyclerView? = null
    var orderDataList: ArrayList<OrderData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_orders)

        recycler = findViewById(R.id.recyclerView)
        recycler!!.layoutManager = LinearLayoutManager(this)
        sellerOrdersAdapter = SellerOrdersAdapter(this,
                orderDataList
        )
        recycler!!.adapter = sellerOrdersAdapter
        callApi()
    }

    fun callApi() {
        showDialoog(this,"")
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<OrderResponse> = apiInterface.getOrder(token)
        call.enqueue(object : Callback<OrderResponse?> {
            override fun onResponse(
                    call: Call<OrderResponse?>,
                    response: Response<OrderResponse?>
            ) {
                if (response.body() != null) {
                    val orderResponse: OrderResponse =
                            response.body() as OrderResponse

                    orderDataList.clear()

                    if(orderResponse.data.size> 0)
                    {
                        orderDataList=orderResponse.data
                        sellerOrdersAdapter = SellerOrdersAdapter(
                                this@SellerOrders,
                                orderDataList
                        )
                        recycler!!.adapter = sellerOrdersAdapter
                        sellerOrdersAdapter!!.notifyDataSetChanged()
                    }
                    dismissDialoog()
                    showToast("Success")
                } else {
                    dismissDialoog()
                    showToast("Failed")
                }
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                dismissDialoog()
                showToast("Failed")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }
}