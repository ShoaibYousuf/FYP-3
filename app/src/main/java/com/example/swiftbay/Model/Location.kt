package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class Location (

  @SerializedName("address"   ) var address   : String? = null,
  @SerializedName("latitude"  ) var  latitude : Double? = null,
  @SerializedName("longitude" ) var longitude : Double? = null

)