package com.example.swiftbay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.CreateResturantResponse
import com.example.swiftbay.Model.Restaurant
import com.example.swiftbay.helper.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantCreation : AppCompatActivity() {
    var restlogo: ImageView? = null
    private val PICK_IMAGE = 1000
    private var selectedAction = 0
    var mainImage: File? = null
    private val READ_STORAGE_PERMISSION = 100
    private val Location_PERMISSION = 123
    var restId: String = ""
    var nameEd: EditText? = null
    var cityEd: EditText? = null
    var addressEd: EditText? = null
    var descriptionEd: EditText? = null
    var createRestaurantButton: Button? = null
    var addlocation: Button? = null

    var address:String = ""
    var lat:String = ""
    var lon:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_creation)



        createRestaurantButton = findViewById(R.id.createRestaurantButton)
        nameEd = findViewById(R.id.nameEd)
        cityEd = findViewById(R.id.cityEd)
       // addressEd = findViewById(R.id.addressEd)
        descriptionEd = findViewById(R.id.descriptionEd)
        restlogo = findViewById(R.id.rest_logo)
        addlocation = findViewById(R.id.addlocation)
        restlogo!!.setOnClickListener {
            if (!isReadStoragePermissionGranted) {
                // Permission is not granted
                askReadStoragePermission()
            } else {
                openGallery(PICK_IMAGE)
            }
        }


        val extras = intent.extras
        if (extras != null) {

            val restaurant = fromJson(extras.getString("Restaurant").toString(), Restaurant::class.java)
            nameEd!!.setText(restaurant.name)
            cityEd!!.setText(restaurant.city)
            descriptionEd!!.setText(restaurant.description)

            Glide.with(this@RestaurantCreation).load(restaurant.image).into(restlogo!!)
            createRestaurantButton!!.setText("Update Restaurant")
            restId =
                if(isEmptyField(restaurant._id!!) )
                    restaurant.id!! else restaurant._id!!
            createRestaurantButton!!.setOnClickListener {
                Updatecall()
            }

        } else {

            createRestaurantButton!!.setOnClickListener {
                createdCall()
            }
            addlocation!!.setOnClickListener {if (!isLocationPermissionGranted) {
                // Permission is not granted
                askReadLocationPermission()
            } else {
                val i = Intent(this, MapsActivity2::class.java)
                startActivityForResult(i, 1)
            }

            }

        }

    }


    fun createdCall() {
        if (!isEmptyField(nameEd!!.text.toString())) {
            if (!isEmptyField(cityEd!!.text.toString())) {
                if (!isEmptyField(address)) {
                    if (!isEmptyField(descriptionEd!!.text.toString())) {
                        if (mainImage != null) {
                            callApi(
                                nameEd!!.text.toString(),
                                descriptionEd!!.text.toString(),
                                cityEd!!.text.toString(),
                                mainImage!!
                               ,address,
                                lat,
                                lon)

                        } else {

                            showToast("please select image")
                        }
                    } else {

                        showToast("please enter description")
                    }
                } else {

                    showToast("please Select address")
                }
            } else {

                showToast("please enter city")
            }
        } else {
            showToast("please enter name")
        }

    }

    fun Updatecall() {
        if (!isEmptyField(nameEd!!.text.toString())) {
            if (!isEmptyField(cityEd!!.text.toString())) {
                if (!isEmptyField(addressEd!!.text.toString())) {
                    if (!isEmptyField(descriptionEd!!.text.toString())) {
                        if (mainImage != null) {
                            updatecallApi(
                                restId,
                                nameEd!!.text.toString(),
                                descriptionEd!!.text.toString(),
                                cityEd!!.text.toString(),
                                mainImage!!)

                        } else {

                            updatecallApi2(
                                restId,
                                nameEd!!.text.toString(),
                                descriptionEd!!.text.toString(),
                                cityEd!!.text.toString(),
                            )
                        }
                    } else {

                        showToast("please enter description")
                    }
                } else {

                    showToast("please enter adress")
                }
            } else {

                showToast("please enter city")
            }
        } else {
            showToast("please enter name")
        }

    }

    private fun askReadStoragePermission() {
        ActivityCompat.requestPermissions(
            this@RestaurantCreation, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_STORAGE_PERMISSION
        )
    }

    private fun askReadLocationPermission() {
        ActivityCompat.requestPermissions(
            this@RestaurantCreation, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Location_PERMISSION
        )
    }

    private val isLocationPermissionGranted: Boolean
        private get() {
            var isGranted = false
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = true
            }
            return isGranted
        }

 private val isReadStoragePermissionGranted: Boolean
        private get() {
            var isGranted = false
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = true
            }
            return isGranted
        }


    private fun openGallery(actionIndicator: Int) {
        selectedAction = actionIndicator
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.type = "image/*"
        startActivityForResult(gallery, actionIndicator)
    }


    fun callApi(name: String, desscription: String, city: String, image: File,address: String,latitude:String,longitude:String) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        var imageBody: MultipartBody.Part? = null
        imageBody = MulitiPratHelper.convertToMultiPart(image, "productImage")
        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val cityBody = convertToBody(city)
        val addressBody = convertToBody(address)
        val latBody = convertToBody(latitude)
        val lonBody = convertToBody(longitude)

        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateResturantResponse> =
            apiInterface.createRestaurants(
                token,
                nameBody,
                desscriptionBody,
                cityBody,
                imageBody,addressBody,latBody,lonBody
            )
        call.enqueue(object : Callback<CreateResturantResponse?> {
            override fun onResponse(
                call: Call<CreateResturantResponse?>,
                response: Response<CreateResturantResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateResturantResponse =
                        response.body() as CreateResturantResponse
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()


                } else {
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateResturantResponse?>, t: Throwable) {
                Toast.makeText(this@RestaurantCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updatecallApi(
        restId: String,
        name: String,
        desscription: String,
        city: String,
        image: File
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        var imageBody: MultipartBody.Part? = null
        imageBody = MulitiPratHelper.convertToMultiPart(image, "productImage")
        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val cityBody = convertToBody(city)


        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateResturantResponse> =
            apiInterface.updateRestaurants(
                restId,
                token,
                nameBody,
                desscriptionBody,
                cityBody,
                imageBody
            )
        call.enqueue(object : Callback<CreateResturantResponse?> {
            override fun onResponse(
                call: Call<CreateResturantResponse?>,
                response: Response<CreateResturantResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateResturantResponse =
                        response.body() as CreateResturantResponse
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateResturantResponse?>, t: Throwable) {
                Toast.makeText(this@RestaurantCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updatecallApi2(
        restId: String,
        name: String,
        desscription: String,
        city: String,
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val cityBody = convertToBody(city)

        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateResturantResponse> =
            apiInterface.updateRestaurants(
                restId,
                token,
                nameBody,
                desscriptionBody,
                cityBody,
            )
        call.enqueue(object : Callback<CreateResturantResponse?> {
            override fun onResponse(
                call: Call<CreateResturantResponse?>,
                response: Response<CreateResturantResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateResturantResponse =
                        response.body() as CreateResturantResponse
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        this@RestaurantCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateResturantResponse?>, t: Throwable) {
                Toast.makeText(this@RestaurantCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                address = data!!.getStringExtra("address")!!
                lat = data.getStringExtra("lat")!!
                lon = data.getStringExtra("lon")!!
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == READ_STORAGE_PERMISSION) {
            openGallery(selectedAction)
        }else if (resultCode == Activity.RESULT_OK && requestCode == Location_PERMISSION) {
            val i = Intent(this, MapsActivity2::class.java)
            startActivityForResult(i, 1)
        } else if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            lifecycleScope.launch {
                if (data != null && data.data != null) {
                    mainImage = this@RestaurantCreation.compressInstance(
                        getFile(getRealPathFromURI(data.data!!))
                    )
                    Glide.with(this@RestaurantCreation).load(mainImage).into(restlogo!!)

                }
            }
        }
    }
}