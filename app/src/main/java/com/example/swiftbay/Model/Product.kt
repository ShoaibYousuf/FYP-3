package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class Product(

    @SerializedName("id") var id: String? = null,
    @SerializedName("productName") var productName: String? = null,
    @SerializedName("price") var price: Any? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("seller") var seller: String? = null,
    @SerializedName("resturaunt") var resturaunt: String? = null,
    @SerializedName("productImage") var productImage: String? = null,
    @SerializedName("_id") var _id: String? = null

)