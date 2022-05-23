package com.example.swiftbay

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SwiftBuy :Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }

    companion object {
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }
}