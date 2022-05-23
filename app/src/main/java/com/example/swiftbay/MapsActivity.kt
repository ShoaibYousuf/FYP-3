package com.example.swiftbay

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.Destination
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.Model.PlaceOrderModel
import com.example.swiftbay.Model.PlaceModel
import com.example.swiftbay.databinding.ActivityMapsBinding
import com.example.swiftbay.helper.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, PopupMenu.OnMenuItemClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var gpsTracker: GPSTracker? = null
    var geocoder: Geocoder? = null
    var latLng: LatLng? = null
    var scheduleTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gpsTracker = GPSTracker(this)
        geocoder = Geocoder(this)

        binding.time.setOnClickListener {
            val popup = PopupMenu(this,binding.time)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.pop_menu)
            popup.show()

        }

        binding.search.setOnClickListener {
            if (!isEmptyField(binding.edSerch.text.toString())) {
                 binding.edSerch.setText(getLocationName(binding.edSerch.text.toString()))
             }
        }
        binding.placeOrder.setOnClickListener {
            if (!isEmptyField(binding.edSerch.text.toString())) {
                if (latLng != null) {
                    callApi()
                } else {
                    showToast("Adress is empty")
                }
            } else {
                showToast("Adress is empty")
            }
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        latLng = LatLng(gpsTracker!!.latitude, gpsTracker!!.longitude)
        mMap.addMarker(MarkerOptions().position(latLng!!).title("Current Location"))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng!!, 16.0f)
        mMap.animateCamera(cameraUpdate)
        mMap.isMyLocationEnabled = true
        binding.edSerch.setText(currentLocationName(latLng!!))


        mMap.setOnMapClickListener { latLngs ->
            latLng = latLngs
            mMap.clear()
            binding.edSerch.setText(currentLocationName(latLng!!))
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng!!)
                    .title("My Spot")
                    .snippet("This is my spot!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        }
        mMap.setOnMarkerClickListener { marker ->

            true
        }
    }


    fun callApi() {
        showDialoog(this, "")
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        val destination = Destination(
            binding.edSerch.text.toString(),
            latLng!!.latitude.toString(),
            latLng!!.longitude.toString()
        )
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        if(isEmptyField(scheduleTime)){
            val placeOrderModel = PlaceOrderModel(destination, "cod")
            val call: Call<OrderResponse> = apiInterface.placeOrder(token, placeOrderModel)
            call.enqueue(object : Callback<OrderResponse?> {
                override fun onResponse(
                    call: Call<OrderResponse?>,
                    response: Response<OrderResponse?>
                ) {
                    if (response.body() != null) {
                        val orderResponse: OrderResponse =
                            response.body() as OrderResponse
                        dismissDialoog()
                        showToast("Success")
                    } else {
                        dismissDialoog()
                        showToast("Failed")
                    }
                }

                override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                    dismissDialoog()
                    showToast("Success")
                    //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
                }
            })

        }else{
            val placeOrderModel2 = PlaceModel(destination, "cod",scheduleTime)
            val call: Call<OrderResponse> = apiInterface.placeOrder2(token, placeOrderModel2)
            call.enqueue(object : Callback<OrderResponse?> {
                override fun onResponse(
                    call: Call<OrderResponse?>,
                    response: Response<OrderResponse?>
                ) {
                    if (response.body() != null) {
                        val orderResponse: OrderResponse =
                            response.body() as OrderResponse
                        dismissDialoog()
                        showToast("Success")
                    } else {
                        dismissDialoog()
                        showToast("Failed")
                    }
                }

                override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                    dismissDialoog()
                    showToast("Success")
                    //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun currentLocationName(latLng: LatLng): String {
        val addresses: List<Address>

        var name: String? = ""
        try {
            addresses = geocoder!!.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.size > 0) {
                name = addresses[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return name!!
    }

    fun getLocationName(value: String?): String {
        val list: List<Address>
        var name = value
        try {
            list = geocoder!!.getFromLocationName(value, 1)
            if (list.size > 0) {
                name = list[0].getAddressLine(0)
                latLng = LatLng(list[0].latitude, list[0].longitude)
                dismissKeyboard()
            } else {
                Toast.makeText(this, "Invalid Location", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name!!
    }

    fun dismissKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != currentFocus) imm.hideSoftInputFromWindow(
            currentFocus!!
                .applicationWindowToken, 0
        )
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.one -> {
                scheduleTime = "1"
                true
            }
            R.id.two -> {
                scheduleTime = "2"
                true
            }
            R.id.three -> {
                scheduleTime = "3"
                true
            }
            R.id.four -> {
                scheduleTime = "4"
                true
            }
            R.id.five -> {
                scheduleTime = "5"
                true
            }
            R.id.six -> {
                scheduleTime = "6"
                true
            }
            R.id.saven -> {
                scheduleTime = "7"
                true
            }
            R.id.eight -> {
                scheduleTime = "8"
                true
            }
            else -> false
        }
    }
}