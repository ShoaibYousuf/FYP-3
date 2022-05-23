package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ModelSignup(
        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("email")
        @Expose
        var email: String = "",

        @SerializedName("password")
        @Expose
        var password: String = "",

        @SerializedName("role")
        @Expose
        var role: String = ""



)
