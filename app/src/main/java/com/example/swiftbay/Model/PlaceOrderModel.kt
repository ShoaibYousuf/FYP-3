package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName

data class PlaceOrderModel(
    @SerializedName("destination") var destination     : Destination?        = Destination(),
    @SerializedName("paymentMethod")
    var paymentMethod : String = ""
    )
