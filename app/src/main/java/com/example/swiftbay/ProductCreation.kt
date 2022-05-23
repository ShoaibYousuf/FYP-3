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
import com.example.swiftbay.Model.CreateProductResponse
import com.example.swiftbay.Model.Product
import com.example.swiftbay.helper.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProductCreation : AppCompatActivity() {

    private val PICK_IMAGE = 1000
    private var selectedAction = 0
    var mainImage: File? = null
    private val READ_STORAGE_PERMISSION = 1
    var product_image: ImageView? = null
    var createProductButton: Button? = null
    var nameEd: EditText? = null
    var descriptionEd: EditText? = null
    var priceEd: EditText? = null
    var productId: String = ""
    var restId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_creation)

        product_image = findViewById(R.id.product_image)
        createProductButton = findViewById(R.id.createProductButton)
        nameEd = findViewById(R.id.nameEd)
        descriptionEd = findViewById(R.id.descriptionEd)
        priceEd = findViewById(R.id.priceEd)

        product_image!!.setOnClickListener {
            if (!isReadStoragePermissionGranted) {
                // Permission is not granted
                askReadStoragePermission()
            } else {
                openGallery(PICK_IMAGE)
            }
        }

        val extras = intent.extras
        if (extras != null) {
           restId = extras.getString("restId").toString()
            if(isEmptyField(restId)){
                val product =
                    fromJson(extras.getString("Product").toString(), Product::class.java)
                nameEd!!.setText(product.productName)
                descriptionEd!!.setText(product.description)
                priceEd!!.setText(product.price.toString())
                Glide.with(this@ProductCreation).load(product.productImage).into(product_image!!)
                createProductButton!!.text="Update Product"
                productId =
                    if (isEmptyField(product._id!!))
                        product.id!! else product._id!!

                createProductButton!!.setOnClickListener {
                    Updatecall()
                }
            } else {
                createProductButton!!.setOnClickListener {
                    createCall()
                }
            }

        }
    }

    fun Updatecall() {

        if (!isEmptyField(nameEd!!.text.toString())) {
            if (!isEmptyField(descriptionEd!!.text.toString())) {
                if (!isEmptyField(priceEd!!.text.toString())) {
                    if (mainImage != null) {
                        updatecallApi(
                            productId,
                            nameEd!!.text.toString(),
                            descriptionEd!!.text.toString(),
                            priceEd!!.text.toString(),
                            mainImage!!)
                    }
                    else {
                        updatecallApi2(productId,
                            nameEd!!.text.toString(),
                            descriptionEd!!.text.toString(),
                            priceEd!!.text.toString())
                    }
                } else {
                    showToast("please enter price")
                }
            } else {
                showToast("please enter description")
            }
        } else {
            showToast("please enter name")
        }

    }

    fun createCall() {
        if (!isEmptyField(nameEd!!.text.toString())) {
            if (!isEmptyField(descriptionEd!!.text.toString())) {
                if (!isEmptyField(priceEd!!.text.toString())) {
                    if (mainImage != null) {
                        callApi(
                            restId,
                            nameEd!!.text.toString(),
                            descriptionEd!!.text.toString(),
                            priceEd!!.text.toString(),
                            mainImage!!)
                    } else {
                        showToast("please Select image")
                    }
                } else {
                    showToast("please enter price")
                }
            } else {
                showToast("please enter description")
            }
        } else {
            showToast("please enter name")
        }

    }


    fun callApi(

        restId: String,
        name: String,
        desscription: String,
        price: String,
        image: File
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        var imageBody: MultipartBody.Part? = null
        imageBody = MulitiPratHelper.convertToMultiPart(image, "productImage")
        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val priceBody = convertToBody(price)

        showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateProductResponse> =
            apiInterface.createProduct(
                restId,
                token,
                nameBody,
                desscriptionBody,
                priceBody,
                imageBody
            )
        call.enqueue(object : Callback<CreateProductResponse?> {
            override fun onResponse(
                call: Call<CreateProductResponse?>,
                response: Response<CreateProductResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateProductResponse =
                        response.body() as CreateProductResponse
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateProductResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@ProductCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updatecallApi(
        productId: String,
        name: String,
        desscription: String,
        price: String,
        image: File
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        var imageBody: MultipartBody.Part? = null
        imageBody = MulitiPratHelper.convertToMultiPart(image, "productImage")
        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val priceBody = convertToBody(price)

        showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateProductResponse> =
            apiInterface.updateProduct(
                productId,
                token,
                nameBody,
                desscriptionBody,
                priceBody,
                imageBody
            )
        call.enqueue(object : Callback<CreateProductResponse?> {
            override fun onResponse(
                call: Call<CreateProductResponse?>,
                response: Response<CreateProductResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateProductResponse =
                        response.body() as CreateProductResponse
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateProductResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@ProductCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

 fun updatecallApi2(
        productId: String,
        name: String,
        desscription: String,
        price: String
    ) {
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")

        val nameBody = convertToBody(name)
        val desscriptionBody = convertToBody(desscription)
        val priceBody = convertToBody(price)

     showDialoog(this,"")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val call: Call<CreateProductResponse> =
            apiInterface.updateProduct(
                productId,
                token,
                nameBody,
                desscriptionBody,
                priceBody
            )
        call.enqueue(object : Callback<CreateProductResponse?> {
            override fun onResponse(
                call: Call<CreateProductResponse?>,
                response: Response<CreateProductResponse?>
            ) {
                if (response.body() != null) {
                    val createResturantResponse: CreateProductResponse =
                        response.body() as CreateProductResponse
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Success  : ${response.message()}",
                        Toast.LENGTH_LONG

                    ).show()
                    finish()
                } else {
                    dismissDialoog()
                    Toast.makeText(
                        this@ProductCreation,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CreateProductResponse?>, t: Throwable) {
                dismissDialoog()
                Toast.makeText(this@ProductCreation, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun askReadStoragePermission() {
        ActivityCompat.requestPermissions(
            this@ProductCreation, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_STORAGE_PERMISSION
        )
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == READ_STORAGE_PERMISSION) {
            openGallery(selectedAction)
        } else if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            lifecycleScope.launch {
                if (data != null && data.data != null) {
                    mainImage = this@ProductCreation.compressInstance(
                        getFile(getRealPathFromURI(data.data!!))
                    )
                    Glide.with(this@ProductCreation).load(mainImage).into(product_image!!)

                }
            }
        }
    }

}