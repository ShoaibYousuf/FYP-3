package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class AllProductsResponse (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("success" ) var success : String?           = null,
  @SerializedName("result"  ) var result  : ArrayList<Product> = arrayListOf(),
  @SerializedName("message" ) var message : String?           = null

)