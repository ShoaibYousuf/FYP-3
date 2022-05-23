package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class OrderUpdateResponse (

  @SerializedName("status"    ) var status    : Int?            = null,
  @SerializedName("isSuccess" ) var isSuccess : Boolean?        = null,
  @SerializedName("data"      ) var data      : OrderData = OrderData(),
  @SerializedName("message"   ) var message   : String?         = null

)