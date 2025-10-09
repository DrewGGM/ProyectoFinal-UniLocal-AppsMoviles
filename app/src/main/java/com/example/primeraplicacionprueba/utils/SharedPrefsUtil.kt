package com.example.primeraplicacionprueba.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsUtil {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"

    fun savePreferences(context: Context, userId: String, role: String, name: String = "", email: String = "") {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USER_ROLE, role)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    fun getPreferences(context: Context): Map<String, String> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "userId" to (prefs.getString(KEY_USER_ID, "") ?: ""),
            "role" to (prefs.getString(KEY_USER_ROLE, "") ?: ""),
            "name" to (prefs.getString(KEY_USER_NAME, "") ?: ""),
            "email" to (prefs.getString(KEY_USER_EMAIL, "") ?: "")
        )
    }

    fun clearPreferences(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getString(KEY_USER_ID, "")
        return !userId.isNullOrEmpty()
    }
}
