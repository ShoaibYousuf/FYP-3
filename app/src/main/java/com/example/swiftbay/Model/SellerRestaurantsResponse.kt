package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class SellerRestaurantsResponse(

    @SerializedName("status") var status:String? =null,
    @SerializedName("success") var success:String? =null,
    @SerializedName("result") var result: ArrayList<Restaurant> = arrayListOf(),
    @SerializedName("message") var message:String? =null,

    )