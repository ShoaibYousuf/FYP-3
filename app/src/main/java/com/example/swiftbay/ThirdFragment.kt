package com.example.swiftbay

import android.os.Bundle
import com.example.swiftbay.ThirdFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.AddToCartResponse
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.Model.Product
import com.example.swiftbay.R
import com.example.swiftbay.adapter.CartAdapter
import com.example.swiftbay.adapter.OrderAdapter
import com.example.swiftbay.helper.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ThirdFragment : Fragment() {


    var orderAdapter: OrderAdapter? = null
    var recycler: RecyclerView? = null
    var orderDataList: ArrayList<OrderData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_pickup_order, container, false)
        recycler = view.findViewById(R.id.recyclerView)

        recycler!!.layoutManager = LinearLayoutManager(context)
        orderAdapter = OrderAdapter(
            requireActivity(),
            orderDataList
        )
        recycler!!.adapter = orderAdapter
        callApi()
        return view
    }


    fun callApi() {
        showDialoog(requireActivity(),"")
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
                        orderAdapter = OrderAdapter(
                            requireActivity(),
                            orderDataList
                        )
                        recycler!!.adapter = orderAdapter
                        orderAdapter!!.notifyDataSetChanged()
                    }
                    dismissDialoog()
                    requireActivity().showToast("Success")
                } else {
                    dismissDialoog()
                    requireActivity().showToast("Failed")
                }
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                dismissDialoog()
                requireActivity().showToast("Failed")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }
}