package com.example.swiftbay.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UpdateStutsModel(
        @SerializedName("status")
        @Expose
        var status: String = ""

)
