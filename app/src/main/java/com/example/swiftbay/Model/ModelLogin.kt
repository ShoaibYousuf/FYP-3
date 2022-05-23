package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelLogin (
    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("password")
    @Expose
    var password: String = ""
)
