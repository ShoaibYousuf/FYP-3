package com.example.swiftbay

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.Destination
import com.example.swiftbay.Model.OrderResponse
import com.example.swiftbay.Model.PlaceOrderModel
import com.example.swiftbay.databinding.ActivityMapsBinding
import com.example.swiftbay.databinding.MapsActivity2Binding
import com.example.swiftbay.helper.GPSTracker
import com.example.swiftbay.helper.isEmptyField
import com.example.swiftbay.helper.showToast
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


class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: MapsActivity2Binding
    var gpsTracker :GPSTracker ?= null
    var geocoder :Geocoder? = null
    var latLng:LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MapsActivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        gpsTracker = GPSTracker(this)
        geocoder = Geocoder(this)

        binding.search.setOnClickListener {
            if(!isEmptyField(binding.edSerch.text.toString())){
               binding.edSerch.setText(getLocationName(binding.edSerch.text.toString()))
            }
        }
        binding.placeOrder.setOnClickListener {
            if(!isEmptyField(binding.edSerch.text.toString())){
               if(latLng != null){
                   val intent =  Intent()
                   intent.putExtra("address", binding.edSerch.text.toString())
                   intent.putExtra("lat", latLng!!.latitude.toString())
                   intent.putExtra("lon", latLng!!.longitude.toString())
                   setResult(RESULT_OK, intent)
                   finish()
               }else{
                   showToast("Address is empty")
               }
            }else{
                showToast("Address is empty")
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
        mMap.isMyLocationEnabled  = true
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
                Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        val intent =  Intent()
        intent.putExtra("address", binding.edSerch.text.toString())
        intent.putExtra("lat", latLng!!.latitude.toString())
        intent.putExtra("lon", latLng!!.longitude.toString())
        setResult(RESULT_OK, intent)
        finish()
    }


}