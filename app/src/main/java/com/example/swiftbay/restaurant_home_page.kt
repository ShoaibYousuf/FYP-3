package com.example.swiftbay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.CreateResturantResponse
import com.example.swiftbay.Model.Restaurant
import com.example.swiftbay.Model.SellerRestaurantsResponse
import com.example.swiftbay.adapter.RestaurantsListAdapter
import com.example.swiftbay.helper.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class restaurant_home_page : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var button2: FloatingActionButton? = null
    var ordersBtn: Button? =null
    var restaurantsListAdapter: RestaurantsListAdapter? = null
    var restaurantsRecycler: RecyclerView? = null
    var restList: ArrayList<Restaurant> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_home_page)
        toolbar = findViewById<View>(R.id.restaurant_home_page_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        PrefsHelper.init(this)
        ordersBtn = findViewById(R.id.ordersBtn)

        ordersBtn!!.setOnClickListener {
            val intent = Intent(this, SellerOrders::class.java)
            startActivity(intent)
        }



        button2 = findViewById(R.id.button2)
        button2!!.setOnClickListener {
            val i = Intent(this, RestaurantCreation::class.java)
            startActivity(i)
        }

        restaurantsRecycler = findViewById(R.id.restaurants_list_recycler_view)
        restaurantsRecycler!!.layoutManager = LinearLayoutManager(this)

        restaurantsListAdapter = RestaurantsListAdapter(
            this@restaurant_home_page,
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {
                    if (value is Int) {
                        val bundle = Bundle()
                        val orderDataString = toJson(restList[value])
                        bundle.putString("Restaurant", orderDataString)
                        val i = Intent(
                            this@restaurant_home_page,
                            RestaurantCreation::class.java
                        )
                        i.putExtras(bundle)
                        startActivity(i)
                    }

                }

                override fun onDelete(value: Any?) {
                    if (value is Int) {
                        val restId: String = restList[value].id!!
                        deletecallApi(restId, value)

                    }

                }

                override fun onTap(value: Any?) {
                    if (value is Int) {
                        val bundle = Bundle()
                        val orderDataString = toJson(restList[value])
                        bundle.putString("Restaurant", orderDataString)
                        val i = Intent(
                            this@restaurant_home_page,
                            Manage_Current_Products::class.java
                        )
                        i.putExtras(bundle)
                        startActivity(i)
                    }

                }
            },
            restList
        )
        restaurantsRecycler!!.adapter = restaurantsListAdapter
        //recycleproducts.setLayoutManager(new LinearLayoutManager(this));

    }

    override fun onResume() {
        super.onResume()
        PrefsHelper.init(this)
        callApi()
    }

    fun callApi() {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<SellerRestaurantsResponse> = apiInterface.getSellerRestaurants(token)
        call.enqueue(object : Callback<SellerRestaurantsResponse?> {
            override fun onResponse(
                call: Call<SellerRestaurantsResponse?>,
                response: Response<SellerRestaurantsResponse?>
            ) {
                if (response.body() != null) {
                    val restaurantsResponse: SellerRestaurantsResponse =
                        response.body() as SellerRestaurantsResponse
                    val resultsList = restaurantsResponse.result
                    if (resultsList.size > 0) {
                        restList.clear()
                        restaurantsListAdapter!!.clearAllData()
                        restaurantsListAdapter!!.addAllItems(resultsList)
                        dismissDialoog()
                    } else {
                        dismissDialoog()
                        Toast.makeText(
                            this@restaurant_home_page,
                            "Sorry! you dont have any resturant : ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@restaurant_home_page,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<SellerRestaurantsResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@restaurant_home_page, "Fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun deletecallApi(
        restId: String,
        position: Int
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateResturantResponse> =
            apiInterface.deleteRestaurants(
                restId,
                token
            )
        call.enqueue(object : Callback<CreateResturantResponse?> {
            override fun onResponse(
                call: Call<CreateResturantResponse?>,
                response: Response<CreateResturantResponse?>
            ) {
                if (response.body() != null) {
                    restaurantsListAdapter!!.deletedItem(position)
                    restList = restaurantsListAdapter!!.allItems as ArrayList<Restaurant>
                    Toast.makeText(
                        this@restaurant_home_page,
                        "Deleted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@restaurant_home_page,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateResturantResponse?>, t: Throwable) {
                Toast.makeText(this@restaurant_home_page, "Failed", Toast.LENGTH_LONG).show()
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