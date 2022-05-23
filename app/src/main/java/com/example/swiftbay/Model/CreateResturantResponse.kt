package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class CreateResturantResponse(

    @SerializedName("result") var restaurant: Restaurant? =Restaurant(),

    )