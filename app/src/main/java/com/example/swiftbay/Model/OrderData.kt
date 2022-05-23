package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class OrderData(

    @SerializedName("products") var products: ArrayList<Product> = arrayListOf(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("deliveryCharges") var deliveryCharges: Int? = null,
    @SerializedName("paymentMethod") var paymentMethod: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("_id") var _id: String? = null,
    @SerializedName("user") var user: String? = null,
    @SerializedName("source") var source: Source? = Source(),
    @SerializedName("destination") var destination: Destination? = Destination(),
    @SerializedName("resturaunt") var resturaunt: Restaurant? = Restaurant(),
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("timeRemaining") var timeRemaining: Int? = null,
    @SerializedName("id") var id: String? = null

)