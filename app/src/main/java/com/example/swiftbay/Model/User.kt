package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("_id"      ) var Id       : String? = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("email"    ) var email    : String? = null,
  @SerializedName("password" ) var password : String? = null,
  @SerializedName("role"     ) var role     : String? = null,

)