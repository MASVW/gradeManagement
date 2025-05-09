package com.smp5.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import com.smp5.app.ui.LoginActivity

class AuthPrefs(private val context: Context) {
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

    fun logoutAndRedirect() {
        clearToken()

        Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
