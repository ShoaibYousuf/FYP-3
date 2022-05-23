package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AddToCartModel(
        @SerializedName("product")
        @Expose
        var product: String = "",

        @SerializedName("quantity")
        @Expose
        var quantity: Int = 1,

)
