package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelCreateResturant (
    @SerializedName("name")
    @Expose
    var name : String = "",

    @SerializedName("description")
    @Expose
    var description : String = "",

    @SerializedName("city")
    @Expose
    var city  : String = "",

    @SerializedName("address")
    @Expose
    var address : String = "",
)
