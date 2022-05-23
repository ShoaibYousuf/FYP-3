package com.example.swiftbay.Model

import com.google.gson.annotations.SerializedName

class Restaurant(
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("owner")
    var owner: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("location")
    var location: Location? = Location(),
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("__v")
    var _v: Int? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("image")
    var image: String? = null
)