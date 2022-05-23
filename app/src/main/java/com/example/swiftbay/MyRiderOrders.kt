package com.example.swiftbay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.adapter.MyRiderOrderAdapter
import com.example.swiftbay.adapter.SellerOrdersAdapter
import com.example.swiftbay.helper.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRiderOrders : AppCompatActivity() {
    var sellerOrdersAdapter: MyRiderOrderAdapter? = null
    var recycler: RecyclerView? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var orderDataList: ArrayList<OrderData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rider_orders)

        recycler = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipe)
        recycler!!.layoutManager = LinearLayoutManager(this)
        sellerOrdersAdapter = MyRiderOrderAdapter(
            this,
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {
                    if(value is Int){

                        openRouteIntent( orderDataList[value].source!!.address.toString(),
                            orderDataList[value].destination!!.address.toString())
                    }

                }

                override fun onDelete(value: Any?) {
                }

                override fun onTap(value: Any?) {
                }
            },
            orderDataList
        )
        recycler!!.adapter = sellerOrdersAdapter
        callApi()

        swipeRefreshLayout.setOnRefreshListener {
            callApi()
            Handler().postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 4000)
        }
    }


    fun callApi() {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        showDialoog(this, "")
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

                    if (orderResponse.data.size > 0) {
                        orderDataList = orderResponse.data
                        sellerOrdersAdapter = MyRiderOrderAdapter(
                            this@MyRiderOrders,
                            object : GeneralCallBack {
                                override fun onUpdate(value: Any?) {
                                    if(value is Int){

                                        openRouteIntent( orderDataList[value].source!!.address.toString(),
                                            orderDataList[value].destination!!.address.toString())
                                    }

                                }

                                override fun onDelete(value: Any?) {

                                }

                                override fun onTap(value: Any?) {
                                }
                            },
                            orderDataList
                        )
                        recycler!!.adapter = sellerOrdersAdapter
                        sellerOrdersAdapter!!.notifyDataSetChanged()
                    }

                    dismissDialoog()
                    showToast("Success")
                } else {
                    dismissDialoog()
                    showToast("Failleds")
                }
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {

                dismissDialoog()
                showToast("Failled")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }
}