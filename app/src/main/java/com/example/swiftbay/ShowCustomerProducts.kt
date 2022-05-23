package com.example.swiftbay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.*
import com.example.swiftbay.adapter.CustomerProductsAdapter
import com.example.swiftbay.adapter.ManageCurrentProductsAdapter
import com.example.swiftbay.helper.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowCustomerProducts : AppCompatActivity() {
    var logo: ImageView? = null
    var name: TextView? = null
    var productsListAdapter: CustomerProductsAdapter? = null
    var productsRecycler: RecyclerView? = null
    var productsList: ArrayList<Product> = arrayListOf()
    var restId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_current_products)
        logo = findViewById(R.id.logo)
        name = findViewById(R.id.name)

        productsRecycler = findViewById(R.id.recyclerView)
        productsRecycler!!.layoutManager = LinearLayoutManager(this)

        productsListAdapter = CustomerProductsAdapter(
            this@ShowCustomerProducts,
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {}
                override fun onDelete(value: Any?) {}

                override fun onTap(value: Any?) {
                    if (value is Int) {
                        val bundle = Bundle()
                        val orderDataString = toJson(productsList[value])
                        bundle.putString("Product", orderDataString)
                        bundle.putString("restId", "")
                        val i = Intent(
                            this@ShowCustomerProducts,
                            SingleProduct::class.java
                        )
                        i.putExtras(bundle)
                        startActivity(i)
                    }

                }
            },
            productsList
        )
        productsRecycler!!.adapter = productsListAdapter

    }

    override fun onResume() {
        super.onResume()
        val extras = intent.extras
        if (extras != null) {

            val restaurant =
                fromJson(extras.getString("Restaurant").toString(), Restaurant::class.java)
            name!!.setText(restaurant.name)
            Glide.with(this@ShowCustomerProducts).load(restaurant.image).into(logo!!)
            restId =
                if (isEmptyField(restaurant._id!!))
                    restaurant.id!! else restaurant._id!!
            callApi(restId)
        }
    }

    fun deletecallApi(
        productId: String,
        position: Int
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<DelProductResponse> =
            apiInterface.deleteProduct(
                productId,
                token
            )
        call.enqueue(object : Callback<DelProductResponse?> {
            override fun onResponse(
                call: Call<DelProductResponse?>,
                response: Response<DelProductResponse?>
            ) {
                if (response.body() != null) {
                    productsListAdapter!!.deletedItem(position)
                    productsList = productsListAdapter!!.allItems as ArrayList<Product>
                    dismissDialoog()
                    Toast.makeText(
                        this@ShowCustomerProducts,
                        "Deleted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@ShowCustomerProducts,
                        "Failed: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<DelProductResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@ShowCustomerProducts, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }




    fun callApi(id: String) {
        showDialoog(this,"")
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<AllProductsResponse> = apiInterface.sellerProducts(id)
        call.enqueue(object : Callback<AllProductsResponse?> {
            override fun onResponse(
                call: Call<AllProductsResponse?>,
                response: Response<AllProductsResponse?>
            ) {
                if (response.body() != null) {
                    val restaurantsResponse: AllProductsResponse =
                        response.body() as AllProductsResponse
                    val resultsList = restaurantsResponse.result
                    if (resultsList.size > 0) {
                        productsList.clear()
                        productsListAdapter!!.clearAllData()
                        productsListAdapter!!.addAllItems(resultsList)
                        dismissDialoog()

                    } else {
                        dismissDialoog()
                        Toast.makeText(
                            this@ShowCustomerProducts,
                            "Sorry! you dont have any Product : ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@ShowCustomerProducts,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AllProductsResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@ShowCustomerProducts, "Fail", Toast.LENGTH_LONG).show()
            }
        })
    }

}