package com.example.swiftbay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fazalmapbox.utils.APIInterface
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.Model.*
import com.example.swiftbay.helper.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleProduct : AppCompatActivity() {

    var product_image: ImageView? = null
    var name: TextView? = null
    var price: TextView? = null
    var description: TextView? = null
    var AddToCartBtn: Button? = null
    var QuantityToOrder: TextView? = null
    var plusBtn: Button? = null
    var minusBtn: Button? = null
    var productId = ""
    var quantity = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product)

        PrefsHelper.init(this)
        product_image = findViewById(R.id.product_image)
        name = findViewById(R.id.name)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        AddToCartBtn = findViewById(R.id.AddToCartBtn)
        QuantityToOrder = findViewById(R.id.QuantityToOrder)
        plusBtn = findViewById(R.id.plusBtn)
        minusBtn = findViewById(R.id.minusBtn)

        val extras = intent.extras
        if (extras != null) {
            val product =
                fromJson(extras.getString("Product").toString(), Product::class.java)
            name!!.text = product.productName
            price!!.text = product.price.toString()
            description!!.text = product.description
            Glide.with(this).load(product.productImage).into(product_image!!)
            QuantityToOrder!!.text = "1"

            productId =
                if (isEmptyField(product._id!!))
                    product.id!! else product._id!!
            plusBtn!!.setOnClickListener {
                QuantityToOrder!!.text = (QuantityToOrder!!.text.toString().toInt() + 1).toString()
            }
            minusBtn!!.setOnClickListener {
                if (QuantityToOrder!!.text.toString().toInt() > 1) {
                    QuantityToOrder!!.text =
                        (QuantityToOrder!!.text.toString().toInt() - 1).toString()
                }
            }

            AddToCartBtn!!.setOnClickListener {
                val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                if(isLogin){
                    callApi(productId, QuantityToOrder!!.text.toString().toInt())
                }else{
                    val i = Intent(this,LoginActivity::class.java)
                    startActivityForResult(i, 1)
                }


            }

        }

    }

    fun callApi(product: String, quantity: Int) {
        showDialoog(this, "")
        val token = "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "")
        val apiInterface = APIInterface.retrofit.create(APIInterface::class.java)
        val addToCartModel = AddToCartModel(product, quantity)
        val call: Call<AddToCartResponse> = apiInterface.addToCart(token, addToCartModel)
        call.enqueue(object : Callback<AddToCartResponse?> {
            override fun onResponse(
                call: Call<AddToCartResponse?>,
                response: Response<AddToCartResponse?>
            ) {
                if (response.body() != null) {
                    dismissDialoog()
                    Toast.makeText(this@SingleProduct, response.body()!!.comment, Toast.LENGTH_LONG)
                        .show()


                } else {
                    dismissDialoog()
                    showToast("Failled")
                }
            }

            override fun onFailure(call: Call<AddToCartResponse?>, t: Throwable) {
                dismissDialoog()
                showToast("Failled")
                //  Toast.makeText(this@SingleProduct, "Failed" Toast.LENGTH_LONG).show()
            }
        })

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                if(isLogin){
                    callApi(productId, QuantityToOrder!!.text.toString().toInt())
                }
            }
        }
    }
}