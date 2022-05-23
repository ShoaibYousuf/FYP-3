package com.example.swiftbay.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.swiftbay.SwiftBuy
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


var progressDialog: ProgressDialog? = null

fun showDialoog(context: Context,title:String){
    progressDialog = ProgressDialog(context);
    progressDialog!!.setMessage("Loading..."); // Setting Message
    progressDialog!!.setTitle(title); // Setting Title
    progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
    progressDialog!!.show(); // Display Progress Dialog
    progressDialog!!.setCancelable(false);
}

fun dismissDialoog(){
    if(progressDialog!= null && progressDialog!!.isShowing){
        progressDialog!!.dismiss()
    }
}

private fun loggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.d("RAW_RESPONSE", message)
        }
    })
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return interceptor
}

val okHttpClient: OkHttpClient
    get() {
        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
        builder.connectTimeout(90, TimeUnit.SECONDS)
        builder.readTimeout(270, TimeUnit.SECONDS)
        builder.writeTimeout(90, TimeUnit.SECONDS)
        builder.addInterceptor(loggingInterceptor())
        return builder.build()
    }


fun Context.getFormattedAmount(amount: Any?): String {
    when (amount) {
        null -> {
            return ""
        }
        is Long -> {
            return   "PKR " + NumberFormat.getNumberInstance(Locale.US)
                .format(amount)
        }
        is Float -> {
            return  "PKR " + NumberFormat.getNumberInstance(Locale.US)
                .format(amount)
        }

        is Double -> {
            return  "PKR " + NumberFormat.getNumberInstance(Locale.US)
                .format(amount)
        }
        else -> return ""
    }
}

fun Activity.openRouteIntent(src: String, des: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=" + src + "&daddr=" + des)
        )
        startActivity(intent)
    } catch (ane: ActivityNotFoundException) {
        checkMap()
    }
}

fun Activity.checkMap() {
    val builder = AlertDialog.Builder(Objects.requireNonNull(this))
    builder.setMessage("")
        .setCancelable(false)
        .setPositiveButton(
            "Yes"
        ) { dialog, id ->
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                    )
                )
            }
        }
        .setNegativeButton(
            "Cancel"
        ) { dialog, id -> dialog.cancel() }
    val alert = builder.create()
    alert.show()
}

fun toJson(value: Any): String {
    return GsonBuilder().create().toJson(value)
}

fun <T> fromJson(json: String, aClass: Class<T>): T {
    return GsonBuilder().create().fromJson(json, aClass)
}

fun convertToBody(text:String) : RequestBody{
    return  RequestBody.create(
        "text/plain".toMediaTypeOrNull(),
        text
    )
}

suspend fun Context.compressInstance(file: File): File {
    return Compressor.compress(this, file) {
        resolution(720, 720)
        quality(50)
        size(1_097_152) // 1 MB
    }
}

fun getFile(productImageUri: String): File {
    val file = File(productImageUri)
    Log.d("viewModel", file.path)
    return file
}

fun Context.showToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun isEmptyField(value: Any?): Boolean {
    if (value == null) {
        return true
    } else {
        when (value) {
            is String -> {
                val x: String = value
                return x.trim().isEmpty()
            }
            is TextInputEditText -> {
                val x: TextInputEditText = value
                return TextUtils.isEmpty(x.text.toString().trim())
            }
            is AppCompatEditText -> {
                val x: AppCompatEditText = value
                return TextUtils.isEmpty(x.text.toString().trim())
            }
            is EditText -> {
                val x: EditText = value
                return TextUtils.isEmpty(x.text.toString().trim())
            }
            is ArrayList<*> -> return value.size == 0
        }
    }
    return true
}

fun getRealPathFromURI(contentUri: Uri?): String {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    @SuppressLint("Recycle") val cursor = SwiftBuy.context!!.contentResolver.query(
        contentUri!!, proj,
        null, null, null
    )!!
    val column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
}

