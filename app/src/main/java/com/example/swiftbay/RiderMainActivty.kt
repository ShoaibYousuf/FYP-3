package com.example.swiftbay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.OrderData
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.Model.OrderUpdateResponse
import com.example.swiftbay.Model.UpdateStutsModel
import com.example.swiftbay.adapter.RiderOrdersAdapter
import com.example.swiftbay.adapter.SellerOrdersAdapter
import com.example.swiftbay.helper.GeneralCallBack
import com.example.swiftbay.helper.dismissDialoog
import com.example.swiftbay.helper.showDialoog
import com.example.swiftbay.helper.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiderMainActivty : AppCompatActivity() {

    var sellerOrdersAdapter: RiderOrdersAdapter? = null
    var recycler: RecyclerView? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var myorder: Button? = null
    var orderDataList: ArrayList<OrderData> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_main_activty)



        recycler = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipe)
        myorder = findViewById(R.id.myorder)

        myorder!!.setOnClickListener {
            val i = Intent(this,MyRiderOrders::class.java)
            startActivity(i)
        }
        recycler!!.layoutManager = LinearLayoutManager(this)
      /*  sellerOrdersAdapter = RiderOrdersAdapter(this,
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {
                    if (value is Int) {

                        callUpdateOrder(orderDataList[value]._id!!)
                }}

                override fun onDelete(value: Any?) {


                }

                override fun onTap(value: Any?) {
                }
            },
            orderDataList
        )*/

        swipeRefreshLayout.setOnRefreshListener {
            callApi()
            Handler().postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 4000)
        }


    }

    override fun onResume() {
        super.onResume()
        callApi()
    }

    fun callUpdateOrder(id:String) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        showDialoog(this,"")

        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val updateStutsModel= UpdateStutsModel("assigned")
        val call: Call<OrderUpdateResponse> = apiInterface.getRiderOrderUpdate(id,token,updateStutsModel)
        call.enqueue(object : Callback<OrderUpdateResponse?> {
            override fun onResponse(
                call: Call<OrderUpdateResponse?>,
                response: Response<OrderUpdateResponse?>
            ) {
                if (response.body() != null) {
                    val orderResponse: OrderUpdateResponse =
                        response.body() as OrderUpdateResponse
                    val i2 = Intent(this@RiderMainActivty,MyRiderOrders::class.java)
                    startActivity(i2)
                    dismissDialoog()
                    showToast("Success")
                } else {
                    dismissDialoog()
                    showToast("Failed")
                }
            }

            override fun onFailure(call: Call<OrderUpdateResponse?>, t: Throwable) {
                dismissDialoog()
                showToast("Failled")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }

    fun callApi() {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<OrderResponse> = apiInterface.getRiderOrder(token)
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
                        sellerOrdersAdapter = RiderOrdersAdapter(
                            this@RiderMainActivty,
                            object : GeneralCallBack {
                                override fun onUpdate(value: Any?) {
                                    if (value is Int) {

                                        callUpdateOrder(orderDataList[value]._id!!)
                                    }}

                                override fun onDelete(value: Any?) {}

                                override fun onTap(value: Any?) {}
                            },orderDataList
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

    override fun onBackPressed() {
        callBack()
    }

    fun callBack() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure! yoe want to exit?")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                finishAffinity()
            })

        builder1.setNeutralButton(
            "LogOut",
            DialogInterface.OnClickListener { dialog, id ->
                PrefsHelper.clear(PrefsHelper.TOKEN)
                PrefsHelper.clear(PrefsHelper.ROLE)
                dialog.cancel()
                finishAffinity()
            })

        builder1.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

}