package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class AddToCartResponse (

  @SerializedName("status"   ) var status   : Int?     = null,
  @SerializedName("isSucces" ) var isSucces : Boolean? = null,
  @SerializedName("data"     ) var data     : Data?    = Data(),
  @SerializedName("comment"  ) var comment  : String?  = null

)