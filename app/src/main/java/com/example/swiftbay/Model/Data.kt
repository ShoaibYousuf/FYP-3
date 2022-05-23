package com.example.swiftbay.Model

import com.example.swiftbay.Model.Product
import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("products"        ) var products        : ArrayList<Product> = arrayListOf(),
  @SerializedName("total"           ) var total           : Any?                = null,
  @SerializedName("deliveryCharges" ) var deliveryCharges : Int?                = null,
  @SerializedName("_id"             ) var _Id              : String?             = null,
  @SerializedName("user"            ) var user            : String?             = null,
  @SerializedName("createdAt"       ) var createdAt       : String?             = null,
  @SerializedName("updatedAt"       ) var updatedAt       : String?             = null,
  @SerializedName("__v"             ) var _v              : Int?                = null,
  @SerializedName("id"              ) var id              : String?             = null

)