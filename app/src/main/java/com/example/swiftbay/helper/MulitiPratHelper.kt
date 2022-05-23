package com.example.swiftbay.helper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object MulitiPratHelper {

    @JvmStatic
    fun convertToMultiPart(file: File,name: String): MultipartBody.Part? {
        try {
            val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaType())

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(name, System.currentTimeMillis().toString() + ".jpeg", requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}