package com.example.swiftbay.Model
import com.google.gson.annotations.SerializedName


data class Destination (

  @SerializedName("address"  ) var address  : String? = null,
  @SerializedName("latitude" ) var latitude : String? = null,
  @SerializedName("longitude" ) var longitude : String? = null

)