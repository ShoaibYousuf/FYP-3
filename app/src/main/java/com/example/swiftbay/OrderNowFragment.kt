package com.example.swiftbay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.Restaurant
import com.example.swiftbay.Model.RestaurantsResponse
import com.example.swiftbay.adapter.CustomerRestaurantsAdapter
import com.example.swiftbay.adapter.RestaurantsListAdapter
import com.example.swiftbay.helper.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderNowFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var edSerch: EditText? = null
    private var search: Button? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var restaurantsListAdapter: CustomerRestaurantsAdapter? = null
    var restaurantsRecycler: RecyclerView? = null
    var restList: ArrayList<Restaurant> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_now, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe)
        restaurantsRecycler = view.findViewById(R.id.restaurants_list_recycler_view)
        edSerch = view.findViewById(R.id.edSerch)
        search = view.findViewById(R.id.search)
        search!!.setOnClickListener {
            callApi()
        }
        swipeRefreshLayout.setOnRefreshListener {
            callApi()
            Handler().postDelayed(Runnable {
                swipeRefreshLayout.isRefreshing = false
            }, 4000)
        }
        restaurantsRecycler!!.layoutManager = LinearLayoutManager(context)
        restaurantsListAdapter = CustomerRestaurantsAdapter(
            context,
            object : GeneralCallBack {
                override fun onUpdate(value: Any?) {}
                override fun onDelete(value: Any?) {}

                override fun onTap(value: Any?) {
                    if (value is Int) {
                        val bundle = Bundle()
                        val orderDataString = toJson(restList[value])
                        bundle.putString("Restaurant", orderDataString)
                        val i = Intent(
                            context,
                            ShowCustomerProducts::class.java
                        )
                        i.putExtras(bundle)
                        startActivity(i)
                    }

                }
            },
            restList
        )
        restaurantsRecycler!!.adapter = restaurantsListAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApi()
    }

    fun callApi() {
        showDialoog(requireActivity(),"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        if(!isEmptyField(edSerch!!.text.toString())){
            val call: Call<RestaurantsResponse> = apiInterface.getRestaurants(edSerch!!.text.toString())
            call.enqueue(object : Callback<RestaurantsResponse?> {
                override fun onResponse(
                    call: Call<RestaurantsResponse?>,
                    response: Response<RestaurantsResponse?>
                ) {
                    if (response.body() != null) {
                        val restaurantsResponse: RestaurantsResponse =
                            response.body() as RestaurantsResponse
                        val resultsList = restaurantsResponse.results
                        if(resultsList.size>0){
                            restList.clear()
                            restaurantsListAdapter!!.clearAllData()
                            restaurantsListAdapter!!.addAllItems(resultsList)}else{
                            requireActivity().showToast("No Restaurant Found")
                        }
                        dismissKeyboard()
                        dismissDialoog()
                    } else {
                        dismissDialoog()
                        Toast.makeText(
                            requireActivity(),
                            "Failed : ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RestaurantsResponse?>, t: Throwable) {
                    dismissDialoog()
                    Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
                }
            })
        }else{
            dismissDialoog()
            val call: Call<RestaurantsResponse> = apiInterface.getRestaurants()
            call.enqueue(object : Callback<RestaurantsResponse?> {
                override fun onResponse(
                    call: Call<RestaurantsResponse?>,
                    response: Response<RestaurantsResponse?>
                ) {
                    if (response.body() != null) {
                        val restaurantsResponse: RestaurantsResponse =
                            response.body() as RestaurantsResponse
                        val resultsList = restaurantsResponse.results
                        if(resultsList.size>0){
                        restList.clear()
                        restaurantsListAdapter!!.clearAllData()
                        restaurantsListAdapter!!.addAllItems(resultsList)}else{
                            requireActivity().showToast("No Restaurant Found")
                        }
                        dismissDialoog()
                        dismissKeyboard()
                    } else {
                        dismissDialoog()
                        Toast.makeText(
                            requireActivity(),
                            "Failed to Login: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RestaurantsResponse?>, t: Throwable) {
                    dismissDialoog()
                    Toast.makeText(requireActivity(), "Fail", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    fun dismissKeyboard() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != requireActivity().currentFocus) imm.hideSoftInputFromWindow(
            requireActivity().currentFocus!!
                .applicationWindowToken, 0
        )
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): OrderNowFragment {
            val fragment = OrderNowFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}