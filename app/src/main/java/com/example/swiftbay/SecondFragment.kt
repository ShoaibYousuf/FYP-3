package com.example.swiftbay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.example.swiftbay.SecondFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.*
import com.example.swiftbay.R
import com.example.swiftbay.adapter.CartAdapter
import com.example.swiftbay.adapter.ManageCurrentProductsAdapter
import com.example.swiftbay.adapter.RestaurantsListAdapter
import com.example.swiftbay.helper.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    var cartAdapter: CartAdapter? = null
    var productRecycler: RecyclerView? = null
    var totalTv: TextView? = null
    var placeOrder: Button? = null
    var productList: ArrayList<Product> = arrayListOf()
    private val Location_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_schedule_order, container, false)
        productRecycler = view.findViewById(R.id.restaurants_list_recycler_view)
        totalTv = view.findViewById(R.id.totalTv)
        placeOrder = view.findViewById(R.id.placeOrder)
        productRecycler!!.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(
            requireActivity(),
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {}

                override fun onDelete(value: Any?) {
                    if (value is Int) {

                       val id = productList[value]._id
                       deletecallApi(id!!)

                    }

                }

                override fun onTap(value: Any?) {
                }
            },
            productList
        )
        productRecycler!!.adapter = cartAdapter
        callApi()

        placeOrder!!.setOnClickListener {
            if (!isReadStoragePermissionGranted) {
                // Permission is not granted
                askReadStoragePermission()
            } else {
                val i = Intent(requireActivity(),MapsActivity::class.java)
                startActivity(i)
            }

        }
        return view

    }

    private fun askReadStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Location_PERMISSION
        )
    }

    private val isReadStoragePermissionGranted: Boolean
        private get() {
            var isGranted = false
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = true
            }
            return isGranted
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Location_PERMISSION) {
            val i = Intent(requireActivity(),MapsActivity::class.java)
            startActivity(i)
        }
    }
    fun deletecallApi(
        product: String
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        val deleteCartModel = DeleteCartModel(product)
        showDialoog(requireActivity(),"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<AddToCartResponse> =
            apiInterface.deleteCart(
                token,
                deleteCartModel
            )
        call.enqueue(object : Callback<AddToCartResponse?> {
            override fun onResponse(
                call: Call<AddToCartResponse?>,
                response: Response<AddToCartResponse?>
            ) {
                if (response.body() != null) {
                    val cartResponse: AddToCartResponse =
                        response.body() as AddToCartResponse
                    if (cartResponse.data != null && cartResponse.data!!.products.size > 0) {
                        placeOrder!!.visibility = View.VISIBLE
                        totalTv!!.text =
                            requireActivity().getFormattedAmount(cartResponse.data!!.total)
                        dismissDialoog()
                        Toast.makeText(
                            requireActivity(),
                            response.body()!!.comment,
                            Toast.LENGTH_LONG
                        ).show()

                        productList.clear()
                        productList.addAll(cartResponse.data!!.products)
                        cartAdapter!!.clearAllData()
                        cartAdapter!!.addAllItems(cartResponse.data!!.products)
                    } else {
                        dismissDialoog()
                        requireActivity().showToast("No Product Found")
                    }

                } else {
                    dismissDialoog()
                    requireActivity().showToast("Failled")
                }
            }

            override fun onFailure(call: Call<AddToCartResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun callApi() {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        showDialoog(requireActivity(),"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<AddToCartResponse> = apiInterface.getCart(token)
        call.enqueue(object : Callback<AddToCartResponse?> {
            override fun onResponse(
                call: Call<AddToCartResponse?>,
                response: Response<AddToCartResponse?>
            ) {
                if (response.body() != null) {
                    val cartResponse: AddToCartResponse =
                        response.body() as AddToCartResponse
                    if (cartResponse.data != null && cartResponse.data!!.products.size > 0) {
                        totalTv!!.text =
                            requireActivity().getFormattedAmount(cartResponse.data!!.total)
                        dismissDialoog()
                        Toast.makeText(
                            requireActivity(),
                            response.body()!!.comment,
                            Toast.LENGTH_LONG
                        ).show()

                        productList.clear()
                        productList.addAll(cartResponse.data!!.products)
                        cartAdapter!!.clearAllData()
                        cartAdapter!!.addAllItems(cartResponse.data!!.products)
                    } else {
                        dismissDialoog()
                        requireActivity().showToast("No Product Found")
                    }

                } else {
                    dismissDialoog()
                    requireActivity().showToast("Failled")
                }
            }

            override fun onFailure(call: Call<AddToCartResponse?>, t: Throwable) {

                requireActivity().showToast("Failled")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }
}