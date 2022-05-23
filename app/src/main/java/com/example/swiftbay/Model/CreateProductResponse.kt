package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class CreateProductResponse (

  @SerializedName("status"  ) var status  : String? = null,
  @SerializedName("success" ) var success : String? = null,
  @SerializedName("result"  ) var result  : Product? = Product(),
  @SerializedName("message" ) var message : String? = null

)