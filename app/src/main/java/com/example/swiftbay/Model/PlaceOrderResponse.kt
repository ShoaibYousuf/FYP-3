package com.example.example

import com.example.swiftbay.Model.OrderData
import com.google.gson.annotations.SerializedName


data class ExampleJson2KtKotlin (

  @SerializedName("status"    ) var status    : Int?     = null,
  @SerializedName("isSuccess" ) var isSuccess : Boolean? = null,
  @SerializedName("data"      ) var data      : OrderData?    = OrderData(),
  @SerializedName("message"   ) var message   : String?  = null

)