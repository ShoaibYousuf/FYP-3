package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName


data class LoginResponse(

    @SerializedName("user") var user: User? = User(),
    @SerializedName("token") var token: String? = null,

)