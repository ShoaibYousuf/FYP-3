package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DeleteCartModel(
        @SerializedName("product")
        @Expose
        var product: String = ""
)
