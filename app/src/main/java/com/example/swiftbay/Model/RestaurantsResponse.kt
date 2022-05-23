package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class RestaurantsResponse(

    @SerializedName("results") var results: ArrayList<Restaurant> = arrayListOf(),
    @SerializedName("page") var page: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalResults") var totalResults: Int? = null

)