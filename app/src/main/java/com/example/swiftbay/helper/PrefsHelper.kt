package com.example.fazalmapbox.utils

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {

    private lateinit var prefs: SharedPreferences

    private const val PREFS_NAME = "FoodPanda"

    const val TOKEN = "token"
    const val ROLE = "role"
    const val LOGIN = "login"


    public fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun hasKey(key: String): Boolean {
        return prefs.contains(key)
    }

    fun read(key: String, value: Boolean): Boolean {
        return prefs.getBoolean(key, value)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            apply()
        }
    }

    fun read(key: String, value: Int): Int {
        return prefs.getInt(key, value)
    }

    fun write(key: String, value: Int) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putInt(key, value)
            apply()
        }
    }

    fun read(key: String, value: String): String? {
        return prefs.getString(key, value)
    }

    fun clear(key: String) {
        prefs.edit().remove(key).commit();
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            apply()
        }
    }

}