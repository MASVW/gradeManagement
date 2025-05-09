package com.smp5.app

import android.content.Context
import android.content.SharedPreferences

class AuthPrefs(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "token"
    }

    fun saveToken(token: String) {
        sharedPref.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPref.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        sharedPref.edit().remove(KEY_TOKEN).apply()
    }
}